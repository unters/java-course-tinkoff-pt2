package edu.scrapper.utils;

import edu.scrapper.domain.EventGroup;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
public class LinkParser {

    private static final String OWNER = "owner";
    private static final String REPO = "repo";
    private static final String PULL = "pull";

    private static final String GITHUB_REPOSITORY_REGEX =
        "https:\\/\\/github.com\\/(?<" + OWNER + ">[a-zA-Z0-9-_]{1,})\\/(?<" + REPO + ">[a-zA-Z0-9-_]{1,})";
    private static final String GITHUB_PULL_REGEX =
        GITHUB_REPOSITORY_REGEX + "\\/pull\\/(?<" + PULL + ">[0-9]{1,})";


    private static final Pattern GITHUB_REPOSITORY_PATTERN = Pattern.compile(GITHUB_REPOSITORY_REGEX);
    private static final Pattern GITHUB_PULL_PATTERN = Pattern.compile(GITHUB_PULL_REGEX);

    public static Pair<Optional<EventGroup>, Map<String, String>> resolveLinkData(String url) {
        Matcher matcher = GITHUB_REPOSITORY_PATTERN.matcher(url);
        if (matcher.matches()) {
            return Pair.of(
                Optional.of(EventGroup.GITHUB_REPOSITORY),
                Map.of(OWNER, matcher.group(OWNER), REPO, matcher.group(REPO))
            );
        }

        if (GITHUB_PULL_PATTERN.matcher(url).matches()) {
            return Pair.of(
                Optional.of(EventGroup.GITHUB_PULL_REQUEST),
                Map.of(OWNER, matcher.group(OWNER), REPO, matcher.group(REPO), PULL, matcher.group(PULL))
            );
        }

        return Pair.of(Optional.empty(), Map.of());
    }
}
