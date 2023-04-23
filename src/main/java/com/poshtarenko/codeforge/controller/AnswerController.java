package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.SaveTaskDTO;
import com.poshtarenko.codeforge.dto.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.UpdateTaskDTO;
import com.poshtarenko.codeforge.dto.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewTaskDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.TaskService;
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
@RequestMapping("/answer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping("/{id}")
    public ViewAnswerDTO findAnswer(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        answerService.checkAccess(id, userId);

        return answerService.find(id);
    }

    @PostMapping
    public ViewAnswerDTO createAnswer(@RequestBody SaveAnswerDTO answerDTO) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);

        SaveAnswerDTO saveAnswerDTO = new SaveAnswerDTO(
                answerDTO.code(),
                answerDTO.taskId(),
                SecurityUtils.getUserId()
        );

        return answerService.save(saveAnswerDTO);
    }

    @PutMapping("/{id}")
    public ViewAnswerDTO updateAnswer(@PathVariable long id, @RequestBody UpdateAnswerDTO answerDTO) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        answerService.checkAccess(id, userId);

        UpdateAnswerDTO updateAnswerDTO = new UpdateAnswerDTO(
                id,
                answerDTO.code()
        );

        return answerService.update(updateAnswerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        answerService.checkAccess(id, userId);

        answerService.delete(id);
        return ResponseEntity.ok().build();
    }

}
