package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AnswerMapper {

    @Mapping(source = "test", target = "testId")
    ViewAnswerDTO toDto(Answer entity);

    ViewAnswerDTO.RespondentDTO toDto(Respondent respondent);

    ViewAnswerDTO.SolutionDTO toDto(Solution entity);

    ViewAnswerDTO.TaskDTO toDto(Task entity);

    ViewAnswerDTO.ProblemDTO toDto(Problem entity);

}
