package edu.scrapper.configuration;

import edu.scrapper.client.github.GitHubClient;
import edu.scrapper.client.stackoverflow.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ClientConfig {

    @Bean
    public GitHubClient gitHubClient(
        @Value("${client.github.url:https://api.github.com}")
        String url,
        @Value("${client.github.token}")
        String token
    ) {
        WebClient client = WebClient.builder()
            .baseUrl(url)
            .defaultHeader("Authorization", "Bearer %s".formatted(token))
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(GitHubClient.class);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        @Value("${client.stack-overflow.url:https://api.stackexchange.com/2.3?site=stackoverflow}")
        String url
    ) {
        WebClient client = WebClient.builder()
            .baseUrl(url)
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(StackOverflowClient.class);
    }
}
