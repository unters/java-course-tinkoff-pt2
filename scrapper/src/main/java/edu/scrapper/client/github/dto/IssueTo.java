package edu.scrapper.client.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record IssueTo(
    String title,
    UserTo user,
    String state,
    String closedAt,
    String updatedAt,
    String createdAt
) {
}
