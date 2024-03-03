package edu.common.dto.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.common.domain.EventType;
import edu.common.dto.event.github.NewIssueEventTo;
import edu.common.dto.event.github.NewPullRequestCommentEventTo;
import edu.common.dto.event.github.NewPullRequestEventTo;
import edu.common.dto.event.github.NewPullRequestReviewEventTo;
import edu.common.dto.event.github.PullRequestMergedEventTo;
import edu.common.dto.event.stackoverflow.NewAnswerCommentEventTo;
import edu.common.dto.event.stackoverflow.NewQuestionAnswerEventTo;
import edu.common.dto.event.stackoverflow.NewQuestionCommentEventTo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = NewIssueEventTo.class, name = "issue"),
    @JsonSubTypes.Type(value = NewPullRequestEventTo.class, name = "pull"),
    @JsonSubTypes.Type(value = NewPullRequestCommentEventTo.class, name = "pull-comment"),
    @JsonSubTypes.Type(value = NewPullRequestReviewEventTo.class, name = "pull-review"),
    @JsonSubTypes.Type(value = PullRequestMergedEventTo.class, name = "pull-merged"),
    @JsonSubTypes.Type(value = NewQuestionCommentEventTo.class, name = "question-comment"),
    @JsonSubTypes.Type(value = NewQuestionAnswerEventTo.class, name = "question-answer"),
    @JsonSubTypes.Type(value = NewAnswerCommentEventTo.class, name = "answer-comment")
})
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
abstract public class AbstractEventTo {

    @ApiModelProperty(
        value = "Event type",
        required = true,
        allowableValues =
            "issue, pull, pull-comment, pull-review, pull-merged, question-comment, question-answer, answer-comment"
    )
    private final EventType type;
}
