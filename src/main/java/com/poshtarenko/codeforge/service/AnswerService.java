package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;

import java.util.List;

public interface AnswerService {

    ViewAnswerDTO find(long id);

    List<ViewAnswerDTO> findAnswersOnTest(long respondentId, long testId);

    ViewAnswerDTO put(SaveAnswerDTO answerDTO);

    CodeEvaluationResult tryCode(TryCodeRequest tryCodeRequest);

    void delete(long id);

    void checkAccess(long answerId, long respondentId);
}
