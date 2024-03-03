package edu.bot.controller;

import edu.bot.dto.request.UpdateTo;
import edu.bot.service.UpdatesProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "update", description = "Updates handling Api (for telegram webhook).")
@RestController
@RequiredArgsConstructor
public class UpdateController {

    private final UpdatesProcessingService updateProcessingService;

    @Operation(summary = "Handle new message from the chat.", tags = "update")
    @ApiResponse(
        responseCode = "200",
        description = "Event data sent"
    )
    @PostMapping("/update")
    public void update(@RequestBody UpdateTo update) {
        updateProcessingService.processUpdate(update);
    }
}
