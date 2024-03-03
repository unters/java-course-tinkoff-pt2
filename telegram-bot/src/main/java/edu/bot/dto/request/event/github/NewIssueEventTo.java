package edu.bot.dto.request.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.bot.domain.EventType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class NewIssueEventTo extends AbstractGitHubEventTo {

    private final String title;
    private final String username;

    public NewIssueEventTo(EventType type, String user, String repository, String title, String username) {
        super(type, user, repository);
        this.title = title;
        this.username = username;
    }
}
