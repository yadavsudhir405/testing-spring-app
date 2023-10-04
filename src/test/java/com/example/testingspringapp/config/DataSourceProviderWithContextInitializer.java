package com.example.testingspringapp.config;

import com.example.testingspringapp.profileTestRelated.DataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DefaultDataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DevDataSourceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Instead of multiple Initializer both Default and dev datasource resolver could have been configured in just one initializer. Two different Initializer have been created
 * just to show case that initializer can accepts multiple initializer classes
 */
@SpringJUnitConfig(initializers = {DataSourceProviderWithContextInitializer.DefaultDatasourceInitializer.class, DataSourceProviderWithContextInitializer.DevDatasourceInitializer.class})

public class DataSourceProviderWithContextInitializer {

    @Test
    void test(@Autowired @Qualifier("defaultDatasourceResolver") DataSourceProvider dataSourceProvider) {
        assertEquals("defaultUrl", dataSourceProvider.getUrl());
        assertEquals("defaultUsername", dataSourceProvider.getUsername());
        assertEquals("defaultPassword", dataSourceProvider.getPassword());
    }

    @Test
    void test1(@Autowired @Qualifier("devDatasourceResolver") DataSourceProvider dataSourceProvider) {
        assertEquals("devUrl", dataSourceProvider.getUrl());
        assertEquals("devUsername", dataSourceProvider.getUsername());
        assertEquals("devPassword", dataSourceProvider.getPassword());
    }

    static class DefaultDatasourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.getBeanFactory().registerSingleton("defaultDatasourceResolver", new DefaultDataSourceProvider());
        }
    }
    static class DevDatasourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.getBeanFactory().registerSingleton("devDatasourceResolver", new DevDataSourceProvider());
        }
    }

}
