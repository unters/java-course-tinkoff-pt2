package edu.bot.dto.request.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.bot.domain.EventType;
import edu.bot.dto.request.event.AbstractEventTo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
abstract public class AbstractGitHubEventTo extends AbstractEventTo {

    private final String user;
    private final String repository;

    public AbstractGitHubEventTo(EventType type, String user, String repository) {
        super(type);
        this.user = user;
        this.repository = repository;
    }
}
