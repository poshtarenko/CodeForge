package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.SaveResultDTO;
import com.poshtarenko.codeforge.dto.request.UpdateResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;

public interface ResultService {

    ViewResultDTO find(long id);

    ViewResultDTO save(SaveResultDTO resultDTO);

    void delete(long id);

    ViewResultDTO update(UpdateResultDTO resultDTO);

    void checkAccess(long resultId, long respondentId);

}
