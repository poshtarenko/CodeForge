package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.CreateTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.entity.test.Problem;
import com.poshtarenko.codeforge.entity.test.Task;
import com.poshtarenko.codeforge.entity.test.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class, LanguageMapper.class, CategoryMapper.class})
public interface TestMapper {

    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);

    @Mapping(source = "author", target = "authorId")
    ViewTestDTO toDto(Test entity);

    ViewTestDTO.TaskDTO toDto(Task task);

    ViewTestDTO.ProblemDTO toDto(Problem problem);

    Test toEntity(CreateTestDTO dto);

    Test toEntity(UpdateTestDTO dto);

}
