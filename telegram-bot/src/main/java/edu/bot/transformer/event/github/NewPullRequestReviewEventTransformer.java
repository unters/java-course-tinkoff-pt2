package edu.bot.transformer.event.github;

import edu.bot.transformer.event.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.github.NewPullRequestReviewEventTo;
import static edu.common.domain.EventType.PULL_REVIEW;

public class NewPullRequestReviewEventTransformer implements EventTransformer<NewPullRequestReviewEventTo> {

    private static final String MESSAGE_TEMPLATE =
        "New review by `%s` in pull request [\"%s\"](%s).";

    @Override
    public EventType suitableFor() {
        return PULL_REVIEW;
    }

    @Override
    public String transformToMessage(NewPullRequestReviewEventTo eventTo) {
        return MESSAGE_TEMPLATE.formatted(
            eventTo.getUsername(),
            eventTo.getUrl(),
            eventTo.getPullRequestTitle()
        );
    }
}
