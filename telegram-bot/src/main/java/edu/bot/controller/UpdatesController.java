package edu.bot.controller;

import edu.bot.dto.request.UpdateTo;
import edu.bot.service.UpdateProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdatesController {

    private final UpdateProcessingService updateProcessingService;

    @PostMapping("/update")
    public void update(@RequestBody UpdateTo update) {
        updateProcessingService.processUpdate(update);
    }
}
