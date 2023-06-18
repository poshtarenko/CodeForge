package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.Solution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface SolutionMapper {

    SolutionMapper INSTANCE = Mappers.getMapper(SolutionMapper.class);

    @Mapping(source = "task", target = "taskId")
    ViewSolutionDTO toDto(Solution entity);

    @Mapping(source = "taskId", target = "task")
    Solution toEntity(SaveSolutionDTO dto);

}
