package edu.bot.service;

import edu.bot.client.scrapper.ScrapperClient;
import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dao.TrackingDao;
import edu.bot.dto.request.UpdateTo;
import java.util.Set;
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
    private final TrackingDao trackingDao;

    public void trackUrl(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        String url = update.message().text();
        ResponseEntity<?> response = scrapperClient.trackUrl(chatId, url);
        String responseMessage = switch (response.getStatusCode()) {
            case ACCEPTED -> "Tracking has been successfully added.";
            case UNPROCESSABLE_ENTITY -> INVALID_URL_RESPONSE_MESSAGE;
            default -> SERVER_ERROR_RESPONSE_MESSAGE;
        };
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, responseMessage);
        telegramClient.sendMessage(sendMessageTo);
    }

    public void untrackUrl(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        String url = update.message().text();
        scrapperClient.untrackUrl(chatId, url);
        ResponseEntity<?> response = scrapperClient.untrackUrl(chatId, url);
        String responseMessage = switch (response.getStatusCode()) {
            case ACCEPTED -> "Url has been successfully removed from set of tracked urls.";
            case UNPROCESSABLE_ENTITY -> INVALID_URL_RESPONSE_MESSAGE;
            default -> SERVER_ERROR_RESPONSE_MESSAGE;
        };
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, responseMessage);
        telegramClient.sendMessage(sendMessageTo);
    }

    public void getTrackings(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        Set<String> trackings = trackingDao.getTrackings(chatId);
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, trackings.stream().collect(Collectors.joining("\n")));
        telegramClient.sendMessage(sendMessageTo);
    }
}
