package edu.bot.controller;

import edu.bot.service.EventService;
import edu.common.dto.event.AbstractEventTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "event", description = "Events sending Api")
@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "Send event to the chat", tags = "event")
    @ApiResponse(
        responseCode = "200",
        description = "Event data sent"
    )
    @PostMapping("/{chatId}/sendEvent")
    public void sendEventData(
        @PathVariable("chatId")
        Long chatId,
        @RequestBody
        AbstractEventTo eventTo
    ) {
        eventService.sendEvent(chatId, eventTo);
    }
}
