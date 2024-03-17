package edu.bot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ClientConfig.class, PersistenceConfiguration.class})
public class ApplicationConfig {
}

