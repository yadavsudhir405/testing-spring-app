package com.example.testingspringapp.config.realDataBase;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.SqlServerDialect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

/*

 */
@Configuration
@EnableJdbcRepositories(basePackages = {
        "com.example.testingspringapp.entity",
        "com.example.testingspringapp.repository"
})
public class SpringDataJdbcConfiguration extends AbstractJdbcConfiguration {

    @Override
    public Dialect jdbcDialect(NamedParameterJdbcOperations operations) {
        return SqlServerDialect.INSTANCE;
    }

}
