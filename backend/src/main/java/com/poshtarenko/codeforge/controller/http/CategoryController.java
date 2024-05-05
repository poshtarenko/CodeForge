package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.response.ViewCategoryDTO;
import com.poshtarenko.codeforge.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<ViewCategoryDTO> findAll() {
        return categoryService.findAll();
    }
}
