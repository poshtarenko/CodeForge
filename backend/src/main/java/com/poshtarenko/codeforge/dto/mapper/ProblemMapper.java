package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.SaveProblemDTO;
import com.poshtarenko.codeforge.dto.request.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class, CategoryMapper.class, LanguageMapper.class})
public interface ProblemMapper {

    ProblemMapper INSTANCE = Mappers.getMapper(ProblemMapper.class);

    ViewProblemDTO toDto(Problem entity);

    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "categoryId", target = "category")
    Problem toEntity(SaveProblemDTO dto);

    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "categoryId", target = "category")
    Problem toEntity(UpdateProblemDTO dto);

}
