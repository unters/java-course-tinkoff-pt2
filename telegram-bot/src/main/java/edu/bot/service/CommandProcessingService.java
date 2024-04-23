package edu.bot.service;

import edu.bot.dao.ChatStatusDao;
import edu.bot.domain.Command;
import edu.bot.dto.request.UpdateTo;
import edu.bot.utils.CommandParser;
import java.util.ResourceBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static edu.bot.domain.ChatStatus.AWAITING_COMMAND;
import static edu.bot.domain.ChatStatus.AWAITING_URL_TO_TRACK;
import static edu.bot.domain.ChatStatus.AWAITING_URL_TO_UNTRACK;

@Service
@RequiredArgsConstructor
public class CommandProcessingService {

    private final ResourceBundle telegramMessages;

    private final TelegramService telegramService;
    private final TrackingService trackingService;
    private final ChatStatusDao chatStatusDao;

    @SuppressWarnings("MissingSwitchDefault")
    public void processCommand(UpdateTo update) {
        String message = update.message().text();
        Command command = CommandParser.resolveCommand(message);

        if (command == null) {
            Long chatId = update.message().chat().chatId();
            telegramService.sendMessage(chatId, telegramMessages.getString("command.invalid.command.message"));
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
        telegramService.sendMessage(chatId, telegramMessages.getString("command.start.command"));
    }

    private void help(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        telegramService.sendMessage(chatId, telegramMessages.getString("command.help.command"));
    }

    private void track(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        chatStatusDao.upsertChatStatus(chatId, AWAITING_URL_TO_TRACK);
        telegramService.sendMessage(chatId, telegramMessages.getString("command.track.command"));
    }

    private void untrack(UpdateTo update) {
        Long chatId = update.message().chat().chatId();
        chatStatusDao.upsertChatStatus(chatId, AWAITING_URL_TO_UNTRACK);
        telegramService.sendMessage(chatId, telegramMessages.getString("command.untrack.command"));
    }

    // TODO: remove inline url widgets from response messages.
    private void list(UpdateTo update) {
        trackingService.getTrackings(update);
    }
}
