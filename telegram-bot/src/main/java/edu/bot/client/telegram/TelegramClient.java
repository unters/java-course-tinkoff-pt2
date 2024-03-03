package edu.bot.client.telegram;

import edu.bot.client.telegram.dto.SendMessageTo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface TelegramClient {

    @PostExchange("/sendMessage")
    ResponseEntity<?> sendMessage(
        @RequestBody
        SendMessageTo message
    );
}
