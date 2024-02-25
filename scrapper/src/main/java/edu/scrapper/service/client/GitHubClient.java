package edu.scrapper.service.client;

import edu.scrapper.dto.github.PullRequestCommentTo;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface GitHubClient {

    @GetExchange("/repos/{user}/{repo}/pulls/{pullNumber}/comments")
    List<PullRequestCommentTo> getPullRequestComments(
        @PathVariable("user")
        String user,
        @PathVariable("repo")
        String repository,
        @PathVariable("pullNumber")
        Integer pullNumber
    );
}
