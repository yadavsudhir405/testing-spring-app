/**/package com.example.testingspringapp.profileTestRelated;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoadConfigWithProfileTest extends DefaultDataSourceProviderTest {
    @Test
    void shouldReturnDefaultDatasourceConfig(@Autowired DataSourceProvider dataSourceProvider) {
        assertEquals("defaultUrl", dataSourceProvider.getUrl());
        assertEquals("defaultUsername", dataSourceProvider.getUsername());
        assertEquals("defaultPassword", dataSourceProvider.getPassword());
    }

    @Nested
    @ActiveProfiles("Dev")
    class DevRelatedConfigurationTest {
        @Test
        void shouldReturnDevRelatedConfiguration(@Autowired @Qualifier("devDataSourceProvider") DataSourceProvider dataSourceProvider) {
            assertEquals("devUrl", dataSourceProvider.getUrl());
            assertEquals("devUsername", dataSourceProvider.getUsername());
            assertEquals("devPassword", dataSourceProvider.getPassword());

        }
    }
    @Nested
    @ActiveProfiles("Test")
    class TestRelatedConfigurationTest {
        @Test
        void shouldReturnDevRelatedConfiguration(@Autowired @Qualifier("testDataSourceProvider") DataSourceProvider dataSourceProvider) {
            assertEquals("testUrl", dataSourceProvider.getUrl());
            assertEquals("testUsername", dataSourceProvider.getUsername());
            assertEquals("testPassword", dataSourceProvider.getPassword());

        }
    }

    @Nested
    @ActiveProfiles("Dev")
    @NestedTestConfiguration(NestedTestConfiguration.EnclosingConfiguration.OVERRIDE) // This clears all inherited configuration from Outer class
    @SpringJUnitConfig(DevDataSourceProvider.class) // Specify current Configuration to use for test
    class Dev1RelatedConfigurationTest {
        @Test
        void shouldReturnDevRelatedConfiguration(@Autowired DataSourceProvider dataSourceProvider) {
            assertEquals("devUrl", dataSourceProvider.getUrl());
            assertEquals("devUsername", dataSourceProvider.getUsername());
            assertEquals("devPassword", dataSourceProvider.getPassword());

        }
    }
}
