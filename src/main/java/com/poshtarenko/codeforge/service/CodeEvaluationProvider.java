package com.poshtarenko.codeforge.service;

import com.poshtarenko.codeforge.pojo.CodeEvaluationRequest;
import com.poshtarenko.codeforge.pojo.CodeEvaluationResult;

public interface CodeEvaluationProvider {
    public CodeEvaluationResult evaluateCode(CodeEvaluationRequest request);
}
