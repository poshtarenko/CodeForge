package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.ViewCategoryDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    ViewCategoryDTO find(long id);

    List<ViewCategoryDTO> findAll();

}
