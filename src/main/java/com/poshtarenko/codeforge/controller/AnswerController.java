package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.request.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.response.TryCodeResponse;
import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.AnswerService;
import lombok.RequiredArgsConstructor;
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
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

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

        return answerService.put(saveAnswerDTO);
    }

    @PostMapping("/try_code")
    public TryCodeResponse tryCode(@RequestBody TryCodeRequest request) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        CodeEvaluationResult codeEvaluationResult = answerService.tryCode(request);

        return new TryCodeResponse(
                codeEvaluationResult.isCompleted(),
                codeEvaluationResult.evaluationTime(),
                codeEvaluationResult.error().orElse("")
        );
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
