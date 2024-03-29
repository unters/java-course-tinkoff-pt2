package edu.scrapper.client.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record ReviewTo(
    UserTo user,
    String body,
    String state,
    String updatedAt,
    String createdAt
) {
}
