package com.poshtarenko.codeforge.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.entity.ERole;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ActiveProfiles("test")
@WithMockCustomUser(role = ERole.AUTHOR)
public class TestControllerTestIT {

    private static final String BASE_URL = "/test";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestDataInitializer dataInitializer;

    com.poshtarenko.codeforge.entity.Test test;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        test = dataInitializer.getTest();
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @SneakyThrows
    public void createTest() {
        SaveTestDTO request = new SaveTestDTO(
                "New test",
                100,
                test.getAuthor().getId()
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTestDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTestDTO.class
        );

        assertEquals(response.name(), request.name());
        assertEquals(response.maxDuration(), request.maxDuration());
        assertEquals(response.authorId(), request.authorId());
    }

    @Test
    @SneakyThrows
    public void viewTest() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTestDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTestDTO.class
        );

        assertEquals(test.getId(), response.id());
        assertEquals(test.getMaxDuration(), response.maxDuration());
        assertEquals(test.getAuthor().getId(), response.authorId());
    }

    @Test
    @SneakyThrows
    public void getMyTests() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/my"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<ViewTestDTO> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(response.size(), 1);
    }

    @Test
    @SneakyThrows
    public void updateTest() {
        UpdateTestDTO request = new UpdateTestDTO(
                test.getId(),
                "Updated test name",
                999
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + test.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTestDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTestDTO.class
        );

        assertEquals(response.id(), request.id());
        assertEquals(response.name(), request.name());
        assertEquals(response.maxDuration(), request.maxDuration());
    }

    @Test
    @SneakyThrows
    public void deleteTest() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }


}
