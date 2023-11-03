package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.LessonMapper;
import com.poshtarenko.codeforge.dto.request.SaveLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.entity.code.Language;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.LessonRepository;
import com.poshtarenko.codeforge.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('AUTHOR')")
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    private final static int LESSON_INVITE_CODE_LENGTH = 9;

    @Override
    public List<ViewLessonDTO> findAuthorLessons(Long authorId) {
        return lessonRepository.findByAuthorId(authorId).stream()
                .map(lessonMapper::toDto)
                .toList();
    }

    @Override
    public ViewLessonDTO findAsAuthor(Long id) {
        Lesson lesson = findById(id);
        return lessonMapper.toDto(lesson);
    }

    @Override
    @PreAuthorize("hasAuthority('RESPONDENT')")
    public ViewLessonDTO findAsRespondent(Long id) {
        Lesson lesson = findById(id);
        return lessonMapper.toDto(lesson);
    }

    private Lesson findById(long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        Lesson.class, "Lesson with id " + id + " not found")
                );
    }

    @Override
    @Transactional
    public ViewLessonDTO save(SaveLessonDTO lessonDTO) {
        String code;
        do {
            code = RandomStringUtils.randomAlphabetic(LESSON_INVITE_CODE_LENGTH);
        } while (lessonRepository.existsLessonByInviteCode(code));

        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson.setInviteCode(code);

        Lesson saved = lessonRepository.save(lesson);
        return lessonMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ViewLessonDTO update(UpdateLessonDTO lessonDTO) {
        Lesson lesson = findById(lessonDTO.id());
        lesson.setName(lessonDTO.name());
        if (lessonDTO.languageId() != null) {
            lesson.setLanguage(new Language(lessonDTO.languageId()));
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public ViewLessonDTO updateCurrentDescription(UpdateLessonDescriptionDTO request) {
        Lesson lesson = findById(request.id());
        lesson.setDescription(request.description());

        Lesson savedLesson = lessonRepository.save(lesson);
        return lessonMapper.toDto(savedLesson);
    }

    @Override
    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }
}
