package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.dto.response.ViewLanguageDTO;
import com.poshtarenko.codeforge.entity.Language;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EntityIdMapper.class})
public interface LanguageMapper {

    LanguageMapper INSTANCE = Mappers.getMapper(LanguageMapper.class);

    ViewLanguageDTO toDto(Language entity);


}
