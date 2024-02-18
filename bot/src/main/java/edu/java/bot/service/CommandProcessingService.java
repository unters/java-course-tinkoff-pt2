package edu.java.bot.service;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.domain.ChatStatus;
import edu.java.bot.domain.Command;
import edu.java.bot.service.utils.CommandParser;
import edu.java.bot.service.utils.CommandProcessingServiceHelper;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@Service
public class CommandProcessingService {

    private static final Logger LOGGER = LogManager.getLogger(CommandProcessingService.class);

    private final TelegramBot telegramBot;
    private final TrackingService trackingService;
    private final ConcurrentMap<Long, ChatStatus> chatStatuses;

    public CommandProcessingService(
            TelegramBot telegramBot,
            TrackingService trackingService,
            ConcurrentMap<Long, ChatStatus> chatStatuses
    ) {
        this.telegramBot = telegramBot;
        this.trackingService = trackingService;
        this.chatStatuses = chatStatuses;
    }

    public void processCommand(@NotNull Update update) {
        String message = update.message().text();
        Command command = CommandParser.resolveCommand(message);

        if (command == null) {
            Long chatId = update.message().from().id();
            SendMessage request = new SendMessage(chatId, CommandProcessingServiceHelper.getWrongCommandMessage());
            telegramBot.execute(request);
            LOGGER.info("Update %d contained wrong command. Type /help to see list of available commands.".formatted(update.hashCode()));
            return;
        }

        switch (command) {
            case START -> start(update);
            case HELP -> help(update);
            case TRACK -> track(update);
            case UNTRACK -> untrack(update);
            case LIST -> list(update);
        }
    }

    private void start(@NotNull Update update) {
        Long chatId = update.message().from().id();
        String message = "Command has been recognised as /start.";
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(request);
    }

    private void help(@NotNull Update update) {
        Long chatId = update.message().from().id();
        SendMessage request = new SendMessage(chatId, CommandProcessingServiceHelper.getHelpMessage());
        telegramBot.execute(request);
    }

    private void track(@NotNull Update update) {
        Long chatId = update.message().from().id();
        // TODO: add logic.
        String message = "Please, enter the url to track.";
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                chatStatuses.put(chatId, ChatStatus.AWAITING_URL_TO_TRACK);
            }

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {

            }
        });
    }

    private void untrack(@NotNull Update update) {
        Long chatId = update.message().from().id();
        // TODO: add logic.
        String message = "Please, enter the url to untrack.";
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(request, new Callback<SendMessage, SendResponse>() {
            @Override
            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                chatStatuses.put(chatId, ChatStatus.AWAITING_URL_TO_UNTRACK);
            }

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {

            }
        });
    }

    private void list(@NotNull Update update) {
        trackingService.getTrackings(update);
    }
}
