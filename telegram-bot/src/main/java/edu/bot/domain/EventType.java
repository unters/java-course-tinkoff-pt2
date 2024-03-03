package edu.bot.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("issue")
    ISSUE,

    @JsonProperty("pull")
    PULL,

    @JsonProperty("pull-comment")
    PULL_COMMENT,

    @JsonProperty("pull-review")
    PULL_REVIEW,

    @JsonProperty("pull-merged")
    PULL_MERGED,

    @JsonProperty("question-comment")
    QUESTION_COMMENT,

    @JsonProperty("question-answer")
    QUESTION_ANSWER,

    @JsonProperty("answer-comment")
    ANSWER_COMMENT
}
