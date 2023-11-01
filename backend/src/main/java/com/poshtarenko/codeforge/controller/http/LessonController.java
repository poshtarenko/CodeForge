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

    @GetMapping("/as_author/{id}")
    public ViewLessonDTO findLessonAsAuthor(@PathVariable @Positive long id,
                                            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return lessonService.findAsAuthor(id);
    }

    @GetMapping("/as_respondent/{testId}")
    public ViewLessonDTO findLessonAsRespondent(@PathVariable @Positive long testId,
                                                @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return lessonService.findAsRespondent(testId);
    }

    @GetMapping("/as_author/my")
    public List<ViewLessonDTO> findAuthorLessons(@AuthenticationPrincipal UserDetailsImpl currentUser) {
        return lessonService.findAuthorLessons(currentUser.getId());
    }

    @PostMapping
    public ViewLessonDTO createLesson(@RequestBody @Validated SaveLessonDTO lessonDTO,
                                      @AuthenticationPrincipal UserDetailsImpl currentUser) {
        SaveLessonDTO saveLessonDTO = new SaveLessonDTO(
                lessonDTO.name(),
                currentUser.getId()
        );

        return lessonService.save(saveLessonDTO);
    }

    @PutMapping("/{id}")
    public ViewLessonDTO updateLesson(@PathVariable @Positive long id,
                                      @RequestBody @Validated UpdateLessonDTO lessonDTO,
                                      @AuthenticationPrincipal UserDetailsImpl currentUser) {

        UpdateLessonDTO updateLessonDTO = new UpdateLessonDTO(
                id,
                lessonDTO.name(),
                lessonDTO.languageId()
        );

        return lessonService.update(updateLessonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable @Positive long id,
                                          @AuthenticationPrincipal UserDetailsImpl currentUser) {
        lessonService.delete(id);
        return ResponseEntity.ok().build();
    }

}
