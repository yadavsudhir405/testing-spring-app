package com.example.testingspringapp.config.realDataBase.tenants;


import com.example.testingspringapp.config.realDataBase.global.DbLoc;
import com.example.testingspringapp.config.realDataBase.global.DbLocRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class TenantDatabaseConfig implements InitializingBean {
   private final DbLocRepository dbLocRepository;
   private final TenantDatabaseHikariConfig tenantDatabaseHikariConfig;
   private final TenantAwareDatasource tenantAwareDatasource;


   @Bean
   public DataSource tenantDatasource() {
       return this.tenantAwareDatasource;
   }

  @Bean
  NamedParameterJdbcOperations namedParameterJdbcOperations(@Qualifier("tenantDatasource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
   }
   @Bean
   public JdbcTemplate tenantJdbcTemplate(@Qualifier("tenantDatasource") DataSource dataSource) {
       return new JdbcTemplate(dataSource);
   }

   @Bean
   public TransactionManager transactionManager(@Qualifier("tenantDatasource") DataSource dataSource) {
       return new DataSourceTransactionManager(dataSource);
   }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dbLocRepository.getAllActiveTenantDb().forEach(dbLoc -> {
            final DataSource dataSource = this.setupTenant(dbLoc);
            this.tenantAwareDatasource.addDataSource(dbLoc.getDbName(), dataSource);
        });
    }

    private DataSource setupTenant(DbLoc dbLoc) {
        DataSource dataSource = this.buildDataSource(dbLoc);
        this.runFlywayMigration(dataSource);
        return dataSource;
    }

    private void runFlywayMigration(DataSource dataSource) {
        final Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:db/migrations/tenant")
                .load();
        flyway.repair();
        flyway.migrate();

    }

    private DataSource buildDataSource(DbLoc dbLoc) {
        HikariConfig hikariConfig = this.buildHikariConfig(dbLoc);
        return new HikariDataSource(hikariConfig);
    }

    private HikariConfig buildHikariConfig(DbLoc dbLoc) {
        final HikariConfig hikariConfig = new HikariConfig();
        this.tenantDatabaseHikariConfig.copyStateTo(hikariConfig);
        final StringBuilder jdbcUrlBuilder = new StringBuilder("jdbc:sqlserver://")
                .append(dbLoc.getServerName())
                .append(":")
                .append(dbLoc.getPortNumber())
                .append(";")
                .append("databaseName=")
                .append(dbLoc.getDbName())
                .append(";trustServerCertificate=true");
        hikariConfig.setJdbcUrl(jdbcUrlBuilder.toString());
        hikariConfig.setPoolName(dbLoc.getDbName()+"-Datasource-Pool");
        return hikariConfig;
    }

}
