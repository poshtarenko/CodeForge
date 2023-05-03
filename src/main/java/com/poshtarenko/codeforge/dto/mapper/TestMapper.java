package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.SaveTestDTO;
import com.poshtarenko.codeforge.dto.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.ViewTestDTO;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(source = "author", target = "authorId")
    ViewTestDTO toDto(Test entity);

    ViewTestDTO.TaskDTO toDto(Task task);

    @Mapping(source = "language.name", target = "language")
    @Mapping(source = "category.name", target = "category")
    ViewTestDTO.ProblemDTO toDto(Problem problem);

    @Mapping(source = "authorId", target = "author")
    Test toEntity(SaveTestDTO dto);

    @Mapping(source = "authorId", target = "author")
    Test toEntity(UpdateTestDTO dto);

}
