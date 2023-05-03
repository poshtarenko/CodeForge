package com.poshtarenko.codeforge.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.integration.data.TestDataInitializer;
import com.poshtarenko.codeforge.integration.security.WithMockCustomUser;
import com.poshtarenko.codeforge.pojo.CodeEvaluationResult;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
@WithMockCustomUser(role = ERole.RESPONDENT)
public class AnswerControllerTestIT {

    private static final String BASE_URL = "/answer";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestDataInitializer dataInitializer;

    @MockBean
    CodeEvaluationProvider codeEvaluationProvider;

    Answer answer;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        answer = dataInitializer.getAnswer();
        when(codeEvaluationProvider.evaluateCode(any())).thenReturn(new CodeEvaluationResult(true, 1L));
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @SneakyThrows
    public void createAnswer() {
        SaveAnswerDTO request = new SaveAnswerDTO(
                answer.getCode(),
                answer.getTask().getId(),
                null
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertEquals(response.code(), request.code());
        assertEquals(response.taskId(), request.taskId());
        assertEquals(response.isCompleted(), true);
    }

    @Test
    @SneakyThrows
    public void viewAnswer() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + answer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertEquals(answer.getId(), response.id());
        assertEquals(answer.getCode(), response.code());
        assertEquals(answer.getRespondent().getId(), response.respondentId());
        assertEquals(answer.getTask().getId(), response.taskId());
    }

    @Test
    @SneakyThrows
    public void updateAnswer() {
        UpdateAnswerDTO request = new UpdateAnswerDTO(
                answer.getId(),
                answer.getCode() + "updated"
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + answer.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewAnswerDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewAnswerDTO.class
        );

        assertEquals(response.id(), request.id());
        assertEquals(response.code(), request.code());
    }

    @Test
    @SneakyThrows
    public void deleteAnswer() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + answer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
