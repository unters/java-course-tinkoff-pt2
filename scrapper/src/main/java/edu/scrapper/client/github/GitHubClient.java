package edu.scrapper.client.github;

import edu.scrapper.client.github.dto.CommentTo;
import edu.scrapper.client.github.dto.IssueTo;
import edu.scrapper.client.github.dto.PullTo;
import edu.scrapper.client.github.dto.ReviewTo;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/repos/{user}/{repo}")
public interface GitHubClient {

    /**
     * List open issues in the repository.
     * @param lastUpdateTime timestamp in ISO-8601 format. If specified, only results that were last updated after
     *                       the given time will be returned.
     */
    @GetExchange("/issues")
    List<IssueTo> getRepositoryIssue(
        @PathVariable("user")
        String user,
        @PathVariable("repo")
        String repository,
        @RequestParam(required = false)
        String lastUpdateTime
    );

    /**
     * List open pull requests in the repository.
     */
    @GetExchange("/pulls")
    List<PullTo> getRepositoryPulls(
        @PathVariable("user")
        String user,
        @PathVariable("repo")
        String repository
    );

    /**
     * Get authors and creation dates of all comments in the pull request published after specified date.
     * @param lastUpdateTime timestamp in ISO-8601 format. If specified, only results that were last updated after
     *                       the given time will be returned.
     */
    @GetExchange("/pulls/{pullNumber}/comments")
    List<CommentTo> getPullRequestComments(
        @PathVariable("user")
        String user,
        @PathVariable("repo")
        String repository,
        @PathVariable("pullNumber")
        Integer pullNumber,
        @RequestParam(name = "since", required = false)
        String lastUpdateTime
    );

    /**
     * Get information about published reviews in specified pull request.
     */
    @GetExchange("/pulls/{pullNumber}/reviews")
    List<ReviewTo> getPullRequestReviews(
        @PathVariable("user")
        String user,
        @PathVariable("repo")
        String repository,
        @PathVariable("pullNumber")
        Integer pullNumber
    );

    /**
     * Check if pull request has been merged.
     */
    @GetExchange("/pulls/{pullNumber}/merge")
    void checkIfPullRequestHasBeenMerged(
        @PathVariable("user")
        String user,
        @PathVariable("repo")
        String repository,
        @PathVariable("pullNumber")
        Integer pullNumber
    );
}
