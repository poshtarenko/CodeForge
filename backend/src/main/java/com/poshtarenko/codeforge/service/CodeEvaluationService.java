package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;

public interface CodeEvaluationService {
    EvaluationResult evaluateCode(CodeEvaluationRequest request);

}
