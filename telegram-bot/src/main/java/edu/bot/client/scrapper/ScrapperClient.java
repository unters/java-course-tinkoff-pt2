package edu.bot.client.scrapper;

import edu.common.dto.tracking.TrackingDataTo;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ScrapperClient {

    @PostExchange("/track")
    @Retryable
    ResponseEntity<?> trackUrl(
        @RequestBody
        TrackingDataTo trackingDataTo
    );

    @PostExchange("/untrack")
    @Retryable
    ResponseEntity<?> untrackUrl(
        @RequestBody
        TrackingDataTo trackingDataTo
    );
}
