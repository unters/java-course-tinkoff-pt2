package edu.bot.configuration;

import edu.bot.client.scrapper.ScrapperClient;
import edu.bot.client.telegram.TelegramClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@SuppressWarnings("MultipleStringLiterals")
public class ClientConfig {

    private static final String UNSUPPORTED_RETRY_POLICY_TEMPLATE =
        "Unsupported retry policy: %s (should be one of [ fixed, exponential ]).";

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

    @Bean
    public ScrapperClient scrapperClient(
        @Value("${client.scrapper.url}")
        String url
    ) {
        WebClient client = WebClient.builder()
            .baseUrl(url)
            .defaultHeader("Content-Type", "application/json")
            .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(client)).build();
        return factory.createClient(ScrapperClient.class);
    }

    @Bean
    @SuppressWarnings("MagicNumber")
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
