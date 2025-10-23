package zw.co.netone.ussdreportsanalyser.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zw.co.netone.ussdreportsanalyser.dto.SubscriberDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SubscriberRepository {
    private final JdbcTemplate selfCareJdbcTemplate;

    private static final String SUBSCRIBER_QUERY = "SELECT id,msisdn," +
            " passphrase, " +
            "pin," +
            " source," +
            " is_account_locked," +
            " status," +
            " attempts " +
            "FROM subscriber";


    private final RowMapper<SubscriberDto> rowMapper = new RowMapper<>() {
        @Override
        public SubscriberDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new SubscriberDto(
                    rs.getLong("id"),
                    rs.getString("msisdn"),
                    rs.getString("passphrase"),
                    rs.getString("pin"),
                    rs.getString("source"),
                    rs.getBoolean("is_account_locked"),
                    zw.co.netone.ussdreportsanalyser.enums.
                            RegistrationStatus.valueOf(rs.getString("status")),
                    rs.getInt("attempts")
            );
        }
    };

    public Optional<List<SubscriberDto>> findAll() {
        try {
            List<SubscriberDto> results = selfCareJdbcTemplate.query(SUBSCRIBER_QUERY+ "LIMIT 100 OFFSET 0", rowMapper);
            return Optional.of(results);
        } catch (Exception e) {
            log.error("Error fetching Subscriber Details: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<SubscriberDto> findByMsisdn(String msisdn) {
        String query = SUBSCRIBER_QUERY + " WHERE msisdn = ?";
        try {
            SubscriberDto result = selfCareJdbcTemplate.
                    queryForObject(query, new Object[]{msisdn}, rowMapper);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Error fetching Subscriber by MSISDN: {}", e.getMessage(), e);
            return Optional.empty();
        }

    }

    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM subscribers WHERE id = ?"; // Adjust table name and column as needed
        try {
            int rowsAffected = selfCareJdbcTemplate.update(deleteQuery, id);
            if (rowsAffected > 0) {
                log.info("Successfully deleted subscriber with id: {}", id);
            } else {
                log.warn("No subscriber found with id: {}", id);
            }
        } catch (Exception e) {
            log.error("Error deleting Subscriber with id: {}", e.getMessage(), e);
        }
    }
}
