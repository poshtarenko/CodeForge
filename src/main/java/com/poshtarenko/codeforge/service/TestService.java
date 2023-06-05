package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;

import java.util.List;

public interface TestService {

    ViewTestDTO find(long id);

    ViewTestDTO findByInviteCode(String inviteCode);

    List<ViewTestDTO> findByAuthor(long authorId);

    ViewTestDTO save(SaveTestDTO testDTO);

    ViewTestDTO update(UpdateTestDTO testDTO);

    void delete(long id);

    void checkAccess(long testId, long respondentId);
}
