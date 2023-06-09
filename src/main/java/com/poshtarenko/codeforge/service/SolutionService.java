package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;

public interface SolutionService {

    ViewSolutionDTO find(long id);

    ViewSolutionDTO put(SaveSolutionDTO answerDTO);

    CodeEvaluationResult tryCode(TryCodeRequest tryCodeRequest);

    void delete(long id);

    void checkAccess(long solutionId, long respondentId);
}
