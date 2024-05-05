package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.CreateTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;

public interface TaskService {

    ViewTaskDTO find(long id);

    ViewTaskDTO save(CreateTaskDTO taskDTO);

    ViewTaskDTO update(Long taskId, UpdateTaskDTO taskDTO);

    void delete(long id);

    void checkAccess(long taskId, long respondentId);
}
