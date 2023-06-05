package com.poshtarenko.codeforge.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.FinishTestRequest;
import com.poshtarenko.codeforge.dto.request.UpdateResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.Result;
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
public class ResultControllerTestIT {

    private static final String BASE_URL = "/result";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestDataInitializer dataInitializer;

    Result result;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        result = dataInitializer.getResult();
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @SneakyThrows
    public void createResult() {
        FinishTestRequest request = new FinishTestRequest(
                result.getTest().getId(),
                null
        );

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewResultDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewResultDTO.class
        );

        assertEquals(response.testId(), request.testId());
    }

    @Test
    @SneakyThrows
    public void viewResult() {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + result.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewResultDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewResultDTO.class
        );

        assertEquals(result.getId(), response.id());
        assertEquals(result.getScore(), response.score());
        assertEquals(result.getRespondent().getId(), response.respondentId());
        assertEquals(result.getTest().getId(), response.testId());
    }

    @Test
    @SneakyThrows
    public void updateResult() {
        UpdateResultDTO request = new UpdateResultDTO(
                result.getId()
        );

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + result.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertEquals(response.id(), result.getId());
    }

    @Test
    @SneakyThrows
    public void deleteResult() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + result.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
