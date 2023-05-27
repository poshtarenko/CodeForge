package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;

public interface CodeEvaluationProvider {
    CodeEvaluationResult evaluateCode(CodeEvaluationRequest request);
}
