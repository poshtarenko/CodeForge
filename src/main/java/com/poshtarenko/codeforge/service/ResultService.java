package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.SaveResultDTO;
import com.poshtarenko.codeforge.dto.ViewResultDTO;

import java.util.Optional;

public interface ResultService {

    ViewResultDTO find(long id);

    ViewResultDTO save(SaveResultDTO taskDTO);

    void delete(long id);

    void checkAccess(long resultId, long respondentId);

}
