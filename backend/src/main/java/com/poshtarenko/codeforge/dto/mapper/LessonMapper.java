package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.CreateLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class, LanguageMapper.class, ParticipationMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LessonMapper {

    @Mapping(source = "author", target = "authorId")
    ViewLessonDTO toDto(Lesson entity);

    Lesson toEntity(CreateLessonDTO dto);

    @Mapping(source = "languageId", target = "language")
    Lesson toEntity(UpdateLessonDTO dto);

}
