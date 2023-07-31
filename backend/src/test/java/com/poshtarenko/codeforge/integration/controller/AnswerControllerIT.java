package com.poshtarenko.codeforge.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.integration.IntegrationTestBase;
import com.poshtarenko.codeforge.integration.controller.data.TestDataInitializer;
import com.poshtarenko.codeforge.integration.controller.security.MockUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class AnswerControllerIT extends IntegrationTestBase {

    private static final String BASE_URL = "/answers";

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
    @MockUser(role = ERole.RESPONDENT)
    public void findAnswer() throws Exception {
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
    @MockUser(role = ERole.AUTHOR)
    public void findTestAnswers() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/by_test/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<ViewAnswerDTO> response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<ViewAnswerDTO>>() {
                }
        );

        response.forEach(this::assertAnswersEquals);
    }

    @Test
    @MockUser(role = ERole.RESPONDENT)
    public void findRespondentCurrentAnswer() throws Exception {
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
    @MockUser(role = ERole.RESPONDENT)
    public void startTest() throws Exception {
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
    @MockUser(role = ERole.RESPONDENT)
    public void finishAnswer() throws Exception {
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
    @MockUser(role = ERole.RESPONDENT)
    public void deleteResult() throws Exception {
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
