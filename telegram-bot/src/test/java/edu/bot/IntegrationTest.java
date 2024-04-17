package edu.bot;

import java.nio.file.Path;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTest {

    private static final String DATABASE = "tracking";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";

    public static PostgreSQLContainer<?> POSTGRES;
    public static DataSource dataSource;

    static {
        configurePostgres();
    }

    @AfterAll
    static void shutdown() {
        POSTGRES.stop();
    }

    private static void configurePostgres() {
        // Container
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName(DATABASE)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withReuse(true);
        runPostgresMigrations(POSTGRES);
        POSTGRES.start();

        // Datasource
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(POSTGRES.getJdbcUrl());
        driverManagerDataSource.setUsername(USERNAME);
        driverManagerDataSource.setPassword(PASSWORD);
        dataSource = driverManagerDataSource;
    }

    @SneakyThrows
    private static void runPostgresMigrations(JdbcDatabaseContainer<?> container) {
        Path initScriptPath = Path.of("database", "init.sql");
        // TODO: read all migration files into a single temporary file.
        container.withInitScript(initScriptPath.toString());
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("postgres.host", () -> {
            return "localhost:%s".formatted(POSTGRES.getMappedPort(5432));
        });
        registry.add("postgres.username", POSTGRES::getUsername);
        registry.add("postgres.username", POSTGRES::getPassword);
    }
}
