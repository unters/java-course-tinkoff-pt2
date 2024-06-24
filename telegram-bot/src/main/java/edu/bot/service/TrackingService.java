package edu.bot.service;

import edu.bot.client.scrapper.ScrapperClient;
import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dao.ChatStatusDao;
import edu.bot.dao.TrackingDao;
import edu.bot.domain.ChatStatus;
import edu.bot.dto.request.UpdateTo;
import edu.common.dto.tracking.TrackingDataTo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private static final String INVALID_URL_RESPONSE_MESSAGE = "Invalid url.";
    private static final String SERVER_ERROR_RESPONSE_MESSAGE = "Server error.";

    private final TelegramClient telegramClient;
    private final ScrapperClient scrapperClient;
    private final ChatStatusDao chatStatusDao;
    private final TrackingDao trackingDao;

    public void trackUrl(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        String url = update.message().text();
        ResponseEntity<?> response = scrapperClient.trackUrl(new TrackingDataTo(chatId, URI.create(url)));
        String responseMessage = switch (response.getStatusCode()) {
            case ACCEPTED -> "Tracking has been successfully added.";
            case UNPROCESSABLE_ENTITY -> INVALID_URL_RESPONSE_MESSAGE;
            default -> SERVER_ERROR_RESPONSE_MESSAGE;
        };
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, responseMessage);
        telegramClient.sendMessage(sendMessageTo);
        chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
    }

    public void untrackUrl(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        String url = update.message().text();
        ResponseEntity<?> response = scrapperClient.untrackUrl(new TrackingDataTo(chatId, URI.create(url)));
        String responseMessage = switch (response.getStatusCode()) {
            case ACCEPTED -> "Url has been successfully removed from set of tracked urls.";
            case UNPROCESSABLE_ENTITY -> INVALID_URL_RESPONSE_MESSAGE;
            default -> SERVER_ERROR_RESPONSE_MESSAGE;
        };
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, responseMessage);
        telegramClient.sendMessage(sendMessageTo);
        chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
    }

    public void getTrackings(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        List<String> trackings = trackingDao.getTrackings(chatId);
        String message = trackings.isEmpty()
            ? "No urls are being tracked yet."
            : trackings.stream().collect(Collectors.joining("\n"));
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, message);
        telegramClient.sendMessage(sendMessageTo);
        chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
    }
}
