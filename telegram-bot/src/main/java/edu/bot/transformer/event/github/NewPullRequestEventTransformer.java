package edu.bot.transformer.event.github;

import edu.bot.transformer.event.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.github.NewPullRequestEventTo;
import static edu.common.domain.EventType.PULL;

public class NewPullRequestEventTransformer implements EventTransformer<NewPullRequestEventTo> {

    private static final String MESSAGE_TEMPLATE =
        "New pull request [\"%s\"](%s) by `%s` in `%s`'s repository `%s`.";

    @Override
    public EventType suitableFor() {
        return PULL;
    }

    @Override
    public String transformToMessage(NewPullRequestEventTo eventTo) {
        return MESSAGE_TEMPLATE.formatted(
            eventTo.getPullRequestTitle(),
            eventTo.getUrl(),
            eventTo.getUsername(),
            eventTo.getOwner(),
            eventTo.getRepository()
        );
    }
}
