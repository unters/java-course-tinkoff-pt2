package edu.bot.utils;

import edu.bot.dto.request.ChatTo;
import edu.bot.dto.request.MessageTo;
import edu.bot.dto.request.UpdateTo;
import java.util.Objects;

public class UpdateToBuilder {

    private static final String CHAT_TYPE = "chat";

    private static Long messageId = 1L;
    private static Long updateId = 1L;

    private Long chatId;
    private String message;

    public static UpdateToBuilder builder() {
        return new UpdateToBuilder();
    }

    public UpdateToBuilder setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public UpdateToBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public UpdateTo build() {
        // validate
        Objects.requireNonNull(chatId);
        Objects.requireNonNull(message);

        // build
        ChatTo chatTo = new ChatTo(chatId, CHAT_TYPE);
        MessageTo messageTo = new MessageTo(nextMessageId(), System.currentTimeMillis(), chatTo, message);
        return new UpdateTo(nextUpdateId(), messageTo);
    }

    private Long nextMessageId() {
        return messageId++;
    }

    private Long nextUpdateId() {
        return updateId++;
    }

    private UpdateToBuilder() {
    }
}
