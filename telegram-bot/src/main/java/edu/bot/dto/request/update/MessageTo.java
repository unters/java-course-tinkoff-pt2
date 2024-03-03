package edu.bot.dto.request.update;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MessageTo(
    Long messageId,
    Long date,
    ChatTo chat,
    String text
) {
}
