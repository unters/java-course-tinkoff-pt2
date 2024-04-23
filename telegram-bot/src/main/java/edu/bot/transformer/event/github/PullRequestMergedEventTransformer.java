package edu.bot.transformer.event.github;

import edu.bot.transformer.event.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.github.PullRequestMergedEventTo;
import static edu.common.domain.EventType.PULL_MERGED;

public class PullRequestMergedEventTransformer implements EventTransformer<PullRequestMergedEventTo> {

    private static final String MESSAGE_TEMPLATE = "Pull request *\"%s\"* has been merged.";

    @Override
    public EventType suitableFor() {
        return PULL_MERGED;
    }

    @Override
    public String transformToMessage(PullRequestMergedEventTo eventTo) {
        return MESSAGE_TEMPLATE.formatted(eventTo.getPullRequestTitle());
    }
}
