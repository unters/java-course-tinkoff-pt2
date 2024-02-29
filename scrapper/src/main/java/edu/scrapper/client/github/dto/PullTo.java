package edu.scrapper.client.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record PullTo(
    Integer id,
    String title,
    String state,
    UserTo user,
    String closedAt,
    String updatedAt,
    String createdAt
) {
}
