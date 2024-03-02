package edu.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record OwnerTo (
    String displayName,
    String userType,
    String reputation
) {
}
