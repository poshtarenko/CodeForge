package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.test.Solution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class, EvaluationResultMapper.class})
public interface SolutionMapper {

    SolutionMapper INSTANCE = Mappers.getMapper(SolutionMapper.class);

    @Mapping(source = "task", target = "taskId")
    @Mapping(source = "answer", target = "answerId")
    @Mapping(source = "solutionResult", target = "evaluationResult")
    ViewSolutionDTO toDto(Solution entity);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "answerId", target = "answer")
    Solution toEntity(SaveSolutionDTO dto);

}
