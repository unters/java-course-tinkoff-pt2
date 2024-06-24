package edu.common.dto.event.github;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Schema(description = "abstract repository related event")
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
abstract public class AbstractGitHubEventTo extends AbstractEventTo {

    @Schema(description = "username of repository owner")
    private final String owner;
    @Schema(description = "name of the repository")
    private final String repository;

    public AbstractGitHubEventTo(EventType type, String owner, String repository) {
        super(type);
        this.owner = owner;
        this.repository = repository;
    }
}
