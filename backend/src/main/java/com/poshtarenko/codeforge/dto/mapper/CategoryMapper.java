package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.response.ViewCategoryDTO;
import com.poshtarenko.codeforge.entity.test.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    ViewCategoryDTO toDto(Category entity);


}
