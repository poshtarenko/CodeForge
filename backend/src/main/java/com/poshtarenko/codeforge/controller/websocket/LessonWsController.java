package com.poshtarenko.codeforge.controller.websocket;

import com.poshtarenko.codeforge.dto.request.UpdateLessonDescriptionDTO;
import com.poshtarenko.codeforge.dto.request.UpdateParticipationCodeDTO;
import com.poshtarenko.codeforge.dto.response.ViewLessonDTO;
import com.poshtarenko.codeforge.dto.response.ViewParticipationDTO;
import com.poshtarenko.codeforge.service.LessonService;
import com.poshtarenko.codeforge.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class LessonWsController {

    private final LessonService lessonService;
    private final ParticipationService participationService;

    @MessageMapping("/lesson/{lessonId}/update_description")
    @SendTo("/topic/lesson/{lessonId}/description_updates")
    public ViewLessonDTO updateLessonDesc(@DestinationVariable Long lessonId,
                                          @RequestBody @Validated UpdateLessonDescriptionDTO dto) {
        return lessonService.updateCurrentDescription(lessonId, dto);
    }

    @MessageMapping("/lesson/{lessonId}/participation/update_code")
    @SendTo("/topic/lesson/{lessonId}/participation_updates")
    public ViewParticipationDTO updateParticipationCode(@DestinationVariable Long lessonId,
                                                        @RequestBody @Validated UpdateParticipationCodeDTO dto) {
        return participationService.updateCode(lessonId, dto);
    }

    @MessageMapping("/lesson/{lessonId}/participation/{participationId}/evaluate_code")
    @SendTo("/topic/lesson/{lessonId}/participation_updates")
    public ViewParticipationDTO evaluateParticipationCode(@DestinationVariable Long lessonId,
                                                          @DestinationVariable Long participationId) {
        return participationService.evaluateCode(participationId);
    }
}