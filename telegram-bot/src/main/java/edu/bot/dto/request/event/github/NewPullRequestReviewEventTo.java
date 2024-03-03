package edu.bot.dto.request.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.bot.domain.EventType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class NewPullRequestReviewEventTo extends AbstractGitHubEventTo {

    private final String pullRequestTitle;
    private final String username;

    public NewPullRequestReviewEventTo(
        EventType type,
        String user,
        String repository,
        String pullRequestTitle,
        String username
    ) {
        super(type, user, repository);
        this.pullRequestTitle = pullRequestTitle;
        this.username = username;
    }
}
