package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.SaveLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;

import java.util.List;

public interface LessonService {

    List<ViewLessonDTO> findAuthorLessons(Long authorId);

    ViewLessonDTO findAsAuthor(Long id);

    ViewLessonDTO findAsRespondent(Long id);

    ViewLessonDTO save(SaveLessonDTO lessonDTO);

    ViewLessonDTO update(UpdateLessonDTO lessonDTO);


    ViewLessonDTO updateCurrentDescription(UpdateLessonDescriptionDTO request);

    void delete(Long id);
}
