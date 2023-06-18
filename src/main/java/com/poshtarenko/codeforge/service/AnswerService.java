package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;

import java.util.List;
import java.util.Optional;

public interface AnswerService {

    ViewAnswerDTO startAnswer(long respondentId, String testCode);

    ViewAnswerDTO finishAnswer(long answerId);

    ViewAnswerDTO find(long id);

    List<ViewAnswerDTO> findByTest(long testId);

    Optional<ViewAnswerDTO> findRespondentCurrentAnswer(long respondentId, long testId);

    void delete(long id);

    void checkAccess(long resultId, long respondentId);

}