package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import edu.bot.dto.request.UpdateTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProcessingService {

    private final TelegramClient telegramClient;

    public void processUpdate(UpdateTo update) {
        echo(update);
    }

    public void echo(UpdateTo update) {
        SendMessageTo sendMessageTo = new SendMessageTo(
            update.message().chat().chatId(),
            update.message().text()
        );
        telegramClient.sendMessage(sendMessageTo);
    }
}
