package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;

public interface CodeEvaluationProvider {
    CodeEvaluationResult evaluateCode(CodeEvaluationRequest request);
}
