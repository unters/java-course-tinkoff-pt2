package edu.scrapper.client.github.dto;

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
