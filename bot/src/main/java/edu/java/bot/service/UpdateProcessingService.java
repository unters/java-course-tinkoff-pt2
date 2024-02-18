package edu.java.bot.service;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.dao.ChatStatusesDao;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class UpdateProcessingService {

    private static final Logger LOGGER = LogManager.getLogger(CommandProcessingService.class);

    private final CommandProcessingService commandProcessingService;
    private final TrackingService trackingService;
    private final ChatStatusesDao chatStatusesDao;

    public UpdateProcessingService(
            CommandProcessingService commandProcessingService,
            TrackingService trackingService,
            ChatStatusesDao chatStatusesDao
    ) {
        this.commandProcessingService = commandProcessingService;
        this.trackingService = trackingService;
        this.chatStatusesDao = chatStatusesDao;
    }

    public void processUpdate(@NotNull Update update) {
        Long chatId = update.message().from().id();
        switch (chatStatusesDao.getChatStatus(chatId)) {
            case AWAITING_COMMAND -> commandProcessingService.processCommand(update);
            case AWAITING_URL_TO_TRACK -> trackingService.trackUrl(update);
            case AWAITING_URL_TO_UNTRACK -> trackingService.untrackUrl(update);
        }
    }
}
