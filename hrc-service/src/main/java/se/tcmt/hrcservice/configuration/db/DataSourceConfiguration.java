package se.tcmt.hrcservice.configuration.db;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

    private final String USERNAME = System.getenv("POSTGRES_USER");
    private final String PASSWORD = System.getenv("POSTGRES_PASSWORD");
    private final String URL = "jdbc:postgresql://" + System.getenv("POSTGRES_HOST") + ":5432/" + System.getenv("POSTGRES_DB");

    @Bean
    @ConfigurationProperties("app.datasource")
    public HikariDataSource hikariDataSource() {
        return DataSourceBuilder.create()
                .username(USERNAME)
                .password(PASSWORD)
                .url(URL)
                .type(HikariDataSource.class)
                .build();
    }
}
