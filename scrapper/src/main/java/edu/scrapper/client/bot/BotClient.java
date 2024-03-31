package edu.scrapper.client.bot;

import edu.common.dto.event.AbstractEventTo;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface BotClient {

    @PostExchange("/{chatId}/sendEvent")
    @Retryable
    void sendEventData(
        @PathVariable("chatId")
        Long chatId,
        @RequestBody
        AbstractEventTo eventTo
    );
}
