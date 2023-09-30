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
            log.info("Url: {}", dbConfig.getUrl());
            log.info("Username: {}", dbConfig.getUsername());
            log.info("Password: {}", dbConfig.getPassword());
        };
    }

    @Bean
    CommandLineRunner commandLineDbConfigProperty(DbConfigProperty dbConfigProperty) {
        return args -> {
            log.info("DbConfigProperty Url: {}", dbConfigProperty.getUrl());
            log.info("DbConfigProperty Username: {}", dbConfigProperty.getUsername());
            log.info("DbConfigProperty Password: {}", dbConfigProperty.getPassword());
        };
    }
}
