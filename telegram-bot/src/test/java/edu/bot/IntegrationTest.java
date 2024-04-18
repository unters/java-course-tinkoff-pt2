package edu.bot;

import edu.bot.client.scrapper.ScrapperClient;
import edu.bot.client.telegram.TelegramClient;
import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.junit.ClassRule;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTest {

    @ClassRule
    public static DockerComposeContainer environment =
        new DockerComposeContainer(new File("src/test/resources/fixture/docker-compose.yml"))
            .withStartupTimeout(Duration.of(300, ChronoUnit.SECONDS));

    @MockBean
    protected TelegramClient telegramClient;
    @MockBean
    protected ScrapperClient scrapperClient;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("client.telegram.token", () -> "noop");
    }
}
