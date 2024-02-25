package edu.java.configuration;

import edu.java.service.client.GitHubClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ClientConfig.class)
public class ApplicationConfig {
}
