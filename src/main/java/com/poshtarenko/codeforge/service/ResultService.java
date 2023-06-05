package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.FinishTestRequest;
import com.poshtarenko.codeforge.dto.request.UpdateResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;

import java.util.List;
import java.util.Optional;

public interface ResultService {

    ViewResultDTO find(long id);

    Optional<ViewResultDTO> findRespondentTestResult(long respondentId, long testId);

    List<ViewResultDTO> findTestResults(long testId);

    ViewResultDTO save(FinishTestRequest resultDTO);

    void delete(long id);

    ViewResultDTO update(UpdateResultDTO resultDTO);

    void checkAccess(long resultId, long respondentId);

}