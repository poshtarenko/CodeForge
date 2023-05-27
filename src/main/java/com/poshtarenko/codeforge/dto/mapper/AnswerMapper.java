package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.request.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(source = "task", target = "taskId")
    @Mapping(source = "respondent", target = "respondentId")
    ViewAnswerDTO toDto(Answer entity);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "respondentId", target = "respondent")
    Answer toEntity(SaveAnswerDTO dto);

    Answer toEntity(UpdateAnswerDTO dto);

}
