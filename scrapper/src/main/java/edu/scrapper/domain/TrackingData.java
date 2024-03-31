package edu.scrapper.domain;

import java.sql.Timestamp;

public record TrackingData(Long chatId, String url, Timestamp updatedAt) {
}
