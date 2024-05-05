package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.CreateProblemDTO;
import com.poshtarenko.codeforge.dto.request.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.test.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class, CategoryMapper.class, LanguageMapper.class})
public interface ProblemMapper {

    ProblemMapper INSTANCE = Mappers.getMapper(ProblemMapper.class);

    ViewProblemDTO toDto(Problem entity);

    @Mapping(source = "languageId", target = "language")
    Problem toEntity(CreateProblemDTO dto);

    @Mapping(source = "languageId", target = "language")
    Problem toEntity(UpdateProblemDTO dto);
}