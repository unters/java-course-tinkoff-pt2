package edu.bot.dao;

import edu.bot.domain.ChatStatus;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;
import static edu.bot.domain.ChatStatus.AWAITING_COMMAND;

@Repository
public class ChatStatusDao {

    private final ConcurrentMap<Long, ChatStatus> chatStatuses = new ConcurrentHashMap<>();

    public void upsertChatStatus(Long chatId, ChatStatus chatStatus) {
        chatStatuses.put(chatId, chatStatus);
    }

    public ChatStatus getChatStatus(Long chatId) {
        ChatStatus chatStatus = chatStatuses.get(chatId);
        return (chatStatus == null) ? AWAITING_COMMAND : chatStatus;
    }
}
