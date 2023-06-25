package com.poshtarenko.codeforge.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.integration.annotation.MvcTest;
import com.poshtarenko.codeforge.integration.controller.data.TestDataInitializer;
import com.poshtarenko.codeforge.integration.controller.security.WithMockCustomUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MvcTest
@WithMockCustomUser(role = ERole.RESPONDENT)
@RequiredArgsConstructor
public class AnswerControllerIT {

    private static final String BASE_URL = "/answer";

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final TestDataInitializer dataInitializer;

    Answer answer;
    com.poshtarenko.codeforge.entity.Test test;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        answer = dataInitializer.getAnswer();
        test = dataInitializer.getTest();
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @SneakyThrows
    public void findAnswer() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + answer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertAnswersEquals(response);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = ERole.AUTHOR)
    public void findTestAnswers() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/by_test/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<ViewAnswerDTO> response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<ViewAnswerDTO>>(){}
        );

        response.forEach(this::assertAnswersEquals);
    }

    @Test
    @SneakyThrows
    public void findRespondentCurrentAnswer() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/current/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertAnswersEquals(response);
    }

    @Test
    @SneakyThrows
    public void startTest() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/start_test/" + test.getInviteCode()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertFalse(response.isFinished());
        assertNull(response.score());
    }

    @Test
    @SneakyThrows
    public void finishAnswer() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/start_test/" + test.getInviteCode()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertFalse(response.isFinished());
        assertNull(response.score());

        MvcResult mvcResult2 = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/finish/" + response.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response2 = objectMapper.readValue(
                mvcResult2.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertTrue(response2.isFinished());
        assertNotNull(response2.score());
    }

    @Test
    @SneakyThrows
    public void deleteResult() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + answer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private void assertAnswersEquals(ViewAnswerDTO response) {
        assertEquals(answer.getId(), response.id());
        assertEquals(answer.getScore(), response.score());
        assertEquals(answer.getRespondent().getId(), response.respondent().id());
        assertNotNull(answer.getIsFinished());
        assertEquals(answer.getTest().getId(), response.testId());
    }

}
