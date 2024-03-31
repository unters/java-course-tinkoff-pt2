package edu.scrapper.dao;

import edu.scrapper.dao.mapper.TrackingDataRowMapper;
import edu.scrapper.domain.TrackingData;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class TrackingDao {

    private static final String SQL_ADD_TRACKING = "INSERT INTO tracking(url, chat_id) VALUES(:url, :chat_id)";
    private static final String SQL_REMOVE_TRACKING = "DELETE FROM tracking WHERE chat_id = :chat_id AND url = :url";
    private static final String SQL_UPDATE_TRACKING_TIME =
        "UPDATE tracking SET updated_at = :updated_at WHERE chat_id = :chat_id AND url = :url";
    private static final String SQL_FIND_URLS_FOR_UPDATE =
        "SELECT chat_id, url, updated_at FROM tracking";
    private static final String SQL_FIND_URLS_NOT_UPDATED_SINCE =
        "SELECT chat_id, url FROM tracking WHERE updated_at <= :not_updated_since";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TrackingDataRowMapper trackingDataRowMapper;

    public TrackingDao(
        DataSource dataSource,
        TrackingDataRowMapper trackingDataRowMapper
    ) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.trackingDataRowMapper = trackingDataRowMapper;
    }

    public void trackUrl(long chatId, String url) {
        jdbcTemplate.update(
            SQL_ADD_TRACKING,
            Map.of("url", url, "chat_id", chatId)
        );
    }

    public void untrackUrl(long chatId, String url) {
        jdbcTemplate.update(
            SQL_REMOVE_TRACKING,
            Map.of("url", url, "chat_id", chatId)
        );
    }

    public void updateTrackingTime(long chatId, String url, Timestamp timestamp) {
        jdbcTemplate.update(
            SQL_UPDATE_TRACKING_TIME,
            Map.of("updated_at", timestamp, "chat_id", chatId, "url", url)
        );
    }

    public List<TrackingData> findAllUrlsForUpdate() {
        return jdbcTemplate.query(
            SQL_FIND_URLS_FOR_UPDATE,
            trackingDataRowMapper
        );
    }

    public List<TrackingData> findUrlsNotUpdatedSince(Timestamp notUpdatedSinceTimestamp) {
        return jdbcTemplate.query(
            SQL_FIND_URLS_NOT_UPDATED_SINCE,
            Map.of("not_updated_since", notUpdatedSinceTimestamp),
            trackingDataRowMapper
        );
    }
}
