package com.example.testingspringapp.config;

import com.example.testingspringapp.profileTestRelated.DataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DevDataSourceProvider;
import com.example.testingspringapp.profileTestRelated.TestDataSourceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ExtendedConfig overrides the configuration defined in Super class
 */
@SpringJUnitConfig(value = ExtendedConfig.class)
class ContextConfigurationInheritanceViaComponent extends BaseTest {
    @Test
    void test(@Autowired DataSourceProvider dataSourceProvider) {
        // datasource inherited from BaseTest is getting replaced by Extended.config class because both configuration
        // register bean with same name. If ExtendedConfig register bean with different name(just change method name from dataSourceProvider to say testDataSourceProvider ) then applicationContext will have
        // two instance of datasource resulting in error while injecting dataSourceProvider to test method.
        assertEquals("testUrl", dataSourceProvider.getUrl());
        assertEquals("testUsername", dataSourceProvider.getUsername());
        assertEquals("testPassword", dataSourceProvider.getPassword());

    }
}

@Configuration
class ExtendedConfig {
    @Bean
    public DataSourceProvider dataSourceProvider() {
        return new TestDataSourceProvider();
    }
}

@SpringJUnitConfig(DevDataSourceProvider.class)
@ActiveProfiles("Dev")
abstract class BaseTest {

}
