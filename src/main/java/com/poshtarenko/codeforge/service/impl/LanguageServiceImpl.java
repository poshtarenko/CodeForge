package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.ViewLanguageDTO;
import com.poshtarenko.codeforge.dto.mapper.LanguageMapper;
import com.poshtarenko.codeforge.entity.Language;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.LanguageRepository;
import com.poshtarenko.codeforge.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LanguageServiceImpl implements LanguageService {

    LanguageRepository languageRepository;
    LanguageMapper categoryRepository;

    @Autowired
    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageMapper categoryRepository) {
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ViewLanguageDTO find(long id) {
        return languageRepository.findById(id)
                .map(categoryRepository::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Language.class, "" +
                        "Language with id " + id + " not found")
                );
    }

    @Override
    public List<ViewLanguageDTO> findAll() {
        return languageRepository.findAll().stream()
                .map(categoryRepository::toDto)
                .collect(Collectors.toList());
    }


}
