package edu.scrapper.configuration;

import edu.scrapper.client.bot.BotClient;
import edu.scrapper.client.github.GitHubClient;
import edu.scrapper.client.stackoverflow.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class ClientConfig {

    private static final String UNSUPPORTED_RETRY_POLICY_TEMPLATE =
        "Unsupported retry policy: %s (should be one of [ fixed, exponential ]).";

    @Bean
    public BotClient botClient(
        @Value("${client.bot.url:http://localhost:8042}")
        String url
    ) {
        WebClient client = WebClient.builder()
            .baseUrl(url)
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(BotClient.class);
    }

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

    @Bean
    public RetryTemplate retryTemplate(
        @Value("${client.backoff.policy:fixed}")
        String backOffPolicyValue,
        @Value("${client.retry.max.attempts:3}")
        int maxAttempts,
        @Value("${client.retry.interval.in.seconds:1}")
        int intervalInSeconds,
        @Value("${client.retry.multiplier:3}")
        int multiplier
    ) {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts);
        retryTemplate.setRetryPolicy(retryPolicy);

        int intervalInMilliseconds = intervalInSeconds * 1000;
        BackOffPolicy backOffPolicy = switch (backOffPolicyValue) {
            case "fixed" -> {
                FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
                fixedBackOffPolicy.setBackOffPeriod(intervalInMilliseconds);
                yield fixedBackOffPolicy;
            }
            case "exponential" -> {
                ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
                exponentialBackOffPolicy.setInitialInterval(intervalInMilliseconds);
                exponentialBackOffPolicy.setMultiplier(multiplier);
                yield exponentialBackOffPolicy;
            }
            default -> {
                throw new IllegalArgumentException(UNSUPPORTED_RETRY_POLICY_TEMPLATE.formatted(backOffPolicyValue));
            }
        };
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
