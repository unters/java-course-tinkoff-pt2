package edu.common.dto.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.common.domain.EventType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class NewPullRequestCommentEventTo extends AbstractGitHubEventTo {

    private final String pullRequestTitle;
    private final String username;

    public NewPullRequestCommentEventTo(EventType type, String user, String repository, String title, String username) {
        super(type, user, repository);
        this.pullRequestTitle = title;
        this.username = username;
    }
}
