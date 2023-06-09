package com.poshtarenko.codeforge.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.TryCodeResponse;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.Solution;
import com.poshtarenko.codeforge.integration.IntegrationTestBase;
import com.poshtarenko.codeforge.integration.controller.data.TestDataInitializer;
import com.poshtarenko.codeforge.integration.controller.security.MockUser;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
public class SolutionControllerIT extends IntegrationTestBase {

    private static final String BASE_URL = "/solutions";

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final TestDataInitializer dataInitializer;

    @MockBean
    CodeEvaluationProvider codeEvaluationProvider;

    Solution solution;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        solution = dataInitializer.getSolution();
        when(codeEvaluationProvider.evaluateCode(any())).thenReturn(new CodeEvaluationResult(true, 1L, Optional.empty()));
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @MockUser(role = ERole.RESPONDENT)
    public void findSolution() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + solution.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewSolutionDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewSolutionDTO.class
        );

        assertEquals(solution.getId(), response.id());
        assertEquals(solution.getCode(), response.code());
        assertEquals(solution.getAnswer().getId(), response.answerId());
        assertEquals(solution.getTask().getId(), response.taskId());
    }

    @Test
    @MockUser(role = ERole.RESPONDENT)
    public void putSolution() throws Exception {
        SaveSolutionDTO request = new SaveSolutionDTO(
                solution.getCode(),
                solution.getTask().getId(),
                solution.getAnswer().getId(),
                null
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewSolutionDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewSolutionDTO.class
        );

        assertEquals(response.code(), request.code());
        assertEquals(response.taskId(), request.taskId());
        assertEquals(response.isCompleted(), true);
    }

    @Test
    @MockUser(role = ERole.RESPONDENT)
    public void tryCode() throws Exception {
        TryCodeRequest request = new TryCodeRequest(
                solution.getCode(),
                solution.getTask().getId()
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/try_code")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        TryCodeResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                TryCodeResponse.class
        );

        assertTrue(response.isCompleted());
        assertEquals(response.evaluationTime(), 1L);
        assertEquals(response.error(), "");
    }

    @Test
    @MockUser(role = ERole.RESPONDENT)
    public void deleteAnswer() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + solution.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
