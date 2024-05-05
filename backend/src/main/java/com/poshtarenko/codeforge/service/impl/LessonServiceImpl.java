package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.LessonMapper;
import com.poshtarenko.codeforge.dto.request.CreateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.entity.code.Language;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.LessonRepository;
import com.poshtarenko.codeforge.service.LessonService;
import com.poshtarenko.codeforge.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final ParticipationService participationService;
    private final AuthorRepository authorRepository;
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
    @PreAuthorize("hasAnyAuthority('AUTHOR', 'RESPONDENT')")
    public ViewLessonDTO find(Long id) {
        return lessonMapper.toDto(findById(id));
    }

    private Lesson findById(long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Lesson.class, id));
    }

    @Override
    @Transactional
    public ViewLessonDTO save(Long userId, CreateLessonDTO lessonDTO) {
        String code;
        do {
            code = RandomStringUtils.randomAlphabetic(LESSON_INVITE_CODE_LENGTH);
        } while (lessonRepository.existsLessonByInviteCode(code));
        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson.setAuthor(authorRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(Author.class, userId)));
        lesson.setInviteCode(code);
        lesson = lessonRepository.save(lesson);
        participationService.startParticipation(lesson.getId(), userId);
        return lessonMapper.toDto(lesson);
    }

    @Override
    @Transactional
    public ViewLessonDTO connectToLesson(String lessonCode, Long userId) {
        Lesson lesson = lessonRepository.findByInviteCode(lessonCode).orElseThrow(() ->
                new EntityNotFoundException(Lesson.class, "Lesson with code %s not exists".formatted(lessonCode)));
        participationService.startParticipation(lesson.getId(), userId);
        return lessonMapper.toDto(lesson);
    }

    @Override
    @Transactional
    public ViewLessonDTO update(Long lessonId, UpdateLessonDTO lessonDTO) {
        Lesson lesson = findById(lessonId);
        lesson.setName(lessonDTO.name());
        if (lessonDTO.languageId() != null) {
            lesson.setLanguage(new Language(lessonDTO.languageId()));
        }
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public ViewLessonDTO updateCurrentDescription(Long lessonId, UpdateLessonDescriptionDTO request) {
        Lesson lesson = findById(lessonId);
        lesson.setDescription(request.description());
        return lessonMapper.toDto(lessonRepository.save(lesson));
    }

    @Override
    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }
}
