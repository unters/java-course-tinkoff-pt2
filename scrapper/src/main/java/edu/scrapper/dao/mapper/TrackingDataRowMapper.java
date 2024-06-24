package edu.scrapper.dao.mapper;

import edu.scrapper.domain.TrackingData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TrackingDataRowMapper implements RowMapper<TrackingData> {

    @Override
    public TrackingData mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TrackingData(rs.getLong("chat_id"), rs.getString("url"), rs.getTimestamp("updated_at"));
    }
}
