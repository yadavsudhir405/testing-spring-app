package com.example.testingspringapp.config.realDataBase.global;


import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.global.datasource")
public class GlobalDatabaseHikariConfig extends HikariConfig {

}
