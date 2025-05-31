package com.ground.dogtor.global.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
            .create()
            .url("jdbc:mysql://localhost:3306/dogtor")
            .username("root")
            .password("1234")
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build();
    }
} 