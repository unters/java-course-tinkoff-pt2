package edu.java.bot.consumer;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.service.UpdateProcessingService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/* TODO: move validation logic in UpdatesHandler constructor.  */
public class UpdatesConsumer {

    private static final Logger LOGGER = LogManager.getLogger(UpdatesConsumer.class);

    private final ExecutorService executorService;
    private final BlockingQueue<Update> updatesQueue;
    private final UpdateProcessingService updateProcessingService;

    public UpdatesConsumer(
        @Positive int corePoolSize,
        int maxPoolSize,
        @Positive long keepAliveTime,
        @NotNull TimeUnit unit,
        @NotNull BlockingQueue<Runnable> queue,
        @NotNull BlockingQueue<Update> updatesQueue,
        @NotNull UpdateProcessingService updateProcessingService
    ) {
        if (maxPoolSize < corePoolSize) {
            throw new IllegalArgumentException("maxPoolSize cannot be less than corePoolSize");
        }

        this.updatesQueue = updatesQueue;
        this.updateProcessingService = updateProcessingService;
        executorService = new ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            unit,
            queue
        );
    }

    @PostConstruct
    public void run() {
        executorService.execute(() -> {
            LOGGER.debug("UpdatesHandler.run() called.");
            while (true) {
                /* TODO: come up with better idea.  */
                while (updatesQueue.isEmpty()) {
                }

                Update update = updatesQueue.poll();
                LOGGER.info("Update %d retrieved from updatesQueue".formatted(update.hashCode()));
                executorService.execute(() -> updateProcessingService.processUpdate(update));
            }
        });
    }

    public static Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("MagicNumber")
    public static class Builder {
        private int corePoolSize = 6;
        private int maxPoolSize = 12;
        private long keepAliveTime = 60;
        private TimeUnit unit = TimeUnit.SECONDS;
        private BlockingQueue<Runnable> queue;
        private BlockingQueue<Update> updatesQueue;
        private UpdateProcessingService updateProcessingService;

        Builder() {
        }

        public Builder corePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public Builder maxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder keepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder unit(TimeUnit unit) {
            this.unit = unit;
            return this;
        }

        public Builder queue(BlockingQueue<Runnable> queue) {
            this.queue = queue;
            return this;
        }

        public Builder updatesQueue(BlockingQueue<Update> updatesQueue) {
            this.updatesQueue = updatesQueue;
            return this;
        }

        public Builder updateProcessingService(UpdateProcessingService updateProcessingService) {
            this.updateProcessingService = updateProcessingService;
            return this;
        }

        public UpdatesConsumer build() {
            return new UpdatesConsumer(
                this.corePoolSize,
                this.maxPoolSize,
                this.keepAliveTime,
                this.unit,
                this.queue,
                this.updatesQueue,
                this.updateProcessingService
            );
        }
    }
}
