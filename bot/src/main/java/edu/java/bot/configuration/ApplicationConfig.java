package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.consumer.UpdatesConsumer;
import edu.java.bot.domain.ChatStatus;
import edu.java.bot.service.CommandProcessingService;
import edu.java.bot.service.UpdateProcessingService;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfig {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public BlockingQueue<Update> updatesQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public UpdatesListener updatesListener(
            BlockingQueue<Update> updatesQueue
    ) {
        return new UpdatesListener() {

            private static final Logger LOGGER = LogManager.getLogger(UpdatesListener.class);

            @Override
            @SneakyThrows(InterruptedException.class)
            public int process(List<Update> updates) {
                for (Update update : updates) {
                    updatesQueue.put(update);
                    LOGGER.info("Received update %d: ".formatted(updates.hashCode()) + update);
                }

                return UpdatesListener.CONFIRMED_UPDATES_ALL;
            }
        };
    }


    @Bean
    public BlockingQueue<Runnable> queue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public UpdatesConsumer updatesHandler(
            BlockingQueue<Runnable> queue,
            BlockingQueue<Update> updatesQueue,
            UpdateProcessingService updateProcessingService
    ) {
        return UpdatesConsumer.builder()
                .queue(queue)
                .updatesQueue(updatesQueue)
                .updateProcessingService(updateProcessingService)
                .build();
    }

    @Bean
    public TelegramBot telegramBot(
            UpdatesListener updatesListener
    ) {
        TelegramBot bot = new TelegramBot(applicationProperties.telegramToken());
        bot.setUpdatesListener(updatesListener);
        return bot;
    }

    @Bean
    public ConcurrentMap<Long, ChatStatus> chatStatuses() {
        return new ConcurrentHashMap<>();
    }
}
