package edu.scrapper.client.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record CommentTo(
    UserTo user,
    String updatedAt,
    String createdAt
) {
}
