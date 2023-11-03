package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.response.ViewSolutionResultDTO;
import com.poshtarenko.codeforge.entity.test.SolutionResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface EvaluationResultMapper {

    EvaluationResultMapper INSTANCE = Mappers.getMapper(EvaluationResultMapper.class);

    ViewSolutionResultDTO toDto(SolutionResult entity);


}
