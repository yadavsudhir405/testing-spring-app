package com.example.testingspringapp;

import com.example.testingspringapp.config.DbConfig;
import com.example.testingspringapp.config.DbConfigProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@PropertySource("classpath:db.properties") // This initialize Environment class with properties defined in it
@ConfigurationPropertiesScan
@Slf4j
public class TestingSpringAppApplication {


    public static void main(String[] args) {
        SpringApplication.run(TestingSpringAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(DbConfig dbConfig) {
        return args -> {
            log.debug("Url: {}", dbConfig.getUrl());
            log.debug("Username: {}", dbConfig.getUsername());
            log.debug("Password: {}", dbConfig.getPassword());
        };
    }

    @Bean
    CommandLineRunner commandLineDbConfigProperty(DbConfigProperty dbConfigProperty) {
        return args -> {
            log.debug("DbConfigProperty Url: {}", dbConfigProperty.getUrl());
            log.debug("DbConfigProperty Username: {}", dbConfigProperty.getUsername());
            log.debug("DbConfigProperty Password: {}", dbConfigProperty.getPassword());
        };
    }
}
