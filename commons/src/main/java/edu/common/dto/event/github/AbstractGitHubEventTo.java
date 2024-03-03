package edu.common.dto.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
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
