package edu.scrapper.validation;

import edu.common.dto.tracking.TrackingDataTo;
import edu.scrapper.exception.TrackingDataValidationException;
import java.net.URI;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TrackingDataValidator {

    private static final String INVALID_CHAT_ID_MESSAGE = "chatId cannot be negative";
    private static final String UNSUPPORTED_URL_MESSAGE = "given url is not supported";

    public static void validate(TrackingDataTo trackingDataTo) {
        validate(trackingDataTo.getChatId());
        validate(trackingDataTo.getUrl());
    }

    private static void validate(Long chatId) {
        if (chatId < 0) {
            throw new TrackingDataValidationException(INVALID_CHAT_ID_MESSAGE);
        }
    }

    private static void validate(URI url) {
        // TODO.
    }
}
