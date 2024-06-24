package edu.scrapper.service.event;

import edu.common.dto.event.AbstractEventTo;
import edu.scrapper.client.bot.BotClient;
import edu.scrapper.service.EventSendingService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebClientEventSendingService implements EventSendingService {

    private final BotClient botClient;

    @Override
    public void send(Long chatId, AbstractEventTo eventTo) {
        botClient.sendEventData(chatId, eventTo);
    }
}
