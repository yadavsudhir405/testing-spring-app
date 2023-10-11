package com.example.testingspringapp.config.realDataBase.global;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class FlaywayConfig {
    static final String GLOBAL_MIGRATION_SCRIPTS_PATH = "classpath:db/migrations/global";
    @Bean(initMethod = "migrate")
    public Flyway globalDatabaseFlyway(@Autowired @Qualifier("globalDataSource") DataSource dataSource){

        log.info("Initializing Global Flyway");
        final Flyway flyway = Flyway.configure().dataSource(dataSource)
                .locations(GLOBAL_MIGRATION_SCRIPTS_PATH)
                .baselineOnMigrate(true)
                .outOfOrder(false)
                .load();
        flyway.repair();
        return flyway;
    }
}
