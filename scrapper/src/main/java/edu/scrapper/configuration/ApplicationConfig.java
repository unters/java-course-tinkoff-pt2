package edu.scrapper.configuration;

import edu.common.configuration.WebMvcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@Import({
    ClientConfig.class,
    PersistenceConfiguration.class,
    TrackingConfiguration.class,
    WebMvcConfiguration.class
})
public class ApplicationConfig {
}
