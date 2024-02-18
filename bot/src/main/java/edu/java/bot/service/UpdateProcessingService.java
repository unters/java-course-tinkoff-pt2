package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.domain.ChatStatus;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

import static edu.java.bot.domain.ChatStatus.AWAITING_COMMAND;

@Service
public class UpdateProcessingService {

    private static final Logger LOGGER = LogManager.getLogger(CommandProcessingService.class);

    private final TelegramBot telegramBot;
    private final CommandProcessingService commandProcessingService;
    private final TrackingService trackingService;
    private final ConcurrentMap<Long, ChatStatus> chatStatuses;

    public UpdateProcessingService(
            TelegramBot telegramBot,
            CommandProcessingService commandProcessingService,
            TrackingService trackingService,
            ConcurrentMap<Long, ChatStatus> chatStatuses
    ) {
        this.telegramBot = telegramBot;
        this.commandProcessingService = commandProcessingService;
        this.trackingService = trackingService;
        this.chatStatuses = chatStatuses;
    }

    public void processUpdate(@NotNull Update update) {
        Long chatId = update.message().from().id();
        ChatStatus status = chatStatuses.get(chatId);
        if (status == null) {
            status = AWAITING_COMMAND;
            chatStatuses.put(chatId, status);
        }

        switch (status) {
            case AWAITING_COMMAND -> commandProcessingService.processCommand(update);
            case AWAITING_URL_TO_TRACK -> {
                trackingService.trackUrl(update);
                chatStatuses.put(chatId, AWAITING_COMMAND);
            }
            case AWAITING_URL_TO_UNTRACK -> {
                trackingService.untrackUrl(update);
                chatStatuses.put(chatId, AWAITING_COMMAND);
            }
        }
    }
}
