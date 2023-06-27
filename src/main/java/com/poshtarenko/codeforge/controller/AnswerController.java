package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/answer")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final TestService testService;

    @GetMapping("/{id}")
    public ViewAnswerDTO findAnswer(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        answerService.checkAccess(id, respondentId);
        return answerService.find(id);
    }

    @GetMapping("/by_test/{testId}")
    public List<ViewAnswerDTO> findTestAnswers(@PathVariable long testId) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long authorId = SecurityUtils.getUserId();
        testService.checkAccess(testId, authorId);
        return answerService.findByTest(testId);
    }

    @GetMapping("/current/{testId}")
    public ViewAnswerDTO findRespondentCurrentAnswer(@PathVariable long testId) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        Optional<ViewAnswerDTO> result = answerService.findRespondentCurrentAnswer(respondentId, testId);
        return result.orElse(null);
    }

    @PostMapping("/start_test/{testCode}")
    public ViewAnswerDTO startTest(@PathVariable String testCode) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        return answerService.startAnswer(respondentId, testCode);
    }

    @PostMapping("/finish/{answerId}")
    public ViewAnswerDTO finishAnswer(@PathVariable long answerId) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        answerService.checkAccess(answerId, respondentId);
        return answerService.finishAnswer(answerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        answerService.checkAccess(id, respondentId);
        answerService.delete(id);
        return ResponseEntity.ok().build();
    }

}
