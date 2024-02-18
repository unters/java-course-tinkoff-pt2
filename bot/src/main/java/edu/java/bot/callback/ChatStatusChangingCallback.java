package edu.java.bot.callback;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.domain.ChatStatus;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

public class ChatStatusChangingCallback implements Callback<SendMessage, SendResponse> {

    private final Long chatId;
    private final ChatStatus chatStatus;
    private ConcurrentMap<Long, ChatStatus> chatStatuses;

    public ChatStatusChangingCallback(
            Long chatId,
            ChatStatus chatStatus,
            ConcurrentMap<Long, ChatStatus> chatStatuses
    ) {
        this.chatId = chatId;
        this.chatStatus = chatStatus;
        this.chatStatuses = chatStatuses;
    }

    @Override
    public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
        chatStatuses.put(chatId, chatStatus);
    }

    @Override
    public void onFailure(SendMessage sendMessage, IOException e) {
        chatStatuses.put(chatId, chatStatus);
    }
}
