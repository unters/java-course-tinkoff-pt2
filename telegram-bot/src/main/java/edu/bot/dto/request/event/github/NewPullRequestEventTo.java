package edu.bot.dto.request.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.bot.domain.EventType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class NewPullRequestEventTo extends AbstractGitHubEventTo {

    private final String pullRequestTitle;
    private final String username;

    public NewPullRequestEventTo(EventType type, String user, String repository, String title, String username) {
        super(type, user, repository);
        this.pullRequestTitle = title;
        this.username = username;
    }
}
