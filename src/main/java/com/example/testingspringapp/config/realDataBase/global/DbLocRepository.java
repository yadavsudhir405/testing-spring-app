package com.example.testingspringapp.config.realDataBase.global;


import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DependsOn("globalDatabaseFlyway")
public class DbLocRepository {

    private static final String GET_ALL_ACTIVE_TENANTS_DB_QUERY = """
            SELECT * FROM DB_Loc WHERE Status_ID = 1
            """;
    private final JdbcTemplate jdbcTemplate;

    public DbLocRepository(@Qualifier("globalJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DbLoc> getAllActiveTenantDb() {
       return this.jdbcTemplate.query(
              GET_ALL_ACTIVE_TENANTS_DB_QUERY,
               (rs, rowNum) -> {
                   final DbLoc dbLoc = new DbLoc();
                   dbLoc.setId(rs.getInt(1));
                   dbLoc.setDbName(rs.getString(2));
                   dbLoc.setServerName(rs.getString(3));
                   dbLoc.setInstance(rs.getString(4));
                   dbLoc.setPortNumber(rs.getInt(5));
                   dbLoc.setStatus(rs.getInt(6));
                   return dbLoc;
               }
       );
    }
}
