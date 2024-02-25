package edu.java.scheduling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class LinkUpdateScheduler {

    private static final Logger LOGGER = LogManager.getLogger(LinkUpdateScheduler.class);

    @Scheduled(fixedDelayString = "${scheduler.interval.in.seconds}", timeUnit = TimeUnit.SECONDS)
    public void update() {
        LOGGER.info("Scheduling: update() called.");
    }
}
