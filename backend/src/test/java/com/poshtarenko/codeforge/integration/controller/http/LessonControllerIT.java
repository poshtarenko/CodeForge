package com.poshtarenko.codeforge.integration.controller.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.request.SaveLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
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
public class LessonControllerIT extends IntegrationTest {

    private static final String BASE_URL = "/lessons";

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    private final TestDataInitializer dataInitializer;

    Lesson lesson;

    @BeforeEach
    public void setup() {
        dataInitializer.setupData();
        lesson = dataInitializer.getLesson();
    }

    @AfterEach
    public void shutdown() {
        dataInitializer.clearData();
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void findTestAsAuthor() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/as_author/" + lesson.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewLessonDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewLessonDTO.class
        );

        assertLessonsEquals(response);
    }

    @Test
    @MockUser(role = ERole.RESPONDENT)
    public void findLessonAsRespondent() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/as_respondent/" + lesson.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewLessonDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewLessonDTO.class
        );

        assertLessonsEquals(response);
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void findMyLessons() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/as_author/my"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<ViewLessonDTO> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );

        assertEquals(response.size(), 1);
        assertLessonsEquals(response.get(0));
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void createLesson() throws Exception {
        SaveLessonDTO request = new SaveLessonDTO(
                "New test",
                lesson.getAuthor().getId()
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewLessonDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewLessonDTO.class
        );

        assertEquals(response.name(), request.name());
        assertEquals(response.authorId(), request.authorId());
        assertNotNull(response.inviteCode());
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void updateLesson() throws Exception {
        UpdateLessonDTO request = new UpdateLessonDTO(
                lesson.getId(),
                "Updated test name",
                lesson.getLanguage().getId()
        );

        MvcResult result = mvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/" + lesson.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ViewLessonDTO response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ViewLessonDTO.class
        );

        assertEquals(response.id(), request.id());
        assertEquals(response.name(), request.name());
        assertEquals(response.language().id(), request.languageId());
        assertNotNull(response.inviteCode());
    }

    @Test
    @MockUser(role = ERole.AUTHOR)
    public void deleteLesson() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/" + lesson.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private void assertLessonsEquals(ViewLessonDTO response) {
        assertEquals(lesson.getId(), response.id());
        assertEquals(lesson.getAuthor().getId(), response.authorId());
        assertEquals(lesson.getInviteCode(), response.inviteCode());
        assertEquals(lesson.getDescription(), response.description());
    }

}
