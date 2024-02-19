package edu.java.bot.callback;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingCallback implements Callback<SendMessage, SendResponse> {

    private static final Logger LOGGER = LogManager.getLogger(LoggingCallback.class);

    @Override
    public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
        LOGGER.info("Message sent successfully: " + sendResponse.message());
    }

    @Override
    public void onFailure(SendMessage sendMessage, IOException e) {
        LOGGER.error("Message sent failed: " + e.getMessage());
    }
}
