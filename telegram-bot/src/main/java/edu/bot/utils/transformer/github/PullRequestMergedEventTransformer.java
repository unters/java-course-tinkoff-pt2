package edu.bot.utils.transformer.github;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
import static edu.common.domain.EventType.PULL_MERGED;

public class PullRequestMergedEventTransformer implements EventTransformer {

    @Override
    public EventType suitableFor() {
        return PULL_MERGED;
    }

    @Override
    public String transformToMessage(AbstractEventTo eventTo) {
        return eventTo.toString();
    }
}
