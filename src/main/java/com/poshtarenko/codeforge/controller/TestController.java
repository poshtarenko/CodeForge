package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.dto.SaveTestDTO;
import com.poshtarenko.codeforge.dto.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.ViewTestDTO;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.util.SecurityUtils;
import com.poshtarenko.codeforge.service.TestService;
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
@RequestMapping("/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/{id}")
    public ViewTestDTO findTest(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long userId = SecurityUtils.getUserId();
        testService.checkAccess(id, userId);

        return testService.find(id);
    }

    @PostMapping
    public ViewTestDTO createTest(@RequestBody SaveTestDTO testDTO) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);

        SaveTestDTO saveTestDTO = new SaveTestDTO(
                testDTO.name(),
                testDTO.maxDuration(),
                SecurityUtils.getUserId()
        );

        return testService.save(saveTestDTO);
    }

    @PutMapping("/{id}")
    public ViewTestDTO updateTest(@PathVariable long id, @RequestBody UpdateTestDTO testDTO) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long userId = SecurityUtils.getUserId();
        testService.checkAccess(id, userId);

        UpdateTestDTO updateTestDTO = new UpdateTestDTO(
                id,
                testDTO.name(),
                testDTO.maxDuration(),
                userId
        );

        return testService.update(updateTestDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTest(@PathVariable long id) {
        SecurityUtils.checkUserRole(ERole.AUTHOR);
        long userId = SecurityUtils.getUserId();
        testService.checkAccess(id, userId);

        testService.delete(id);
        return ResponseEntity.ok().build();
    }

}
