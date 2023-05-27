package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.SaveResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;
import com.poshtarenko.codeforge.entity.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface ResultMapper {

    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

    @Mapping(source = "test", target = "testId")
    @Mapping(source = "respondent", target = "respondentId")
    ViewResultDTO toDto(Result entity);

    @Mapping(source = "testId", target = "test")
    @Mapping(source = "respondentId", target = "respondent")
    Result toEntity(SaveResultDTO dto);

}
