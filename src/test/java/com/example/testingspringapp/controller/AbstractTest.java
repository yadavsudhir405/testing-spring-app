package com.example.testingspringapp.controller;

import com.example.testingspringapp.config.realDataBase.global.GlobalDatabaseHikariConfig;
import com.example.testingspringapp.config.realDataBase.tenants.TenantDatabaseHikariConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.lifecycle.Startables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;


@SpringJUnitConfig(AbstractTest.TestConfig.class)
public abstract class AbstractTest {

    private static final String GLOBAL_DATABASE = "TestingSpringAppGlobal";
    private static final String TENANT_DATABASE = "Tenant1";

    protected static MSSQLServerContainer  mssqlServerContainer = (MSSQLServerContainer) new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2022-latest")
            .acceptLicense().withReuse(true);

   @BeforeAll
   static void initializeDb() throws SQLException {
       Startables.deepStart(Stream.of(mssqlServerContainer)).join();
       if (!isGlobalDatabaseAlreadyCreated()) {
           setupDb();
           setupTenantToGlobalDataBase();
       }

   }

   static boolean isGlobalDatabaseAlreadyCreated() throws SQLException {
       Connection connection = null;
       String sql = "SELECT 1 FROM sys.databases WHERE name = '" + GLOBAL_DATABASE + "'";
       try{
           connection = mssqlServerContainer.createConnection("");
           final ResultSet resultSet = connection.createStatement().executeQuery(sql);
           return resultSet.next();
       }catch (Exception e){
           return false;

       }finally {
           if (null != connection) {
               connection.close();
           }
       }
   }


   // Stop closing container when reuse container is needed

   @AfterAll
   static void shutDownContainer(){
//       mssqlServerContainer.close();
   }

    private static void setupDb() throws SQLException {
        runQuery("CREATE DATABASE " + GLOBAL_DATABASE);
        runQuery("CREATE DATABASE " + TENANT_DATABASE);
    }

    private static void runQuery(String sql) throws SQLException {
        Connection connection = null;
        try{
            connection = mssqlServerContainer.createConnection("");
            connection.prepareStatement(sql).execute();
        }catch (Exception e){

        }finally {
            if(null != connection) {
                connection.close();
            }
        }


    }

    private static void setupTenantToGlobalDataBase() throws SQLException {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mssqlServerContainer.getJdbcUrl()+";databaseName="+GLOBAL_DATABASE);
        hikariConfig.setUsername(mssqlServerContainer.getUsername());
        hikariConfig.setPassword(mssqlServerContainer.getPassword());

        final HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        hikariDataSource.getConnection().prepareStatement("""
         CREATE TABLE [dbo].[DB_Loc](
                                               [DB_Loc_ID] [int] IDENTITY(1,1) NOT NULL,
                                               [DB_Name] [nvarchar](150) NOT NULL,
                                               [Server_Name] [nvarchar](150) NOT NULL,
                                               [Server_Inst] [nvarchar](150) NULL,
                                               [Port_Number] [int] NULL,
                                               [Status_ID] [int] NOT NULL,
                                               [DB_Type_ID] [int] NOT NULL,
                                               [CreateDate] [smalldatetime] NOT NULL
                                                   CONSTRAINT [PK_DB_Loc] PRIMARY KEY CLUSTERED
                                                       (
                                                        [DB_Loc_ID] ASC
                                                           )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
                ) ON [PRIMARY]
        
            ALTER TABLE [dbo].[DB_Loc] ADD  CONSTRAINT [DF_DatabaseLocation_CreateDate]  DEFAULT (getdate()) FOR [CreateDate]
        
        """).execute();

        final PreparedStatement preparedStatement = hikariDataSource.getConnection().prepareStatement("""
                INSERT INTO dbo.DB_Loc(DB_Name, Server_Name, Server_Inst, Port_Number, Status_ID, DB_Type_ID) VALUES 
                (?, ?, ?, ?, ?, ?)
                    """);
        preparedStatement.setString(1,TENANT_DATABASE);
        preparedStatement.setString(2, mssqlServerContainer.getHost());
        preparedStatement.setString(3, "G3SQL01");
        preparedStatement.setInt(4, mssqlServerContainer.getMappedPort(1433));
        preparedStatement.setInt(5, 1);
        preparedStatement.setInt(6, 2);

        preparedStatement.execute();

    }

    @DynamicPropertySource
    static void overrideDataSourceProperties(DynamicPropertyRegistry registry) throws SQLException {
        registry.add("spring.global.datasource.jdbcUrl", () -> mssqlServerContainer.getJdbcUrl() + ";databaseName=" + GLOBAL_DATABASE);
        registry.add("spring.global.datasource.username", () -> mssqlServerContainer.getUsername());
        registry.add("spring.global.datasource.password", () -> mssqlServerContainer.getPassword());

        registry.add("spring.tenant.datasource.jdbcUrl", () -> mssqlServerContainer.getJdbcUrl() + ";databaseName=" + TENANT_DATABASE);
        registry.add("spring.tenant.datasource.username", () -> mssqlServerContainer.getUsername());
        registry.add("spring.tenant.datasource.password", () -> mssqlServerContainer.getPassword());

    }

    @Configuration
    @ComponentScan({
            "com.example.testingspringapp.config.realDataBase",
    })
    @TestPropertySource(locations = "classpath:/application-test.properties")
    @EnableConfigurationProperties({GlobalDatabaseHikariConfig.class, TenantDatabaseHikariConfig.class})
    static class TestConfig {

    }


}
