package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.FinishTestRequest;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface ResultMapper {

    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

    @Mapping(source = "test", target = "testId")
    ViewResultDTO toDto(Result entity);

    ViewResultDTO.RespondentDTO toDto(Respondent respondent);

}
