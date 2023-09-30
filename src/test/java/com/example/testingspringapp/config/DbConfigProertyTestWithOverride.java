package com.example.testingspringapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringJUnitConfig(DbConfigProperty.class)
@EnableConfigurationProperties
@TestPropertySource(properties = {"db.username=childUsername", "db.password=childPassword"})
public class DbConfigProertyTestWithOverride extends BaseProperty {

    @Test
    void test(@Autowired DbConfigProperty dbConfigProperty) {
        assertEquals("baseUrl", dbConfigProperty.getUrl());
        assertEquals("childUsername", dbConfigProperty.getUsername());
        assertEquals("childPassword", dbConfigProperty.getPassword());

    }
}

@TestPropertySource(properties = {"db.url= baseUrl","db.username=baseUsername"})
class BaseProperty {

}
