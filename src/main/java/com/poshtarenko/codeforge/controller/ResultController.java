package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.request.UpdateResultDTO;
import com.poshtarenko.codeforge.dto.response.ViewResultDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.ResultService;
import com.poshtarenko.codeforge.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/result")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;
    private final TestService testService;

    @GetMapping("/{id}")
    public ViewResultDTO findResult(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        resultService.checkAccess(id, userId);

        return resultService.find(id);
    }

    @GetMapping("/by_test_and_respondent/{testId}")
    public ViewResultDTO findRespondentTestResult(@PathVariable long testId) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        Optional<ViewResultDTO> result = resultService.findRespondentTestResult(userId, testId);
        return result.orElse(null);
    }

//    @GetMapping("/by_test/{testId}")
//    public List<ViewResultDTO> findTestResults(@PathVariable long testId) {
//        SecurityUtils.checkUserRole(ERole.AUTHOR);
//        testService.checkAccess(id, userId);
//        long userId = SecurityUtils.getUserId();
//        Optional<ViewResultDTO> result = resultService.findRespondentTestResult(userId, testId);
//        return result.orElse(null);
//    }

    @PutMapping("/{id}")
    public ViewResultDTO updateResult(@RequestBody UpdateResultDTO resultDTO) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        resultService.checkAccess(resultDTO.id(), userId);
        return resultService.update(resultDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResult(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.RESPONDENT);
        long userId = SecurityUtils.getUserId();
        resultService.checkAccess(id, userId);

        resultService.delete(id);
        return ResponseEntity.ok().build();
    }

}
