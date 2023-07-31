package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/problems")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/all")
    public List<ViewProblemDTO> findAll() {
        return problemService.findAll();
    }


}
