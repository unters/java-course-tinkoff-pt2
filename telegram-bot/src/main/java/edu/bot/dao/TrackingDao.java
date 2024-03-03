package edu.bot.dao;

import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public class TrackingDao {

    public Set<String> getTrackings(Long chatId) {
        return Set.of();
    }
}
