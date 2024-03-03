package edu.bot.configuration;

import edu.bot.client.telegram.TelegramClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    @Bean
    public TelegramClient telegramClient(
        @Value("${client.telegram.url:https://api.telegram.org}")
        String url,
        @Value("${client.telegram.token}")
        String token
    ) {
        String authorizedUrl = "%s/bot%s".formatted(url, token);
        WebClient client = WebClient.builder()
            .baseUrl(authorizedUrl)
            .defaultHeader("Content-Type", "application/json")
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(TelegramClient.class);
    }
}
