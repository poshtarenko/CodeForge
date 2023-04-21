package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.SaveTestDTO;
import com.poshtarenko.codeforge.dto.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.ViewTaskDTO;
import com.poshtarenko.codeforge.dto.ViewTestDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.TaskService;
import com.poshtarenko.codeforge.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ViewTaskDTO findTask(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long userId = SecurityUtils.getUserId();
        taskService.checkAccess(id, userId);

        return taskService.find(id);
    }

    @PostMapping
    public ViewTaskDTO createTask(@RequestBody SaveTaskDTO taskDTO) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);

        SaveTaskDTO saveTaskDTO = new SaveTaskDTO(
                taskDTO.note(),
                taskDTO.maxScore(),
                taskDTO.problemId(),
                taskDTO.testId()
        );

        return taskService.save(saveTaskDTO);
    }

    @PutMapping("/{id}")
    public ViewTaskDTO updateTask(@PathVariable long id, @RequestBody UpdateTaskDTO taskDTO) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long userId = SecurityUtils.getUserId();
        taskService.checkAccess(id, userId);

        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(
                id,
                taskDTO.note(),
                taskDTO.maxScore(),
                taskDTO.problemId(),
                taskDTO.testId()
        );

        return taskService.update(updateTaskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long userId = SecurityUtils.getUserId();
        taskService.checkAccess(id, userId);

        taskService.delete(id);
        return ResponseEntity.ok().build();
    }

}
