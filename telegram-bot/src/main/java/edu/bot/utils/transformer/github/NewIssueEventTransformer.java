package edu.bot.utils.transformer.github;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
import static edu.common.domain.EventType.ISSUE;

public class NewIssueEventTransformer implements EventTransformer {

    @Override
    public EventType suitableFor() {
        return ISSUE;
    }

    @Override
    public String transformToMessage(AbstractEventTo eventTo) {
        return eventTo.toString();
    }
}
