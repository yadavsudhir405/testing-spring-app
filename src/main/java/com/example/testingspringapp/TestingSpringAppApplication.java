package com.example.testingspringapp;

import com.example.testingspringapp.config.DbConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:db.properties")
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

}
