package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.LanguageMapper;
import com.poshtarenko.codeforge.dto.response.ViewLanguageDTO;
import com.poshtarenko.codeforge.entity.Language;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.LanguageRepository;
import com.poshtarenko.codeforge.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper categoryRepository;

    @Override
    public ViewLanguageDTO find(long id) {
        return languageRepository.findById(id)
                .map(categoryRepository::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Language.class, "Language with id " + id + " not found")
                );
    }

    @Override
    public List<ViewLanguageDTO> findAll() {
        return languageRepository.findAll().stream()
                .map(categoryRepository::toDto)
                .collect(Collectors.toList());
    }

}
