package edu.java.bot.dao;

import edu.java.bot.domain.TrackingStatus;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Repository;
import static edu.java.bot.domain.TrackingStatus.IS_ALREADY_TRACKED;
import static edu.java.bot.domain.TrackingStatus.IS_NOT_TRACKED;
import static edu.java.bot.domain.TrackingStatus.TRACK_ADDED;
import static edu.java.bot.domain.TrackingStatus.TRACK_REMOVED;

@Repository
public class TrackingDao {

    // TODO: remove temporary solution.
    private final Map<Long, Set<String>> trackedUrls = new HashMap<>();

    public TrackingStatus addTracking(Long chatId, String url) {
        if (!trackedUrls.containsKey(chatId)) {
            trackedUrls.put(chatId, new HashSet<>());
        }

        Set<String> chatTrackedUrls = trackedUrls.get(chatId);
        if (chatTrackedUrls.contains(url)) {
            return IS_ALREADY_TRACKED;
        } else {
            chatTrackedUrls.add(url);
            return TRACK_ADDED;
        }
    }

    public TrackingStatus removeTracking(Long chatId, String url) {
        Set<String> chatTrackedUrls = trackedUrls.get(chatId);
        if (chatTrackedUrls == null || !chatTrackedUrls.contains(url)) {
            return IS_NOT_TRACKED;
        } else {
            chatTrackedUrls.remove(url);
            return TRACK_REMOVED;
        }
    }

    public Set<String> getTrackings(Long chatId) {
        return trackedUrls.get(chatId);
    }
}
