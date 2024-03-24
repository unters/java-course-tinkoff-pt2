package edu.bot.client.scrapper;

import edu.common.dto.tracking.TrackingDataTo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ScrapperClient {

    @PostExchange("/track")
    ResponseEntity<?> trackUrl(
        @RequestBody
        TrackingDataTo trackingDataTo
    );

    @PostExchange("/untrack")
    ResponseEntity<?> untrackUrl(
        @RequestBody
        TrackingDataTo trackingDataTo
    );
}
