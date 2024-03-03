package edu.bot.service;

import edu.bot.dao.ChatStatusDao;
import edu.bot.dto.request.UpdateTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProcessingService {

    private final CommandProcessingService commandProcessingService;
    private final TrackingService trackingService;
    private final ChatStatusDao chatStatusDao;

    @SuppressWarnings("MissingSwitchDefault")
    public void processUpdate(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        switch (chatStatusDao.getChatStatus(chatId)) {
            case AWAITING_COMMAND -> commandProcessingService.processCommand(update);
            case AWAITING_URL_TO_TRACK -> trackingService.trackUrl(update);
            case AWAITING_URL_TO_UNTRACK -> trackingService.untrackUrl(update);
        }
    }
}
