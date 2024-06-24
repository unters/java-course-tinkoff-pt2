package edu.bot.configuration;

import edu.bot.utils.transformer.EventTransformer;
import edu.bot.utils.transformer.github.NewIssueEventTransformer;
import edu.bot.utils.transformer.github.NewPullRequestCommentEventTransformer;
import edu.bot.utils.transformer.github.NewPullRequestEventTransformer;
import edu.bot.utils.transformer.github.NewPullRequestReviewEventTransformer;
import edu.bot.utils.transformer.github.PullRequestMergedEventTransformer;
import edu.bot.utils.transformer.stackoverflow.NewAnswerCommentEventTransformer;
import edu.bot.utils.transformer.stackoverflow.NewQuestionAnswerEventTransformer;
import edu.bot.utils.transformer.stackoverflow.NewQuestionCommentEventTransformer;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ClientConfig.class, PersistenceConfiguration.class})
public class ApplicationConfig {

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

