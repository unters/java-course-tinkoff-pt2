package edu.bot.utils.transformer;

import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;

public interface EventTransformer<E extends AbstractEventTo> {

    EventType suitableFor();

    String transformToMessage(E eventTo);
}
