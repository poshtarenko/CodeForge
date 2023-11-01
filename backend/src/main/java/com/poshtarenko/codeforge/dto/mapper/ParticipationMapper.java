package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.SaveParticipationDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.lesson.Participation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class, LanguageMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ParticipationMapper {

    ViewParticipationDTO toDto(Participation entity);

    ViewParticipationDTO.EvaluationResultDTO toDto(EvaluationResult entity);

    Participation toEntity(SaveParticipationDTO dto);

}
