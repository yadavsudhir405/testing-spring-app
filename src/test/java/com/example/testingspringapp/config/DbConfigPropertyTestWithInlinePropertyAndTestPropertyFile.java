package com.example.testingspringapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(DbConfigProperty.class)
@EnableConfigurationProperties
@TestPropertySource(
        locations = "classpath:db.test.properties",
        properties = {"db.url= inlineLocalhost", "db.username=inlineUsername", "db.password=inlinePassword"}
)
/**
 * TestPropertySource always take precednece over the one deined in System.Property or defined in OS. However, has less
 * precedence compare to the defined by @DynamicPropertySource
 * Both db.test.proeprties and inline properties having url, username and password. Inline properties always takes precedence
 */
class DbConfigPropertyTestWithInlinePropertyAndTestPropertyFile {

    @Test
    void test1(@Autowired DbConfigProperty dbConfigProperty) {
        assertEquals("inlineLocalhost", dbConfigProperty.getUrl());
        assertEquals("inlineUsername", dbConfigProperty.getUsername());
        assertEquals("inlinePassword", dbConfigProperty.getPassword());
    }
}
