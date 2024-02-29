package edu.scrapper.client.github.dto;

public record IssueTo(
    String title,
    UserTo user,
    String state,
    String closedAt,
    String updatedAt,
    String createdAt
) {
}
