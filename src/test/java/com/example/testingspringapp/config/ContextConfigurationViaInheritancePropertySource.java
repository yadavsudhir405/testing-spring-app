package com.example.testingspringapp.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Since this class is extending a class having required annotation(@SpringJunitConfig) to run test it doesn't need further declare those
 * annotation to run the test.
 */
public class ContextConfigurationViaInheritancePropertySource extends BaseTestConfigWithPropertyResource{

    @Test
    void test(@Autowired DbConfigProperty dbConfigProperty) {
        assertEquals("testUrl", dbConfigProperty.getUrl());
        assertEquals("testUsername", dbConfigProperty.getUsername());
        assertEquals("testPassword", dbConfigProperty.getPassword());
    }
}

@SpringJUnitConfig(DbConfigProperty.class)
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:db.test.properties")
class BaseTestConfigWithPropertyResource {

}
