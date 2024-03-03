package edu.bot.dto.request.event.stackoverflow;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.bot.domain.EventType;
import edu.bot.dto.request.event.AbstractEventTo;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class NewAnswerCommentEventTo extends AbstractEventTo {

    private final String questionTitle;
    private final String username;

    public NewAnswerCommentEventTo(EventType type, String questionTitle, String username) {
        super(type);
        this.questionTitle = questionTitle;
        this.username = username;
    }
}
