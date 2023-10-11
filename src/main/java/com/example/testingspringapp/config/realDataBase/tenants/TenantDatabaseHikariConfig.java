package com.example.testingspringapp.config.realDataBase.tenants;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.tenant.datasource")
public class TenantDatabaseHikariConfig extends HikariConfig {
}
