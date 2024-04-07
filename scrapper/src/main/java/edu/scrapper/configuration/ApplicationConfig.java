package edu.scrapper.configuration;

import edu.common.configuration.WebMvcConfiguration;
import edu.common.dto.event.AbstractEventTo;
import edu.scrapper.client.bot.BotClient;
import edu.scrapper.dao.TrackingDao;
import edu.scrapper.jpa.HibernateSessionFactoryUtil;
import edu.scrapper.service.EventSendingService;
import edu.scrapper.service.TrackingService;
import edu.scrapper.service.event.QueueProducerEventSendingService;
import edu.scrapper.service.event.WebClientEventSendingService;
import edu.scrapper.service.tracking.JdbcTrackingService;
import edu.scrapper.service.tracking.JpaTrackingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@Import({
    ClientConfig.class,
    PersistenceConfiguration.class,
    WebMvcConfiguration.class
})
public class ApplicationConfig {

    private static final String INVALID_TRACKING_SERVICE_IMPLEMENTATION_MESSAGE =
        "Invalid tracking service implementation. Should be one of [ jpa, jdbc ], but was %s.";

    @Bean
    public TrackingService trackingService(
        @Value("${tracking.service.implementation}") String trackingServiceImplementation,
        HibernateSessionFactoryUtil hibernateSessionFactoryUtil,
        TrackingDao trackingDao
    ) {
        if ("jpa".equals(trackingServiceImplementation)) {
            return new JpaTrackingService(hibernateSessionFactoryUtil);
        }
        if ("jdbc".equals(trackingServiceImplementation)) {
            return new JdbcTrackingService(trackingDao);
        }

        throw new IllegalArgumentException(
            INVALID_TRACKING_SERVICE_IMPLEMENTATION_MESSAGE.formatted(trackingServiceImplementation)
        );
    }

    @Bean
    public EventSendingService eventSendingService(
        @Value("${event.sending.use.queue}")
        boolean useQueue,
        @Value("${kafka.topic.name}")
        String topicName,
        KafkaTemplate<String, AbstractEventTo> kafkaTemplate,
        BotClient botClient
    ) {
        if (useQueue) {
            return new QueueProducerEventSendingService(kafkaTemplate, topicName);
        }

        return new WebClientEventSendingService(botClient);
    }
}
