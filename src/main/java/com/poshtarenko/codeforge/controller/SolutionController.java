package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.model.CodeEvaluationResult;
import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.TryCodeResponse;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solution")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping("/{id}")
    public ViewSolutionDTO findSolution(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        solutionService.checkAccess(id, respondentId);
        return solutionService.find(id);
    }

    @PostMapping
    public ViewSolutionDTO putSolution(@RequestBody SaveSolutionDTO solutionDTO) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);

        SaveSolutionDTO saveSolutionDTO = new SaveSolutionDTO(
                solutionDTO.code(),
                solutionDTO.taskId(),
                solutionDTO.answerId(),
                SecurityUtils.getUserId()
        );

        return solutionService.put(saveSolutionDTO);
    }

    @PostMapping("/try_code")
    public TryCodeResponse tryCode(@RequestBody TryCodeRequest request) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        CodeEvaluationResult codeEvaluationResult = solutionService.tryCode(request);

        return new TryCodeResponse(
                codeEvaluationResult.isCompleted(),
                codeEvaluationResult.evaluationTime(),
                codeEvaluationResult.error().orElse("")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSolution(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long respondentId = SecurityUtils.getUserId();
        solutionService.checkAccess(id, respondentId);

        solutionService.delete(id);
        return ResponseEntity.ok().build();
    }

}
