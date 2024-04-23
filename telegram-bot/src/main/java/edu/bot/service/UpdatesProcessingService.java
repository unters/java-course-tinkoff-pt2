package edu.bot.service;

import edu.bot.dao.ChatStatusDao;
import edu.bot.domain.ChatStatus;
import edu.bot.dto.request.UpdateTo;
import edu.bot.utils.CommandParser;
import java.util.ResourceBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.bot.domain.Command.START;

@Service
@RequiredArgsConstructor
public class UpdatesProcessingService {

    private final ResourceBundle telegramMessages;

    private final TelegramService telegramService;
    private final CommandProcessingService commandProcessingService;
    private final TrackingService trackingService;
    private final ChatStatusDao chatStatusDao;

    @SuppressWarnings("MissingSwitchDefault")
    public void processUpdate(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        ChatStatus chatStatus = chatStatusDao.getChatStatus(chatId);
        if (chatStatus == null) {
            if (START.equals(CommandParser.resolveCommand(update.message().text()))) {
                chatStatusDao.upsertChatStatus(chatId, ChatStatus.AWAITING_COMMAND);
                telegramService.sendMessage(chatId, telegramMessages.getString("new.user.welcome.message"));
            } else {
                telegramService.sendMessage(chatId, telegramMessages.getString("new.user.start.invitation.message"));
            }
        } else {
            switch (chatStatus) {
                case AWAITING_COMMAND -> commandProcessingService.processCommand(update);
                case AWAITING_URL_TO_TRACK -> trackingService.trackUrl(update);
                case AWAITING_URL_TO_UNTRACK -> trackingService.untrackUrl(update);
            }
        }
    }
}
