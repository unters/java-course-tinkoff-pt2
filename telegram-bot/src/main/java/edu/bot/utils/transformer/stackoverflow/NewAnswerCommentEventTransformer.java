package edu.bot.utils.transformer.stackoverflow;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
import static edu.common.domain.EventType.ANSWER_COMMENT;

public class NewAnswerCommentEventTransformer implements EventTransformer {

    @Override
    public EventType suitableFor() {
        return ANSWER_COMMENT;
    }

    @Override
    public String transformToMessage(AbstractEventTo eventTo) {
        return eventTo.toString();
    }
}
