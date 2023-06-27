package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.request.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ViewTaskDTO findTask(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long authorId = SecurityUtils.getUserId();
        taskService.checkAccess(id, authorId);
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
        long authorId = SecurityUtils.getUserId();
        taskService.checkAccess(id, authorId);

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
        long authorId = SecurityUtils.getUserId();
        taskService.checkAccess(id, authorId);

        taskService.delete(id);
        return ResponseEntity.ok().build();
    }

}
