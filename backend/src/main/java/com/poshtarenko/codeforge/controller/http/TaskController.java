package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.request.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.response.ViewTaskDTO;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.TaskService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ViewTaskDTO findTask(@PathVariable @Positive long id,
                                @AuthenticationPrincipal UserDetailsImpl currentUser) {
        taskService.checkAccess(id, currentUser.getId());
        return taskService.find(id);
    }

    @PostMapping
    public ViewTaskDTO createTask(@RequestBody @Validated SaveTaskDTO taskDTO) {
        SaveTaskDTO saveTaskDTO = new SaveTaskDTO(
                taskDTO.note(),
                taskDTO.maxScore(),
                taskDTO.problemId(),
                taskDTO.testId()
        );

        return taskService.save(saveTaskDTO);
    }

    @PutMapping("/{id}")
    public ViewTaskDTO updateTask(@PathVariable @Positive long id,
                                  @RequestBody @Validated UpdateTaskDTO taskDTO,
                                  @AuthenticationPrincipal UserDetailsImpl currentUser) {
        taskService.checkAccess(id, currentUser.getId());
        return taskService.update(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable @Positive long id,
                                        @AuthenticationPrincipal UserDetailsImpl currentUser) {
        taskService.checkAccess(id, currentUser.getId());

        taskService.delete(id);
        return ResponseEntity.ok().build();
    }

}
