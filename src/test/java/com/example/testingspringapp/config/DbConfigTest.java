package com.example.testingspringapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DbConfigTest {
    @Test
    void shouldReturnUsernameAndPassword() throws Exception {
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("url", "localhost");
        environment.setProperty("username", "admin");
        environment.setProperty("password", "password");

        final DbConfig dbConfig = new DbConfig(environment);
        dbConfig.afterPropertiesSet();

        assertEquals("localhost", dbConfig.getUrl());
        assertEquals("admin", dbConfig.getUsername());
        assertEquals("password", dbConfig.getPassword());
    }

}
