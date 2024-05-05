package com.poshtarenko.codeforge.integration.controller.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.CreateTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.entity.user.ERole;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MockUser(role = ERole.AUTHOR)
@RequiredArgsConstructor
@HttpTest
public class TestControllerIT extends IntegrationTest {

    private static final String BASE_URL = "/tests";

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final TestDataInitializer dataInitializer;

    com.poshtarenko.codeforge.entity.test.Test test;

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
    @MockUser(role = ERole.AUTHOR)
    public void findTestAsAuthor() throws Exception {
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
    @MockUser(role = ERole.RESPONDENT)
    public void findTestAsRespondent() throws Exception {
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
    @MockUser(role = ERole.AUTHOR)
    public void findMyTests() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/as_author/my"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<ViewTestDTO> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals(response.size(), 1);
        assertTestsEquals(response.get(0));
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void createTest() throws Exception {
        CreateTestDTO request = new CreateTestDTO(
                "New test",
                100
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
//        assertEquals(response.authorId(), request.authorId());
        assertNotNull(response.inviteCode());
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void updateTest() throws Exception {
        UpdateTestDTO request = new UpdateTestDTO(
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

//        assertEquals(response.id(), request.id());
        assertEquals(response.name(), request.name());
        assertEquals(response.maxDuration(), request.maxDuration());
        assertNotNull(response.inviteCode());
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void deleteTest() throws Exception {
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
