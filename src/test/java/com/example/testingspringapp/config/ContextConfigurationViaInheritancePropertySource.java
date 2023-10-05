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
 *
 *  When designing a Base config class for Test, Base class should be marked as Abstract
 *  ContextConfigurationViaInheritancePropertySource's test would fail when it declares that do not inherit location. This is because , Spring will
 *  not be able to resolve dependancy DbConfigProperty with the absence of it's dependent properties
 */


//@SpringJUnitConfig(inheritLocations = false) with this test will fail OR @TestPropertySource(inheritLocation = false) OR @TestPropertySource(inheritLocation = false)
public class ContextConfigurationViaInheritancePropertySource extends BaseTestConfigWithPropertyResource{

    @Test
    void test(@Autowired DbConfigProperty dbConfigProperty) {
        assertEquals("testUrl", dbConfigProperty.getUrl());
        assertEquals("testUsername", dbConfigProperty.getUsername());
        assertEquals("testPassword", dbConfigProperty.getPassword());
    }
}

@SpringJUnitConfig(value = DbConfigProperty.class)
@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:db.test.properties")
abstract class BaseTestConfigWithPropertyResource {

}
