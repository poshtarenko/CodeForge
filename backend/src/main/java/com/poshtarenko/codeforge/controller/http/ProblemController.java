package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.request.CreateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problems")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping("/available")
    public List<ViewProblemDTO> findAllAvailableToAuthor(@AuthenticationPrincipal UserDetailsImpl user) {
        return problemService.findAllAvailableToAuthor(user.getId());
    }

    @GetMapping("/custom")
    public List<ViewProblemDTO> findAuthorCustomProblems(@AuthenticationPrincipal UserDetailsImpl user) {
        return problemService.findAuthorCustomProblems(user.getId());
    }

    @PostMapping("/custom")
    public ViewProblemDTO createCustomProblem(@AuthenticationPrincipal UserDetailsImpl user,
                                              @RequestBody @Validated CreateProblemDTO problemDTO) {
        return problemService.createCustomProblem(problemDTO, user.getId());
    }
}
