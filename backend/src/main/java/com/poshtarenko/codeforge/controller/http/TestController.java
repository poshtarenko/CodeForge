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

    @GetMapping("/as_author/{id}")
    public ViewTestDTO findTestAsAuthor(@PathVariable @Positive long id,
                                        @AuthenticationPrincipal UserDetailsImpl currentUser) {
        testService.checkAccess(id, currentUser.getId());
        return testService.find(id);
    }

    @GetMapping("/as_respondent/{testId}")
    public ViewTestDTO findTestAsRespondent(@PathVariable @Positive long testId,
                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        testService.checkRespondentConnectedToTest(currentUser.getId(), testId);
        return testService.findAsRespondent(testId);
    }

    @GetMapping("/as_author/my")
    public List<ViewTestDTO> findAuthorTests(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return testService.findAuthorTests(currentUser.getId());
    }

    @PostMapping
    public ViewTestDTO createTest(@RequestBody @Validated SaveTestDTO testDTO) {
        return testService.save(testDTO);
    }

    @PutMapping("/{id}")
    public ViewTestDTO updateTest(@PathVariable @Positive long id,
                                  @RequestBody @Validated UpdateTestDTO testDTO,
                                  @AuthenticationPrincipal UserDetailsImpl currentUser) {
        testService.checkAccess(id, currentUser.getId());
        return testService.update(id, testDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable @Positive long id,
                                        @AuthenticationPrincipal UserDetailsImpl currentUser) {
        testService.checkAccess(id, currentUser.getId());
        testService.delete(id);
        return ResponseEntity.ok().build();
    }

}
