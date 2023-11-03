package com.poshtarenko.codeforge.integration.controller.websocket;

import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.request.UpdateParticipationCodeDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
import com.poshtarenko.codeforge.entity.lesson.Participation;
import com.poshtarenko.codeforge.integration.IntegrationTest;
import com.poshtarenko.codeforge.integration.annotation.WebsocketTest;
import com.poshtarenko.codeforge.integration.controller.websocket.util.WebSocketClient;
import com.poshtarenko.codeforge.integration.data.TestDataInitializer;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebsocketTest
@RequiredArgsConstructor
public class LessonWsControllerIT extends IntegrationTest {

    private final TestDataInitializer dataInitializer;
    private final WebSocketClient webSocketClient;

    @MockBean
    CodeEvaluationProvider codeEvaluationProvider;

    @Value("${local.server.port}")
    private int port;
    private String URL;
    private CompletableFuture<ViewLessonDTO> lessonCompletableFuture;
    private CompletableFuture<ViewParticipationDTO> participationCompletableFuture;

    private Lesson lesson;
    private Participation participation;

    private final EvaluationResult expectedEvaluationResult = new EvaluationResult("SUCCESS", null);

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        lesson = dataInitializer.getLesson();
        participation = dataInitializer.getParticipation();

        URL = "ws://127.0.0.1:" + port + "/ws";
        lessonCompletableFuture = new CompletableFuture<>();
        participationCompletableFuture = new CompletableFuture<>();
        when(codeEvaluationProvider.evaluateCode(any())).thenReturn(expectedEvaluationResult);
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }


    @Test
    @SneakyThrows
    public void updateLessonDesc() {
        StompSession stompSession = webSocketClient.connect(URL, dataInitializer.getAuthor());

        String sendUrl = "/app/lesson/%s/update_description".formatted(lesson.getId());
        UpdateLessonDescriptionDTO sendData = new UpdateLessonDescriptionDTO(lesson.getId(), "New desc");
        stompSession.send(sendUrl, sendData);

        String subscribeUrl = "/topic/lesson/%s/description_updates".formatted(lesson.getId());
        stompSession.subscribe(subscribeUrl, new LessonStompFrameHandler());

        ViewLessonDTO lesson = lessonCompletableFuture.get(5, SECONDS);

        assertNotNull(lesson);
        assertEquals(lesson.id(), sendData.id());
        assertEquals(lesson.description(), sendData.description());
    }

    @Test
    @SneakyThrows
    public void updateParticipationCode() {
        StompSession stompSession = webSocketClient.connect(URL, dataInitializer.getAuthor());

        String sendUrl = "/app/lesson/%s/participation/update_code".formatted(lesson.getId());
        UpdateParticipationCodeDTO sendData = new UpdateParticipationCodeDTO(participation.getId(), "New code");
        stompSession.send(sendUrl, sendData);

        String subscribeUrl = "/topic/lesson/%s/participation_updates".formatted(lesson.getId());
        stompSession.subscribe(subscribeUrl, new ParticipationStompFrameHandler());

        ViewParticipationDTO participation = participationCompletableFuture.get(5, SECONDS);

        assertNotNull(participation);
        assertEquals(participation.id(), sendData.id());
        assertEquals(participation.code(), sendData.code());
    }

    @Test
    @SneakyThrows
    public void evaluateParticipationCode() {
        StompSession stompSession = webSocketClient.connect(URL, dataInitializer.getAuthor());

        String sendUrl = "/app/lesson/%s/participation/%s/evaluate_code".formatted(lesson.getId(), participation.getId());
        stompSession.send(sendUrl, null);

        String subscribeUrl = "/topic/lesson/%s/participation_updates".formatted(lesson.getId());
        stompSession.subscribe(subscribeUrl, new ParticipationStompFrameHandler());

        ViewParticipationDTO participation = participationCompletableFuture.get(5, SECONDS);

        assertEquals(participation.evaluationResult().output(), expectedEvaluationResult.getOutput());
        assertEquals(participation.evaluationResult().error(), expectedEvaluationResult.getError());
    }

    private class LessonStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ViewLessonDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            lessonCompletableFuture.complete((ViewLessonDTO) o);
        }
    }

    private class ParticipationStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ViewParticipationDTO.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            participationCompletableFuture.complete((ViewParticipationDTO) o);
        }
    }

}
