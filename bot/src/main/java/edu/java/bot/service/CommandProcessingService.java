package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.callback.ChatStatusChangingCallback;
import edu.java.bot.dao.ChatStatusesDao;
import edu.java.bot.domain.ChatStatus;
import edu.java.bot.domain.Command;
import edu.java.bot.service.utils.CommandParser;
import edu.java.bot.service.utils.CommandProcessingHelper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class CommandProcessingService {

    private final TelegramBot telegramBot;
    private final TrackingService trackingService;
    private final ChatStatusesDao chatStatusesDao;

    public CommandProcessingService(
        TelegramBot telegramBot,
        TrackingService trackingService,
        ChatStatusesDao chatStatusesDao
    ) {
        this.telegramBot = telegramBot;
        this.trackingService = trackingService;
        this.chatStatusesDao = chatStatusesDao;
    }

    @SuppressWarnings("MissingSwitchDefault")
    public void processCommand(@NotNull Update update) {
        String message = update.message().text();
        Command command = CommandParser.resolveCommand(message);

        if (command == null) {
            Long chatId = update.message().from().id();
            SendMessage request = new SendMessage(chatId, CommandProcessingHelper.getWrongCommandMessage());
            telegramBot.execute(request);
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
        String message = "Welcome to ... <add welcome message>.";
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(request);
    }

    private void help(@NotNull Update update) {
        Long chatId = update.message().from().id();
        SendMessage request = new SendMessage(chatId, CommandProcessingHelper.getHelpMessage());
        telegramBot.execute(request);
    }

    private void track(@NotNull Update update) {
        Long chatId = update.message().from().id();
        String message = "Please, enter the url to track.";
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(
            request,
            new ChatStatusChangingCallback(chatId, ChatStatus.AWAITING_URL_TO_TRACK, chatStatusesDao)
        );
    }

    private void untrack(@NotNull Update update) {
        Long chatId = update.message().from().id();
        String message = "Please, enter the url to untrack.";
        SendMessage request = new SendMessage(chatId, message);
        telegramBot.execute(
            request,
            new ChatStatusChangingCallback(chatId, ChatStatus.AWAITING_URL_TO_UNTRACK, chatStatusesDao)
        );
    }

    // TODO: remove inline url widgets rom response messages.
    private void list(@NotNull Update update) {
        trackingService.getTrackings(update);
    }
}
