package com.example.testingspringapp.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

// This requires @EnableConfigurationProperty or @ConfigurationPropertiesScane to be added at main class
@ConfigurationProperties(prefix = "db")
@Data
public class DbConfigProperty {
    private String url;
    private String username;
    private String password;
}
