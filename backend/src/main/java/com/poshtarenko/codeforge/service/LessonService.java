package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.CreateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;

import java.util.List;

public interface LessonService {

    List<ViewLessonDTO> findAuthorLessons(Long authorId);

    ViewLessonDTO find(Long id);

    ViewLessonDTO save(Long userId, CreateLessonDTO lessonDTO);

    ViewLessonDTO connectToLesson(String lessonCode, Long userId);

    ViewLessonDTO update(Long lessonId, UpdateLessonDTO lessonDTO);


    ViewLessonDTO updateCurrentDescription(Long lessonId, UpdateLessonDescriptionDTO request);

    void delete(Long id);
}
