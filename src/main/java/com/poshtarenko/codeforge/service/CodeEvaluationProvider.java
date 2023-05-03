package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.pojo.CodeEvaluationRequest;
import com.poshtarenko.codeforge.pojo.CodeEvaluationResult;

public interface CodeEvaluationProvider {
    CodeEvaluationResult evaluateCode(CodeEvaluationRequest request);
}
