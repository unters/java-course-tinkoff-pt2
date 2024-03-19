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
public class NewPullRequestCommentEventTo extends AbstractGitHubEventTo {

    @Schema(description = "title of the pull request")
    private final String pullRequestTitle;
    @Schema(description = "comment author")
    private final String username;
    @Schema(description = "pull request url")
    private final String url;

    public NewPullRequestCommentEventTo(
        EventType type,
        String owner,
        String repository,
        String title,
        String username,
        String url
    ) {
        super(type, owner, repository);
        this.pullRequestTitle = title;
        this.username = username;
        this.url = url;
    }
}
