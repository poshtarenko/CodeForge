package com.poshtarenko.codeforge.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MvcTest
@WithMockCustomUser(role = ERole.AUTHOR)
@RequiredArgsConstructor
public class TestControllerIT {

    private static final String BASE_URL = "/test";

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final TestDataInitializer dataInitializer;

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
    @WithMockCustomUser(role = ERole.AUTHOR)
    public void findTestAsAuthor() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/as_author/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTestDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTestDTO.class
        );

        assertTestsEquals(response);
    }

    @Test
    @SneakyThrows
    @WithMockCustomUser(role = ERole.RESPONDENT)
    public void findTestAsRespondent() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/as_respondent/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewTestDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewTestDTO.class
        );

        assertTestsEquals(response);
    }

    @Test
    @SneakyThrows
    public void findMyTests() {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/my"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<ViewTestDTO> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(response.size(), 1);
        assertTestsEquals(response.get(0));
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
        assertNotNull(response.inviteCode());
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
        assertNotNull(response.inviteCode());
    }

    @Test
    @SneakyThrows
    public void deleteTest() {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + test.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private void assertTestsEquals(ViewTestDTO response) {
        assertEquals(test.getId(), response.id());
        assertEquals(test.getMaxDuration(), response.maxDuration());
        assertEquals(test.getAuthor().getId(), response.authorId());
        assertEquals(test.getInviteCode(), response.inviteCode());
    }

}
