package com.example.testingspringapp.config;

import com.example.testingspringapp.profileTestRelated.DataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DefaultDataSourceProvider;
import com.example.testingspringapp.profileTestRelated.TestDataSourceProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
First run Outer Test with configuration specified inside @SpringJunitConfig. Spring initialize Test context with only those classes being specified in @SpringJunitConfig
 */
@SpringJUnitConfig(classes = {DefaultDataSourceProvider.class})
class DatasourceProviderTestContextLoadWithClass {

    @Test
    void test(@Autowired DataSourceProvider dataSourceProvider) {
        assertEquals("defaultUrl", dataSourceProvider.getUrl());
        assertEquals("defaultUsername", dataSourceProvider.getUsername());
        assertEquals("defaultPassword", dataSourceProvider.getPassword());
    }

    // When No configuration class is passed withing @SpringJunitConfig, Spring tries to load context from Nested static class
    @Nested
    @NestedTestConfiguration(NestedTestConfiguration.EnclosingConfiguration.OVERRIDE) // Clears out Test configuration being loaded from outer class
    @SpringJUnitConfig // configuration class is missing, so Spring tries to detect all Nested static classes having configuration.
    class WithNoConfigurationProvided {
        @Test
        void test1(@Autowired DataSourceProvider dataSourceProvider) {
            assertEquals("testUrl", dataSourceProvider.getUrl());
            assertEquals("testUsername", dataSourceProvider.getUsername());
            assertEquals("testPassword", dataSourceProvider.getPassword());
        }

        @Configuration
        static class TestConfig {
            @Bean
            DataSourceProvider dataSourceProvider() {
                return new TestDataSourceProvider();
            }
        }
    }
}
