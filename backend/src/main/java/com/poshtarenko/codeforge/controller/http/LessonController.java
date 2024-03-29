package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.request.SaveLessonDTO;
import com.poshtarenko.codeforge.dto.request.UpdateLessonDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.LessonService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping
    public List<ViewLessonDTO> findAuthorLessons(@AuthenticationPrincipal UserDetailsImpl user) {
        return lessonService.findAuthorLessons(user.getId());
    }

    @GetMapping("/{id}")
    public ViewLessonDTO findLesson(@PathVariable @Positive long id) {
        return lessonService.find(id);
    }

    @PostMapping
    public ViewLessonDTO createLesson(@RequestBody @Validated SaveLessonDTO lessonDTO,
                                      @AuthenticationPrincipal UserDetailsImpl user) {
        return lessonService.save(user.getId(), lessonDTO);
    }

    @PutMapping("/{id}")
    public ViewLessonDTO updateLesson(@PathVariable @Positive long id,
                                      @RequestBody @Validated UpdateLessonDTO lessonDTO) {
        return lessonService.update(id, lessonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable @Positive long id) {
        lessonService.delete(id);
        return ResponseEntity.ok().build();
    }
}
