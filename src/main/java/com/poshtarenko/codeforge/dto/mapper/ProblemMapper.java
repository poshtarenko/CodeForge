package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.SaveProblemDTO;
import com.poshtarenko.codeforge.dto.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.Problem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface ProblemMapper {

    ProblemMapper INSTANCE = Mappers.getMapper(ProblemMapper.class);

    @Mapping(source = "language", target = "languageId")
    @Mapping(source = "category", target = "categoryId")
    ViewProblemDTO toDto(Problem entity);

    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "categoryId", target = "category")
    Problem toEntity(SaveProblemDTO dto);

    @Mapping(source = "languageId", target = "language")
    @Mapping(source = "categoryId", target = "category")
    Problem toEntity(UpdateProblemDTO dto);

}
