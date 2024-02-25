package edu.java.service.client;

import edu.java.dto.github.PullRequestCommentTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import java.util.List;

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
