package edu.bot.client.telegram.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SendMessageTo(
    Long chatId,
    String text,
    String parseMode
) {

    public SendMessageTo(Long chatId, String text) {
        this(chatId, text, "Markdown");
    }
}
