package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dao.ChatStatusDao;
import edu.bot.domain.ChatStatus;
import edu.bot.dto.request.UpdateTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatesProcessingService {

    private final TelegramClient telegramClient;
    private final CommandProcessingService commandProcessingService;
    private final TrackingService trackingService;
    private final ChatStatusDao chatStatusDao;

    @SuppressWarnings("MissingSwitchDefault")
    public void processUpdate(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        ChatStatus chatStatus = chatStatusDao.getChatStatus(chatId);
        if (chatStatus == null) {
            chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
            SendMessageTo sendMessageTo = new SendMessageTo(chatId, "Welcome! Enter /help to see list of commands.");
            telegramClient.sendMessage(sendMessageTo);
        }

        switch (chatStatus) {
            case AWAITING_COMMAND -> commandProcessingService.processCommand(update);
            case AWAITING_URL_TO_TRACK -> trackingService.trackUrl(update);
            case AWAITING_URL_TO_UNTRACK -> trackingService.untrackUrl(update);
        }
    }
}
