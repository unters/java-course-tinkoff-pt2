package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final TelegramClient telegramClient;
    private final Map<EventType, EventTransformer> eventTransformerMap;

    public EventService(
        TelegramClient telegramClient,
        List<EventTransformer> eventTransformers
    ) {
        this.telegramClient = telegramClient;
        this.eventTransformerMap = buildEventTransformerMap(eventTransformers);
    }

    public void sendEvent(Long chatId, AbstractEventTo eventTo) {
        EventTransformer eventTransformer = resolveEventTransformer(eventTo);
        String message = eventTransformer.transformToMessage(eventTo);
        telegramClient.sendMessage(new SendMessageTo(chatId, message));
    }

    private Map<EventType, EventTransformer> buildEventTransformerMap(List<EventTransformer> eventTransformers) {
        return eventTransformers.stream()
            .collect(Collectors.toMap(EventTransformer::suitableFor, Function.identity()));
    }

    private EventTransformer resolveEventTransformer(AbstractEventTo eventTo) {
        EventTransformer eventTransformer = eventTransformerMap.get(eventTo.getType());
        if (eventTransformer == null) {
            // TODO: throw exception.
        }

        return eventTransformer;
    }
}
