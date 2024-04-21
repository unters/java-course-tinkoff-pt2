package edu.bot.service;

import edu.bot.client.scrapper.ScrapperClient;
import edu.bot.dao.ChatStatusDao;
import edu.bot.dao.TrackingDao;
import edu.bot.domain.ChatStatus;
import edu.bot.dto.request.UpdateTo;
import edu.common.dto.tracking.TrackingDataTo;
import java.net.URI;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private static final String ERROR_MESSAGE_PROPERTY = "tracking.service.error.message";

    private final ResourceBundle telegramMessages;

    private final ScrapperClient scrapperClient;
    private final TelegramService telegramService;
    private final ChatStatusDao chatStatusDao;
    private final TrackingDao trackingDao;

    public void trackUrl(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        String url = update.message().text();
        ResponseEntity<?> response = scrapperClient.trackUrl(new TrackingDataTo(chatId, URI.create(url)));
        String responseMessage = switch (response.getStatusCode()) {
            case ACCEPTED -> telegramMessages.getString("tracking.url.to.track.accepted.message");
            case UNPROCESSABLE_ENTITY -> telegramMessages.getString("tracking.url.to.track.invalid.message");
            default -> telegramMessages.getString(ERROR_MESSAGE_PROPERTY);
        };
        telegramService.sendMessage(chatId, responseMessage);
        chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
    }

    public void untrackUrl(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        String url = update.message().text();
        ResponseEntity<?> response = scrapperClient.untrackUrl(new TrackingDataTo(chatId, URI.create(url)));
        String responseMessage = switch (response.getStatusCode()) {
            case ACCEPTED -> telegramMessages.getString("tracking.url.to.untrack.accepted.message");
            case UNPROCESSABLE_ENTITY -> telegramMessages.getString("tracking.url.to.untrack.invalid.message");
            default -> telegramMessages.getString(ERROR_MESSAGE_PROPERTY);
        };
        telegramService.sendMessage(chatId, responseMessage);
        chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
    }

    public void getTrackings(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        List<String> trackings = trackingDao.getTrackings(chatId);
        String message = trackings.isEmpty()
            ? telegramMessages.getString("tracking.empty.list.message")
            : trackings.stream().collect(Collectors.joining("\n"));
        telegramService.sendMessage(chatId, message);
        chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
    }
}
