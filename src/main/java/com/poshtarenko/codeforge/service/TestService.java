package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.SaveTestDTO;
import com.poshtarenko.codeforge.dto.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.ViewTestDTO;

import java.util.List;

public interface TestService {

    ViewTestDTO find(long id);

    List<ViewTestDTO> findByAuthor(long authorId);

    ViewTestDTO save(SaveTestDTO testDTO);

    ViewTestDTO update(UpdateTestDTO testDTO);

    void delete(long id);

    void checkAccess(long testId, long respondentId);
}
