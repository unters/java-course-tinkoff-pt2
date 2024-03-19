package edu.bot.utils.transformer.stackoverflow;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.stackoverflow.NewQuestionCommentEventTo;
import static edu.common.domain.EventType.QUESTION_COMMENT;

public class NewQuestionCommentEventTransformer implements EventTransformer<NewQuestionCommentEventTo> {

    @Override
    public EventType suitableFor() {
        return QUESTION_COMMENT;
    }

    @Override
    public String transformToMessage(NewQuestionCommentEventTo eventTo) {
        return eventTo.toString();
    }
}
