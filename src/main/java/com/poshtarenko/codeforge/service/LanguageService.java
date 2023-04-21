package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.ViewLanguageDTO;

import java.util.List;
import java.util.Optional;

public interface LanguageService {

    ViewLanguageDTO find(long id);

    List<ViewLanguageDTO> findAll();

}
