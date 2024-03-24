package edu.scrapper.dao;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class TrackingDao {

    private static final String SQL_ADD_TRACKING = "INSERT INTO link(url, chat_id) VALUES(:url, :chat_id)";
    private static final String SQL_REMOVE_TRACKING = "DELETE FROM link WHERE chat_id = :chat_id AND url = :url";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TrackingDao(
        DataSource dataSource
    ) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
}
