package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.TaskMapper;
import com.poshtarenko.codeforge.dto.request.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.test.Task;
import com.poshtarenko.codeforge.entity.test.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.TaskRepository;
import com.poshtarenko.codeforge.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
    @Transactional(readOnly = true)
    @RequiredArgsConstructor
    @PreAuthorize("hasAuthority('AUTHOR')")
    public class TaskServiceImpl implements TaskService {

        private final TaskRepository taskRepository;
        private final TaskMapper taskMapper;

        @Override
        public ViewTaskDTO find(long id) {
            return taskMapper.toDto(findById(id));
        }

        @Override
        @Transactional
        public ViewTaskDTO save(SaveTaskDTO taskDTO) {
            Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
            return taskMapper.toDto(task);
        }

        @Override
        @Transactional
        public ViewTaskDTO update(Long taskId, UpdateTaskDTO taskDTO) {
            taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
            Task task = taskRepository.save(taskMapper.toEntity(taskDTO));
            return taskMapper.toDto(task);
        }

        @Override
        @Transactional
        public void delete(long id) {
            taskRepository.deleteById(id);
        }

        @Override
        public void checkAccess(long taskId, long authorId) {
            if (taskRepository.existsById(taskId)) {
                throw new EntityNotFoundException(Task.class, taskId);
            }
            if (!taskRepository.existsByIdAndTestAuthorId(taskId, authorId)) {
                throw new EntityAccessDeniedException(Task.class, taskId, authorId);
            }
        }

        private Task findById(long taskId) {
            return taskRepository.findById(taskId)
                    .orElseThrow(() -> new EntityNotFoundException(Task.class, taskId));
        }
}
