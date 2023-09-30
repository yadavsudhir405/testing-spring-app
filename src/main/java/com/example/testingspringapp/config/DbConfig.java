package com.example.testingspringapp.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Data
public class DbConfig implements InitializingBean {

    private final Environment environment;

    private String url;
    private String username;
    private String password;

    public DbConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.url = this.environment.getProperty("url");
        this.username = this.environment.getProperty("username");
        this.password = this.environment.getProperty("password");
    }
}
