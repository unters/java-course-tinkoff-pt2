package edu.scrapper.client.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record PullTo(
    Integer id,
    String title,
    String state,
    UserTo user,
    @Schema(description = "pull request url")
    String htmlUrl,
    String closedAt,
    String updatedAt,
    String createdAt
) {
}
