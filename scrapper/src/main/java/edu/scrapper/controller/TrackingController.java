package edu.scrapper.controller;

import edu.common.dto.tracking.TrackingDataTo;
import edu.scrapper.exception.TrackingDataValidationException;
import edu.scrapper.service.TrackingService;
import edu.scrapper.validation.TrackingDataValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/track")
    @ExceptionHandler
    public void trackUrl(
        @Valid @RequestBody
        TrackingDataTo trackingDataTo
    ) {
        TrackingDataValidator.validate(trackingDataTo);
        trackingService.trackUrl(trackingDataTo);
    }


    @PostMapping("/untrack")
    public void untrackUrl(
        @RequestBody
        TrackingDataTo trackingDataTo
    ) {
        TrackingDataValidator.validate(trackingDataTo);
        trackingService.untrackUrl(trackingDataTo);
    }

    @SuppressWarnings("MagicNumber")
    @ExceptionHandler(TrackingDataValidationException.class)
    public ResponseEntity handleException(TrackingDataValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(422));
    }
}
