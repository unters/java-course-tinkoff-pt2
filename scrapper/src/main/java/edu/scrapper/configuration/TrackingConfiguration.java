package edu.scrapper.configuration;

import edu.scrapper.dao.TrackingDao;
import edu.scrapper.service.JdbcTrackingService;
import edu.scrapper.service.JpaTrackingService;
import edu.scrapper.service.TrackingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TrackingConfiguration {

    private static final String INVALID_TRACKING_SERVICE_IMPLEMENTATION_MESSAGE =
        "Invalid tracking service implementation. Should be one of [ jpa, jdbc ], but was %s.";

    @Bean
    public TrackingService trackingService(
        @Value("${tracking.service.implementation}") String trackingServiceImplementation,
        TrackingDao trackingDao
    ) {
        if ("jpa".equals(trackingServiceImplementation)) {
            return new JpaTrackingService();
        }
        if ("jdbc".equals(trackingServiceImplementation)) {
            return new JdbcTrackingService(trackingDao);
        }

        throw new IllegalArgumentException(
            INVALID_TRACKING_SERVICE_IMPLEMENTATION_MESSAGE.formatted(trackingServiceImplementation)
        );
    }
}
