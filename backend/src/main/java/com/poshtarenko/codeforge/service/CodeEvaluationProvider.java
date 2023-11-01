package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.test.SolutionResult;

public interface CodeEvaluationProvider {
    EvaluationResult evaluateCode(CodeEvaluationRequest request);

}
