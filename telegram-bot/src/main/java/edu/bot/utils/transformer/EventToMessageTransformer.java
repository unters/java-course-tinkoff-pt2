package edu.bot.utils.transformer;

import edu.common.dto.event.AbstractEventTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventToMessageTransformer {

    public static String transformEvent(AbstractEventTo eventTo) {
        // TODO: here will be switch by type.
        return "Event received!";
    }
}
