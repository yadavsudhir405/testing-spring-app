package com.example.testingspringapp.config;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class dbConfigTestUsingReflctionUtil {

    @Test
    void shouldReturnUsernameAndPassword() {
        final DbConfig dbConfig = new DbConfig(null);

        // Setting private field of dbConfig object
        ReflectionTestUtils.setField(dbConfig, "url", "localhost");
        ReflectionTestUtils.setField(dbConfig, "username", "admin");
        ReflectionTestUtils.setField(dbConfig, "password", "password");

        assertEquals("localhost", dbConfig.getUrl());
        assertEquals("admin", dbConfig.getUsername());
        assertEquals("password", dbConfig.getPassword());
    }

    @Nested
    @ExtendWith(MockitoExtension.class)
    class DependancySetting {

        @Mock
        Environment environment;

        @Test
        void test() throws Exception {
            when(this.environment.getProperty("url")).thenReturn("localhost");
            when(this.environment.getProperty("username")).thenReturn("admin");
            when(this.environment.getProperty("password")).thenReturn("password");

            final DbConfig dbConfig = new DbConfig(null);
            ReflectionTestUtils.setField(dbConfig, "environment", this.environment);
            dbConfig.afterPropertiesSet();


            assertEquals("localhost", dbConfig.getUrl());
            assertEquals("admin", dbConfig.getUsername());
            assertEquals("password", dbConfig.getPassword());
        }
    }

}
