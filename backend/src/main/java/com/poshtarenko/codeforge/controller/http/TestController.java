package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.TestService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/{id}")
    public ViewTestDTO findTest(@PathVariable @Positive long id) {
        return testService.find(id);
    }

    @GetMapping
    public List<ViewTestDTO> findAuthorTests(@AuthenticationPrincipal UserDetailsImpl user) {
        return testService.findAuthorTests(user.getId());
    }

    @PostMapping
    public ViewTestDTO createTest(@AuthenticationPrincipal UserDetailsImpl user,
                                  @RequestBody @Validated SaveTestDTO testDTO) {
        return testService.save(user.getId(), testDTO);
    }

    @PutMapping("/{id}")
    public ViewTestDTO updateTest(@PathVariable @Positive long id,
                                  @RequestBody @Validated UpdateTestDTO testDTO,
                                  @AuthenticationPrincipal UserDetailsImpl user) {
        testService.checkAccess(id, user.getId());
        return testService.update(id, testDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable @Positive long id,
                                        @AuthenticationPrincipal UserDetailsImpl user) {
        testService.checkAccess(id, user.getId());
        testService.delete(id);
        return ResponseEntity.ok().build();
    }
}
