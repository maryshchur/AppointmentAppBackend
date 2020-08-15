package org.example.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
@Configuration
public class DatabaseConfig {
    @Value("${spring.datasource.url}")
    private String postgresUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String postgresDriver;
    @Value("${spring.datasource.username}")
    private String postgresUser;
    @Value("${spring.datasource.password}")
    private String postgresPassword;

    @Primary
    @Bean
    public DataSource customDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresDriver);
        dataSource.setUrl(postgresUrl);
        dataSource.setUsername(postgresUser);
        dataSource.setPassword(postgresPassword);
        return dataSource;
    }
}
