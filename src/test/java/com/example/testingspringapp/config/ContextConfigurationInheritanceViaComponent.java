package com.example.testingspringapp.config;

import com.example.testingspringapp.profileTestRelated.DataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DefaultDataSourceProvider;
import com.example.testingspringapp.profileTestRelated.DevDataSourceProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ExtendedConfig overrides the configuration defined in Super class
 */
@SpringJUnitConfig(ExtendedConfig.class)
class ContextConfigurationInheritanceViaComponent extends BaseTest {
    @Test
    void test(@Autowired DataSourceProvider dataSourceProvider) {
        assertEquals("devUrl", dataSourceProvider.getUrl());
        assertEquals("devUsername", dataSourceProvider.getUsername());
        assertEquals("devPassword", dataSourceProvider.getPassword());

    }
}

@Configuration
class ExtendedConfig {
    @Bean
    public DataSourceProvider dataSourceProvider() {
        return new DevDataSourceProvider();
    }
}

@SpringJUnitConfig(BaseConfig.class)
abstract class BaseTest {

}


@Configuration
class BaseConfig {
    @Bean
    public DataSourceProvider dataSourceProvider() {
        return new DefaultDataSourceProvider();
    }
}
