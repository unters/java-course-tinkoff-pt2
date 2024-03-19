package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dao.ChatStatusDao;
import edu.bot.domain.ChatStatus;
import edu.bot.dto.request.UpdateTo;
import edu.bot.utils.command.CommandParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.bot.domain.Command.START;

@Service
@RequiredArgsConstructor
public class UpdatesProcessingService {

    private static final String WELCOME_MESSAGE = "Welcome! Enter /help to see list of commands.";
    private static final String ENTER_START_MESSAGE = "Please, enter /start to start using bot.";

    private final TelegramClient telegramClient;
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
                SendMessageTo sendMessageTo = new SendMessageTo(chatId, WELCOME_MESSAGE);
                telegramClient.sendMessage(sendMessageTo);
            } else {
                SendMessageTo sendMessageTo = new SendMessageTo(chatId, ENTER_START_MESSAGE);
                telegramClient.sendMessage(sendMessageTo);
            }
        }

        switch (chatStatus) {
            case AWAITING_COMMAND -> commandProcessingService.processCommand(update);
            case AWAITING_URL_TO_TRACK -> trackingService.trackUrl(update);
            case AWAITING_URL_TO_UNTRACK -> trackingService.untrackUrl(update);
        }
    }
}
