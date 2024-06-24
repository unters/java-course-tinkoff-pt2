package edu.bot.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class PersistenceConfiguration {

    private static final String POSTGRES_URL_PREFIX = "jdbc:postgresql";

    @Bean
    public DataSource dataSource(
        @Value("${postgres.host}")
        String host,
        @Value("${postgres.database:tracking}")
        String database,
        @Value("${postgres.username:postgres}")
        String username,
        @Value("${postgres.password:postgres}")
        String password
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("%s://%s/%s".formatted(POSTGRES_URL_PREFIX, host, database));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
