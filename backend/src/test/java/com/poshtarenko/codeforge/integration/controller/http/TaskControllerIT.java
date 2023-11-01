package com.poshtarenko.codeforge.integration.controller.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.user.ERole;
import com.poshtarenko.codeforge.entity.test.Task;
import com.poshtarenko.codeforge.integration.IntegrationTest;
import com.poshtarenko.codeforge.integration.annotation.HttpTest;
import com.poshtarenko.codeforge.integration.data.TestDataInitializer;
import com.poshtarenko.codeforge.integration.security.MockUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
@HttpTest
public class TaskControllerIT extends IntegrationTest {

    private static final String BASE_URL = "/tasks";

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final TestDataInitializer dataInitializer;

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
    @MockUser(role = ERole.AUTHOR)
    public void findTask() throws Exception {
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
    @MockUser(role = ERole.AUTHOR)
    public void createTask() throws Exception {
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
    @MockUser(role = ERole.AUTHOR)
    public void updateTask() throws Exception {
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
    @MockUser(role = ERole.AUTHOR)
    public void deleteTask() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + task.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
