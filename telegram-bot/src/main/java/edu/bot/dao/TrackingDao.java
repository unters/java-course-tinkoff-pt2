package edu.bot.dao;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TrackingDao {

    private static final String SQL_SELECT_ALL_CHAT_TRACKINGS =
        "SELECT url FROM tracking WHERE chat_id = :chat_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TrackingDao(
        DataSource dataSource
    ) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<String> getTrackings(Long chatId) {
        return jdbcTemplate.query(
            SQL_SELECT_ALL_CHAT_TRACKINGS,
            Map.of("chat_id", chatId),
            (resultSet, rowNum) -> resultSet.getString("url")
        );
    }
}
