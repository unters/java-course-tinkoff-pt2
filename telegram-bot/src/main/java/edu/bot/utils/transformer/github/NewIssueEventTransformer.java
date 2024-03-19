package edu.bot.utils.transformer.github;

import edu.bot.utils.transformer.EventTransformer;
import edu.common.domain.EventType;
import edu.common.dto.event.github.NewIssueEventTo;
import static edu.common.domain.EventType.ISSUE;

public class NewIssueEventTransformer implements EventTransformer<NewIssueEventTo> {

    private static final String MESSAGE_TEMPLATE = "New issue [\"%s\"](%s) by `%s` in `%s`'s repository `%s`.";

    @Override
    public EventType suitableFor() {
        return ISSUE;
    }

    @Override
    public String transformToMessage(NewIssueEventTo eventTo) {
        return MESSAGE_TEMPLATE.formatted(
            eventTo.getTitle(),
            eventTo.getUrl(),
            eventTo.getUsername(),
            eventTo.getOwner(),
            eventTo.getRepository()
        );
    }
}
