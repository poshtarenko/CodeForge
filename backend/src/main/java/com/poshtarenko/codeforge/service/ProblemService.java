package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.SaveProblemDTO;
import com.poshtarenko.codeforge.dto.request.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.test.Problem;

import java.util.List;

public interface ProblemService {

    ViewProblemDTO find(long id);

    List<ViewProblemDTO> findAll();

    Problem findByTask(long taskId);

    ViewProblemDTO save(SaveProblemDTO problemDTO);

    ViewProblemDTO update(Long problemId, UpdateProblemDTO problemDTO);

    void delete(long id);
}
