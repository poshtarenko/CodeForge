package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.SaveProblemDTO;
import com.poshtarenko.codeforge.dto.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.ViewProblemDTO;

import java.util.Optional;

public interface ProblemService {

    ViewProblemDTO find(long id);

    ViewProblemDTO findByTask(long taskId);

    ViewProblemDTO save(SaveProblemDTO problemDTO);

    ViewProblemDTO update(UpdateProblemDTO problemDTO);

    void delete(long id);
}
