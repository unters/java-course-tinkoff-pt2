package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.callback.ChatStatusChangingCallback;
import edu.java.bot.domain.ChatStatus;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static edu.java.bot.domain.ChatStatus.AWAITING_COMMAND;

@Service
public class TrackingService {

    private static final Logger LOGGER = LogManager.getLogger(TrackingService.class);

    private final TelegramBot telegramBot;
    private final ConcurrentMap<Long, ChatStatus> chatStatuses;

    private final Map<Long, Set<String>> trackedUrls = new HashMap<>();

    public TrackingService(
            TelegramBot telegramBot,
            ConcurrentMap<Long, ChatStatus> chatStatuses

    ) {
        this.telegramBot = telegramBot;
        this.chatStatuses = chatStatuses;
    }

    public void trackUrl(@NotNull Update update) {
        Long chatId = update.message().chat().id();
        String url = update.message().text();

        // TODO: validate url.

        if (!trackedUrls.containsKey(chatId)) {
            trackedUrls.put(chatId, new HashSet<>());
        }

        Set<String> chatTrackedUrls = trackedUrls.get(chatId);
        if (chatTrackedUrls.contains(url)) {
            SendMessage sendMessage = new SendMessage(chatId, "Given url is already being tracked.");
            telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatuses));
        } else {
            chatTrackedUrls.add(url);
            SendMessage sendMessage = new SendMessage(chatId, "Given url has been added to set of tracked urls.");
            telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatuses));
        }
    }

    public void untrackUrl(@NotNull Update update) {
        Long chatId = update.message().chat().id();
        String url = update.message().text();

        // TODO: validate url?

        Set<String> chatTrackedUrls = trackedUrls.get(chatId);
        if (chatTrackedUrls == null) {
            SendMessage sendMessage = new SendMessage(chatId, "No urls are being tracked yet.");
            telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatuses));
        } else if (!chatTrackedUrls.contains(url)) {
            SendMessage sendMessage = new SendMessage(chatId, "Given url is not being tracked. Nothing removed.");
            telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatuses));
        } else {
            chatTrackedUrls.remove(url);
            SendMessage sendMessage = new SendMessage(chatId, "Given url has been removed from set of tracked urls.");
            telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatuses));
        }
    }

    public void getTrackings(@NotNull Update update) {
        Long chatId = update.message().chat().id();
        Set<String> chatTrackedUrls = trackedUrls.get(chatId);

        if (chatTrackedUrls == null || chatTrackedUrls.isEmpty()) {
            SendMessage sendMessage = new SendMessage(chatId, "No urls are being tracked yet.");
            telegramBot.execute(sendMessage);
        } else {
            String message = "Urls being tracked: " + String.join(", ", chatTrackedUrls) + ".";
            SendMessage sendMessage = new SendMessage(chatId, message);
            telegramBot.execute(sendMessage);
        }
    }
}
