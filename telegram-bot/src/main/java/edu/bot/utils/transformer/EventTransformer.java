package edu.bot.utils.transformer;

import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;

public interface EventTransformer {

    EventType suitableFor();

    String transformToMessage(AbstractEventTo eventTo);
}
