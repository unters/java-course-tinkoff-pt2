package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dao.ChatStatusDao;
import edu.bot.domain.Command;
import edu.bot.dto.request.UpdateTo;
import edu.bot.utils.command.CommandParser;
import edu.bot.utils.command.CommandProcessingHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.bot.domain.ChatStatus.AWAITING_COMMAND;
import static edu.bot.domain.ChatStatus.AWAITING_URL_TO_TRACK;
import static edu.bot.domain.ChatStatus.AWAITING_URL_TO_UNTRACK;

@Service
@RequiredArgsConstructor
public class CommandProcessingService {

    private final TelegramClient telegramClient;
    private final TrackingService trackingService;
    private final ChatStatusDao chatStatusDao;

    @SuppressWarnings("MissingSwitchDefault")
    public void processCommand(UpdateTo update) {
        String message = update.message().text();
        Command command = CommandParser.resolveCommand(message);

        if (command == null) {
            Long chatId = update.message().chat().chatId();
            SendMessageTo sendMessageTo = new SendMessageTo(chatId, CommandProcessingHelper.getWrongCommandMessage());
            telegramClient.sendMessage(sendMessageTo);
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

    private void start(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        chatStatusDao.upsertChatStatus(chatId, AWAITING_COMMAND);
        String message = "Welcome to ... <add welcome message>.";
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, message);
        telegramClient.sendMessage(sendMessageTo);
    }

    private void help(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, CommandProcessingHelper.getHelpMessage());
        telegramClient.sendMessage(sendMessageTo);
    }

    private void track(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        chatStatusDao.upsertChatStatus(chatId, AWAITING_URL_TO_TRACK);
        String message = "Please, enter the url to track.";
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, message);
        telegramClient.sendMessage(sendMessageTo);
    }

    private void untrack(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        chatStatusDao.upsertChatStatus(chatId, AWAITING_URL_TO_UNTRACK);
        String message = "Please, enter the url to untrack.";
        SendMessageTo sendMessageTo = new SendMessageTo(chatId, message);
        telegramClient.sendMessage(sendMessageTo);
    }

    // TODO: remove inline url widgets from response messages.
    private void list(UpdateTo update) {
        trackingService.getTrackings(update);
    }
}
