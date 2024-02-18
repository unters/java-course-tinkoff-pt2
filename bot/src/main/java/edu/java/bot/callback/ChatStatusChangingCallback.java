package edu.java.bot.callback;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.dao.ChatStatusesDao;
import edu.java.bot.domain.ChatStatus;

import java.io.IOException;

public class ChatStatusChangingCallback implements Callback<SendMessage, SendResponse> {

    private final Long chatId;
    private final ChatStatus chatStatus;
    private final ChatStatusesDao chatStatusesDao;

    public ChatStatusChangingCallback(
            Long chatId,
            ChatStatus chatStatus,
            ChatStatusesDao chatStatusesDao
    ) {
        this.chatId = chatId;
        this.chatStatus = chatStatus;
        this.chatStatusesDao = chatStatusesDao;
    }

    @Override
    public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
        chatStatusesDao.upsertChatStatus(chatId, chatStatus);
    }

    @Override
    public void onFailure(SendMessage sendMessage, IOException e) {
        // TODO: add logic.
    }
}
