package edu.bot.configuration;

import edu.bot.transformer.event.EventTransformer;
import edu.bot.transformer.event.github.NewIssueEventTransformer;
import edu.bot.transformer.event.github.NewPullRequestCommentEventTransformer;
import edu.bot.transformer.event.github.NewPullRequestEventTransformer;
import edu.bot.transformer.event.github.NewPullRequestReviewEventTransformer;
import edu.bot.transformer.event.github.PullRequestMergedEventTransformer;
import edu.bot.transformer.event.stackoverflow.NewAnswerCommentEventTransformer;
import edu.bot.transformer.event.stackoverflow.NewQuestionAnswerEventTransformer;
import edu.bot.transformer.event.stackoverflow.NewQuestionCommentEventTransformer;
import edu.common.configuration.WebMvcConfiguration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
@Import(WebMvcConfiguration.class)
public class ApplicationConfig {

    @Bean
    public ResourceBundle telegramMessages() {
        return ResourceBundle.getBundle("message/telegram/messages", Locale.ROOT);
    }

    @Bean
    public List<EventTransformer> eventTransformers() {
        return List.of(
            new NewIssueEventTransformer(),
            new NewPullRequestCommentEventTransformer(),
            new NewPullRequestEventTransformer(),
            new NewPullRequestReviewEventTransformer(),
            new PullRequestMergedEventTransformer(),
            new NewAnswerCommentEventTransformer(),
            new NewQuestionAnswerEventTransformer(),
            new NewQuestionCommentEventTransformer()
        );
    }
}

