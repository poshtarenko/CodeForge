package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.request.SaveSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.SolutionService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solutions")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    @GetMapping("/{id}")
    public ViewSolutionDTO findSolution(@PathVariable @Positive long id,
                                        @AuthenticationPrincipal UserDetailsImpl currentUser) {
        solutionService.checkAccess(id, currentUser.getId());
        return solutionService.find(id);
    }

    @PostMapping
    public ViewSolutionDTO putSolution(@RequestBody @Validated SaveSolutionDTO solutionDTO,
                                       @AuthenticationPrincipal UserDetailsImpl currentUser) {
        SaveSolutionDTO saveSolutionDTO = new SaveSolutionDTO(
                solutionDTO.code(),
                solutionDTO.taskId(),
                solutionDTO.answerId(),
                currentUser.getId()
        );

        return solutionService.put(saveSolutionDTO);
    }

    @PostMapping("/try_code")
    public String tryCode(@RequestBody @Validated TryCodeRequest request) {
        return solutionService.tryCode(request).getMessage();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSolution(@PathVariable @Positive long id,
                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        solutionService.checkAccess(id, currentUser.getId());
        solutionService.delete(id);
        return ResponseEntity.ok().build();
    }

}
