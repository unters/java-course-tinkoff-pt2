package edu.scrapper.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PullRequestCommentTo {

    private final Integer id;
    private final String body;
    private final UserTo user;

    private final String updatedAt;
    private final String createdAt;

    public PullRequestCommentTo(
        Integer id,
        String body,
        UserTo user,
        @JsonProperty("updated_at")
        String updatedAt,
        @JsonProperty("created_at")
        String createdAt
    ) {
        this.id = id;
        this.body = body;
        this.user = user;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
