package edu.bot.dao;

import edu.bot.domain.ChatStatus;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class ChatStatusDao {

    private static final String SQL_UPSERT_CHAT_STATUS =
        "INSERT INTO chat (id, status) VALUES(:id, :status) ON CONFLICT(id) DO UPDATE SET status = :status";
    private static final String SQL_GET_CHAT_STATUS =
        "SELECT status FROM chat WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ChatStatusDao(
        DataSource dataSource
    ) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void upsertChatStatus(Long chatId, ChatStatus chatStatus) {
        jdbcTemplate.update(
            SQL_UPSERT_CHAT_STATUS,
            Map.of("id", chatId, "status", chatStatus.toString().toLowerCase())
        );
    }

    public ChatStatus getChatStatus(Long chatId) {
        List<ChatStatus> chatStatusList = jdbcTemplate.query(
            SQL_GET_CHAT_STATUS,
            Map.of("id", chatId),
            (resultSet, rowNum) -> ChatStatus.valueOf(resultSet.getString("status").toUpperCase())
        );

        return chatStatusList.isEmpty() ? null : chatStatusList.getFirst();
    }
}
