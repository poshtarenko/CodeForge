package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.ViewTaskDTO;

import java.util.Optional;

public interface TaskService {

    ViewTaskDTO find(long id);

    ViewTaskDTO save(SaveTaskDTO taskDTO);

    ViewTaskDTO update(UpdateTaskDTO taskDTO);

    void delete(long id);


    void checkAccess(long taskId, long respondentId);
}
