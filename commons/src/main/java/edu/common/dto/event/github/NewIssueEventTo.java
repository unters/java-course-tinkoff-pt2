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
public class NewIssueEventTo extends AbstractGitHubEventTo {

    @Schema(description = "issue title")
    private final String title;
    @Schema(description = "issue author")
    private final String username;
    @Schema(description = "issue url")
    private final String url;

    public NewIssueEventTo(EventType type, String owner, String repository, String title, String username, String url) {
        super(type, owner, repository);
        this.title = title;
        this.username = username;
        this.url = url;
    }
}
