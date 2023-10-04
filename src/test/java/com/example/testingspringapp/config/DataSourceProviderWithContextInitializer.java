package com.example.testingspringapp.config;

import com.example.testingspringapp.profileTestRelated.DataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DefaultDataSourceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(initializers = {DataSourceProviderWithContextInitializer.DefaultDatasourceInitializer.class})
public class DataSourceProviderWithContextInitializer {

    @Test
    void test(@Autowired DataSourceProvider dataSourceProvider) {
        assertEquals("defaultUrl", dataSourceProvider.getUrl());
        assertEquals("defaultUsername", dataSourceProvider.getUsername());
        assertEquals("defaultPassword", dataSourceProvider.getPassword());
    }

    static class DefaultDatasourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.getBeanFactory().registerSingleton("defaultDatasourceResolver", new DefaultDataSourceProvider());
        }
    }
}
