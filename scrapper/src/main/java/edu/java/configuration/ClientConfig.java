package edu.java.configuration;

import edu.java.service.client.GitHubClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ClientConfig {

    @Bean
    public GitHubClient gitHubClient(
        @Value("${client.github.url:https://api.github.com}")
        String url
    ) {
        WebClient client = WebClient.builder()
            .baseUrl(url)
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(GitHubClient.class);
    }
}
