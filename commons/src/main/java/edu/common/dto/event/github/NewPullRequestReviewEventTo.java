package edu.common.dto.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.common.domain.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class NewPullRequestReviewEventTo extends AbstractGitHubEventTo {

    @Schema(description = "pull request title")
    private final String pullRequestTitle;
    @Schema(description = "review author")
    private final String username;
    @Schema(description = "pull request url")
    private final String url;

    public NewPullRequestReviewEventTo(
        EventType type,
        String user,
        String repository,
        String pullRequestTitle,
        String username,
        String url
    ) {
        super(type, user, repository);
        this.pullRequestTitle = pullRequestTitle;
        this.username = username;
        this.url = url;
    }
}
