package edu.java.bot.dao;

import edu.java.bot.domain.ChatStatus;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static edu.java.bot.domain.ChatStatus.AWAITING_COMMAND;

@Repository
public class ChatStatusesDao {

    // TODO: remove temporary solution.
    private final ConcurrentMap<Long, ChatStatus> chatStatuses = new ConcurrentHashMap<>();

    public void upsertChatStatus(Long chatId, ChatStatus chatStatus) {
        chatStatuses.put(chatId, chatStatus);
    }

    public ChatStatus getChatStatus(Long chatId) {
        ChatStatus chatStatus = chatStatuses.get(chatId);
        return (chatStatus == null) ? AWAITING_COMMAND : chatStatus;
    }
}
