package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface AnswerMapper {

    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    @Mapping(source = "task", target = "taskId")
    @Mapping(source = "result", target = "resultId")
    ViewAnswerDTO toDto(Answer entity);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "resultId", target = "result")
    Answer toEntity(SaveAnswerDTO dto);

    @Mapping(source = "taskId", target = "task")
    @Mapping(source = "resultId", target = "result")
    Answer toEntity(UpdateAnswerDTO dto);

}
