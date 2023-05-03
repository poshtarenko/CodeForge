package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.ViewTaskDTO;
import com.poshtarenko.codeforge.dto.mapper.TaskMapper;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.TaskRepository;
import com.poshtarenko.codeforge.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public ViewTaskDTO find(long id) {
        return taskRepository.findById(id)
                .map(taskMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Task.class, "Task with id " + id + " not found")
                );
    }

    @Override
    public List<ViewTaskDTO> findByTest(long testId) {
        return taskRepository.findByTestId(testId).stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public ViewTaskDTO save(SaveTaskDTO taskDTO) {
        Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
        return taskMapper.toDto(task);
    }

    @Override
    public ViewTaskDTO update(UpdateTaskDTO taskDTO) {
        taskRepository.findById(taskDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(taskDTO.id())));

        Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
        return taskMapper.toDto(task);
    }

    @Override
    public void delete(long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long taskId, long authorId) {
        if (taskRepository.findById(taskId).isEmpty()) {
            throw new EntityNotFoundException(Task.class, "Task with id %d not found".formatted(taskId));
        }
        if (!taskRepository.checkAccess(taskId, authorId)) {
            throw new EntityAccessDeniedException(Task.class, taskId, authorId);
        }
    }
}
