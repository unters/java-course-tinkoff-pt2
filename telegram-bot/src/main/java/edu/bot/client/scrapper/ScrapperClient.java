package edu.bot.client.scrapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/{chatId}/{url}")
public interface ScrapperClient {

    @PostExchange("/track")
    ResponseEntity<?> trackUrl(
        @PathVariable("chatId")
        Long chatId,
        @PathVariable("url")
        String url
    );

    @PostExchange("/untrack")
    ResponseEntity<?> untrackUrl(
        @PathVariable("chatId")
        Long chatId,
        @PathVariable("url")
        String url
    );
}
