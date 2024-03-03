package edu.bot.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ClientConfig.class)
public class ApplicationConfig {
}

