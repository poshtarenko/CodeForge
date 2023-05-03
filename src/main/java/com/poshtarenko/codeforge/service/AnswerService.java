package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewAnswerDTO;

import java.util.List;

public interface AnswerService {

    ViewAnswerDTO find(long id);

    List<ViewAnswerDTO> findRespondentAnswersOnTest(long respondentId, long testId);

    ViewAnswerDTO save(SaveAnswerDTO answerDTO);

    ViewAnswerDTO update(UpdateAnswerDTO answerDTO);

    void delete(long id);

    void checkAccess(long answerId, long respondentId);
}
