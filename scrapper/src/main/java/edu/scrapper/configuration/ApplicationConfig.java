package edu.scrapper.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    ClientConfig.class,
    PersistenceConfiguration.class,
    TrackingConfiguration.class
})
public class ApplicationConfig {
}
