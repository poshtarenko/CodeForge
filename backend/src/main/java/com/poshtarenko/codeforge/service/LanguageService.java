package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.response.ViewLanguageDTO;

import java.util.List;

public interface LanguageService {

    ViewLanguageDTO find(long id);

    List<ViewLanguageDTO> findAll();

}
