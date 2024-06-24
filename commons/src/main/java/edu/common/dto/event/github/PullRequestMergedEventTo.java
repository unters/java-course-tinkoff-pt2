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
public class PullRequestMergedEventTo extends AbstractGitHubEventTo {

    @Schema(description = "pull request title")
    private final String pullRequestTitle;

    public PullRequestMergedEventTo(EventType type, String user, String repository, String pullRequestTitle) {
        super(type, user, repository);
        this.pullRequestTitle = pullRequestTitle;
    }
}
