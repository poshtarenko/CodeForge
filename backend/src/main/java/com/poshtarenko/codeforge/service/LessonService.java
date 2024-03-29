package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.SaveLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;

import java.util.List;

public interface LessonService {

    List<ViewLessonDTO> findAuthorLessons(Long authorId);

    ViewLessonDTO find(Long id);

    ViewLessonDTO save(Long userId, SaveLessonDTO lessonDTO);

    ViewLessonDTO update(Long lessonId, UpdateLessonDTO lessonDTO);


    ViewLessonDTO updateCurrentDescription(Long lessonId, UpdateLessonDescriptionDTO request);

    void delete(Long id);
}
