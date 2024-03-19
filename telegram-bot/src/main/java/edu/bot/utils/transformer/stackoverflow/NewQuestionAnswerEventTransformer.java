package edu.bot.utils.transformer.stackoverflow;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.stackoverflow.NewQuestionAnswerEventTo;
import static edu.common.domain.EventType.QUESTION_ANSWER;

public class NewQuestionAnswerEventTransformer implements EventTransformer<NewQuestionAnswerEventTo> {

    @Override
    public EventType suitableFor() {
        return QUESTION_ANSWER;
    }

    @Override
    public String transformToMessage(NewQuestionAnswerEventTo eventTo) {
        return eventTo.toString();
    }
}
