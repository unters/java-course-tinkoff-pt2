package edu.bot.service;

import edu.bot.client.telegram.TelegramClient;
import edu.bot.client.telegram.dto.SendMessageTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TelegramService {

    private final TelegramClient telegramClient;

    public void sendMessage(Long chatId, String message) {
        telegramClient.sendMessage(new SendMessageTo(chatId, message));
    }
}
