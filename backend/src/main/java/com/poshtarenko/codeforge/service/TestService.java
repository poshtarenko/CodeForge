package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;

import java.util.List;

public interface TestService {

    ViewTestDTO find(long id);

    List<ViewTestDTO> findAuthorTests(long authorId);

    ViewTestDTO save(Long userId, SaveTestDTO testDTO);

    ViewTestDTO update(Long testId, UpdateTestDTO testDTO);

    void delete(long id);

    void checkRespondentConnectedToTest(long respondentId, long testId);

    void checkAccess(long testId, long respondentId);

}
