package com.example.testingspringapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(DbConfigProperty.class)
@EnableConfigurationProperties // This is needed because @ConfigurationProeprty works with @EnableConfigurationProperty
@TestPropertySource(locations = "classpath:db.test.properties")
class DbConfigPropertyTestWithPropertyFIle {

    @Test
    void test1(@Autowired DbConfigProperty dbConfigProperty) {
        assertEquals("testUrl", dbConfigProperty.getUrl());
        assertEquals("testUsername", dbConfigProperty.getUsername());
        assertEquals("testPassword", dbConfigProperty.getPassword());
    }
}
