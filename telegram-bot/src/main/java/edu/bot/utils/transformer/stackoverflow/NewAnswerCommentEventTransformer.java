package edu.bot.utils.transformer.stackoverflow;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.stackoverflow.NewAnswerCommentEventTo;
import static edu.common.domain.EventType.ANSWER_COMMENT;

public class NewAnswerCommentEventTransformer implements EventTransformer<NewAnswerCommentEventTo> {

    @Override
    public EventType suitableFor() {
        return ANSWER_COMMENT;
    }

    @Override
    public String transformToMessage(NewAnswerCommentEventTo eventTo) {
        return eventTo.toString();
    }
}
