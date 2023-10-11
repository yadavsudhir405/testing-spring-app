package com.example.testingspringapp.config.realDataBase.global;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

@Configuration
public class GlobalDataBaseConfig {
    @Bean
    @Primary
    public DataSource globalDataSource(GlobalDatabaseHikariConfig globalHikariConfig) {
       return new HikariDataSource(globalHikariConfig);
    }

    @Bean
    @Primary
    public JdbcTemplate globalJdbcTemplate(@Autowired @Qualifier("globalDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    public TransactionManager globalTransactionManager(@Autowired @Qualifier("globalDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
