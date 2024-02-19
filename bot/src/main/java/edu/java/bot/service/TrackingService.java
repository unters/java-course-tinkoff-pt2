package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.callback.ChatStatusChangingCallback;
import edu.java.bot.dao.ChatStatusesDao;
import edu.java.bot.dao.TrackingDao;
import edu.java.bot.domain.TrackingStatus;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.stereotype.Service;
import static edu.java.bot.domain.ChatStatus.AWAITING_COMMAND;

@Service
public class TrackingService {

    private final TelegramBot telegramBot;
    private final TrackingDao trackingDao;
    private final ChatStatusesDao chatStatusesDao;

    public TrackingService(
        TelegramBot telegramBot,
        TrackingDao trackingDao,
        ChatStatusesDao chatStatusesDao
    ) {
        this.telegramBot = telegramBot;
        this.trackingDao = trackingDao;
        this.chatStatusesDao = chatStatusesDao;
    }

    public void trackUrl(@NotNull Update update) {
        Long chatId = update.message().chat().id();
        String url = update.message().text();
        // TODO: add url validation logic.
        TrackingStatus trackingStatus = trackingDao.addTracking(chatId, url);

        // TODO: move messages to TrackingHelper.
        String responseMessage = switch (trackingStatus) {
            case TRACK_ADDED -> "Given url has been added to set of tracked urls.";
            case IS_ALREADY_TRACKED -> "Given url is already being tracked.";
            default -> throw new RuntimeException("Unexpected tracking status in addTracking().");
        };
        SendMessage sendMessage = new SendMessage(chatId, responseMessage);
        telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatusesDao));
    }

    public void untrackUrl(@NotNull Update update) {
        Long chatId = update.message().chat().id();
        String url = update.message().text();
        // TODO: add url validation logic.
        TrackingStatus trackingStatus = trackingDao.removeTracking(chatId, url);

        // TODO: move messages to TrackingHelper.
        String responseMessage = switch (trackingStatus) {
            case TRACK_REMOVED -> "Given url has been removed from set of tracked urls.";
            case IS_NOT_TRACKED -> "Given url is not being tracked. Nothing removed.";
            default -> throw new RuntimeException("Unexpected tracking status in removeTracking().");
        };
        SendMessage sendMessage = new SendMessage(chatId, responseMessage);
        telegramBot.execute(sendMessage, new ChatStatusChangingCallback(chatId, AWAITING_COMMAND, chatStatusesDao));
    }

    public void getTrackings(@NotNull Update update) {
        Long chatId = update.message().chat().id();
        Set<String> chatTrackedUrls = trackingDao.getTrackings(chatId);
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
