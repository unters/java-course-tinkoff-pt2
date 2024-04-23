package edu.bot.transformer.event.github;

import edu.bot.transformer.event.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.github.NewPullRequestCommentEventTo;
import static edu.common.domain.EventType.PULL_COMMENT;

public class NewPullRequestCommentEventTransformer implements EventTransformer<NewPullRequestCommentEventTo> {

    private static final String MESSAGE_TEMPLATE = "New comment by `%s` in pull request [*]\"%s\"](%s).";

    @Override
    public EventType suitableFor() {
        return PULL_COMMENT;
    }

    @Override
    public String transformToMessage(NewPullRequestCommentEventTo eventTo) {
        return MESSAGE_TEMPLATE.formatted(
            eventTo.getUsername(),
            eventTo.getPullRequestTitle(),
            eventTo.getUrl()
        );
    }
}
