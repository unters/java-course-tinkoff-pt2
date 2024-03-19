package edu.scrapper.client.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record IssueTo(
    String title,
    UserTo user,
    String state,
    @Schema(description = "issue url")
    String htmlUrl,
    String closedAt,
    String updatedAt,
    String createdAt
) {
}
