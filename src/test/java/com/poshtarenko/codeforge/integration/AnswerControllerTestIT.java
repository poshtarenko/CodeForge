package com.poshtarenko.codeforge.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.integration.data.TestDataInitializer;
import com.poshtarenko.codeforge.integration.security.WithMockCustomUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
@WithMockCustomUser(role = ERole.RESPONDENT)
public class SolutionControllerTestIT {

    private static final String BASE_URL = "/result";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestDataInitializer dataInitializer;

    Answer answer;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        answer = dataInitializer.getAnswer();
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @SneakyThrows
    public void viewResult() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + answer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertEquals(answer.getId(), response.id());
        assertEquals(answer.getScore(), response.score());
        assertEquals(answer.getRespondent().getId(), response.respondent().id());
        assertEquals(answer.getTest().getId(), response.testId());
    }

    @Test
    @SneakyThrows
    public void updateResult() {
        UpdateAnswerDTO request = new UpdateAnswerDTO(
                answer.getId()
        );

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + answer.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewSolutionDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewSolutionDTO.class
        );

        assertEquals(response.id(), answer.getId());
    }

    @Test
    @SneakyThrows
    public void deleteResult() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + answer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
