package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;

import java.util.List;

public interface TestService {

    ViewTestDTO findAsAuthor(long id);

    ViewTestDTO findAsRespondent(long testId);

    List<ViewTestDTO> findAuthorTests(long authorId);

    ViewTestDTO save(SaveTestDTO testDTO);

    ViewTestDTO update(UpdateTestDTO testDTO);

    void delete(long id);

    void checkRespondentConnectedToTest(long respondentId, long testId);

    void checkAccess(long testId, long respondentId);

}
