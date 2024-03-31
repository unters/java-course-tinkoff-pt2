package edu.scrapper.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@Import({
    ClientConfig.class,
    PersistenceConfiguration.class,
    TrackingConfiguration.class
})
public class ApplicationConfig {
}
