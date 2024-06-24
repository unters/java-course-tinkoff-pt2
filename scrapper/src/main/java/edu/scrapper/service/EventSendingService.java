package edu.scrapper.service;

import edu.common.dto.event.AbstractEventTo;

public interface EventSendingService {

    void send(Long chatId, AbstractEventTo eventTo);
}
