package edu.scrapper.scheduling;

import edu.common.domain.EventType;
import edu.common.dto.event.AbstractEventTo;
import edu.common.dto.event.github.NewIssueEventTo;
import edu.common.dto.event.github.NewPullRequestCommentEventTo;
import edu.common.dto.event.github.NewPullRequestReviewEventTo;
import edu.common.dto.event.github.PullRequestMergedEventTo;
import edu.scrapper.client.bot.BotClient;
import edu.scrapper.client.github.GitHubClient;
import edu.scrapper.client.github.dto.CommentTo;
import edu.scrapper.client.github.dto.IssueTo;
import edu.scrapper.client.github.dto.ReviewTo;
import edu.scrapper.dao.TrackingDao;
import edu.scrapper.domain.EventGroup;
import edu.scrapper.domain.TrackingData;
import edu.scrapper.utils.LinkParser;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdateScheduler {

    private static final String USER = "user";
    private static final String REPOSITORY = "repo";
    private static final String PULL = "pull";

    private static final Integer PR_MERGED_STATUS_CODE = 204;

    private final TrackingDao trackingDao;
    private final GitHubClient gitHubClient;
    private final BotClient botClient;

    @Scheduled(fixedDelayString = "${scheduler.interval.in.seconds}", timeUnit = TimeUnit.SECONDS)
    public void update() {
        List<TrackingData> trackingData = trackingDao.findAllUrlsForUpdate();
        for (var entry : trackingData) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
            List<AbstractEventTo> events = getUpdates(entry.url(), entry.updatedAt());
            for (var event : events) {
                // TODO: add check for response code.
                botClient.sendEventData(entry.chatId(), event);
                trackingDao.updateTrackingTime(entry.chatId(), entry.url(), timestamp);
            }
        }
    }

    // TODO: add event type based transformers.
    @SuppressWarnings("MultipleStringLiterals")
    private List<AbstractEventTo> getUpdates(String url, Timestamp since) {
        Pair<Optional<EventGroup>, Map<String, String>> linkData = LinkParser.resolveLinkData(url);
        EventGroup eventGroup = linkData.getLeft()
            .orElseThrow(() -> new RuntimeException("Cannot resolve event group type: unsupported url"));
        Map<String, String> parameters = linkData.getRight();
        List<AbstractEventTo> events = new ArrayList<>();
        switch (eventGroup) {
            case GITHUB_REPOSITORY -> {
                List<IssueTo> issues = gitHubClient.getRepositoryIssues(
                    parameters.get(USER),
                    parameters.get(REPOSITORY),
                    since.toLocalDateTime().toString()
                );

                events.addAll(issues.stream()
                    .map(issueTo -> new NewIssueEventTo(
                        EventType.ISSUE,
                        parameters.get(USER),
                        parameters.get(REPOSITORY),
                        issueTo.title(),
                        issueTo.user().login(),
                        issueTo.htmlUrl()
                    ))
                    .collect(Collectors.toList()));
            }
            case GITHUB_PULL_REQUEST -> {
                List<CommentTo> comments = gitHubClient.getPullRequestComments(
                    parameters.get(USER),
                    parameters.get(REPOSITORY),
                    Integer.valueOf(parameters.get(PULL)),
                    since.toLocalDateTime().toString()
                );
                List<ReviewTo> reviews = gitHubClient.getPullRequestReviews(
                    parameters.get(USER),
                    parameters.get(REPOSITORY),
                    Integer.valueOf(parameters.get(PULL))
                );
                boolean hasBeenMerged = gitHubClient.checkIfPullRequestHasBeenMerged(
                    parameters.get(USER),
                    parameters.get(REPOSITORY),
                    Integer.valueOf(parameters.get(PULL))
                ).getStatusCode().equals(HttpStatusCode.valueOf(PR_MERGED_STATUS_CODE));

                List<AbstractEventTo> eventTos = new ArrayList<>();
                comments.stream()
                    .map(commentTo -> new NewPullRequestCommentEventTo(
                        EventType.PULL_COMMENT,
                        parameters.get(USER),
                        parameters.get(REPOSITORY),
                        "<comments dont have names - delete this functionality>",
                        commentTo.user().login(),
                        commentTo.pullRequestUrl()
                    ))
                    .forEach(events::add);
                reviews.stream()
                    .map(reviewTo -> new NewPullRequestReviewEventTo(
                        EventType.PULL_REVIEW,
                        parameters.get(USER),
                        parameters.get(REPOSITORY),
                        "<pr titles are not implemented yet>",
                        reviewTo.user().login(),
                        reviewTo.pullRequestUrl()
                    ))
                    .forEach(events::add);
                if (hasBeenMerged) {
                    events.add(new PullRequestMergedEventTo(
                        EventType.PULL_MERGED,
                        parameters.get(USER),
                        parameters.get(REPOSITORY),
                        "<pr titles are not implemented yet>"
                    ));
                }
            }
            default -> throw new RuntimeException("Unsupported event group.");
        }

        return events;
    }
}
