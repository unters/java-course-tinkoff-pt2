package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dto.request.event.AbstractEventTo;
import edu.bot.utils.transformer.EventToMessageTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final TelegramClient telegramClient;

    public void sendEvent(Long chatId, AbstractEventTo eventTo) {
        String message = EventToMessageTransformer.transformEvent(eventTo);
        telegramClient.sendMessage(new SendMessageTo(chatId, message));
    }
}
