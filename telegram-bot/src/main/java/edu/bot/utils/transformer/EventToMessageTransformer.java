package edu.bot.utils.transformer;

import edu.bot.dto.request.event.AbstractEventTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EventToMessageTransformer {

    public static String transformEvent(AbstractEventTo eventTo) {
        // TODO: here will be switch by type.
        return "Event received!";
    }
}
