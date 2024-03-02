package edu.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AnswerTo(
    Long questionId,
    Long answerId,
    OwnerTo owner,
    Long creationDate
) {
}
