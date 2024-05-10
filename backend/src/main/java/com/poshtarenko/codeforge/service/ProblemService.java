package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.CreateProblemDTO;
import com.poshtarenko.codeforge.dto.request.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.test.Problem;

import java.util.List;

public interface ProblemService {

    ViewProblemDTO find(long id);

    List<ViewProblemDTO> findAllAvailableToAuthor(Long authorId);

    List<ViewProblemDTO> findAuthorCustomProblems(Long authorId);

    ViewProblemDTO findAuthorCustomProblem(long id, Long authorId);

    ViewProblemDTO createCustomProblem(CreateProblemDTO problemDTO, Long authorId);

    ViewProblemDTO update(Long problemId, UpdateProblemDTO problemDTO);

    void delete(long id);
}
