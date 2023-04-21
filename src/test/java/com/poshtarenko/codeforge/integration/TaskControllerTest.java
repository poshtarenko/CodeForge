package com.poshtarenko.codeforge.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.Task;
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
@WithMockCustomUser(roles = "AUTHOR")
public class TaskControllerTest {

    private static final String BASE_URL = "/task";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestDataInitializer dataInitializer;

    Task task;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        task = dataInitializer.getTask();
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @SneakyThrows
    public void createTest() {
        SaveTaskDTO request = new SaveTaskDTO(
                "New note...",
                3,
                task.getProblem().getId(),
                task.getTest().getId()
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTaskDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTaskDTO.class
        );

        assertEquals(response.note(), request.note());
        assertEquals(response.maxScore(), request.maxScore());
        assertEquals(response.problemId(), request.problemId());
        assertEquals(response.testId(), request.testId());
    }

    @Test
    @SneakyThrows
    public void viewTest() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + task.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTaskDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTaskDTO.class
        );

        assertEquals(task.getId(), response.id());
        assertEquals(task.getMaxScore(), response.maxScore());
        assertEquals(task.getProblem().getId(), response.problemId());
        assertEquals(task.getTest().getId(), response.testId());
    }

    @Test
    @SneakyThrows
    public void updateTest() {
        UpdateTaskDTO request = new UpdateTaskDTO(
                task.getId(),
                task.getNote() + "updated",
                task.getMaxScore() + 1,
                task.getProblem().getId(),
                task.getTest().getId()
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + task.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTaskDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTaskDTO.class
        );

        assertEquals(response.id(), request.id());
        assertEquals(response.note(), request.note());
        assertEquals(response.maxScore(), request.maxScore());
        assertEquals(response.problemId(), request.problemId());
        assertEquals(response.testId(), request.testId());
    }

    @Test
    @SneakyThrows
    public void deleteTest() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + task.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
