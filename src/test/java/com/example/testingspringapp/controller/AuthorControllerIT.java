package com.example.testingspringapp.controller;


import com.example.testingspringapp.config.realDataBase.global.GlobalDatabaseHikariConfig;
import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.config.realDataBase.tenants.TenantDatabaseHikariConfig;
import com.example.testingspringapp.entity.Author;
import com.example.testingspringapp.repository.AuthorRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringJUnitWebConfig(AuthorControllerIT.TestConfig.class)
public class AuthorControllerIT {

    private static final String GLOBAL_DATABASE = "TestingSpringAppGlobal";
    private static final String TENANT_DATABASE = "Tenant1";

    private static boolean IS_DB_INITIALIZED = false;

    @Container
    private static MSSQLServerContainer mssqlServerContainer = new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2022-latest")
            .acceptLicense();


    MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc(@Autowired WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
    public static void overrideDataSourceProperties(DynamicPropertyRegistry registry) throws SQLException {
        if(!IS_DB_INITIALIZED) {
            setupDb();
            setupTenantToGlobalDataBase();

            registry.add("spring.global.datasource.jdbcUrl",() -> mssqlServerContainer.getJdbcUrl()+";databaseName="+GLOBAL_DATABASE);
            registry.add("spring.global.datasource.username",() -> mssqlServerContainer.getUsername());
            registry.add("spring.global.datasource.password",() -> mssqlServerContainer.getPassword());

            registry.add("spring.tenant.datasource.jdbcUrl",() -> mssqlServerContainer.getJdbcUrl()+";databaseName="+TENANT_DATABASE);
            registry.add("spring.tenant.datasource.username",() -> mssqlServerContainer.getUsername());
            registry.add("spring.tenant.datasource.password",() -> mssqlServerContainer.getPassword());

            IS_DB_INITIALIZED = true;

        }
    }



    @BeforeEach
    void setTenant() {
        TenantContext.setTenant(TENANT_DATABASE);
    }

    @Test
    void shouldContainSeededData(@Autowired AuthorRepository authorRepository) {
        assertTrue(authorRepository.findAll().containsAll(List.of(new Author(1,"Foo",28), new Author(2, "Bar", 30))));
    }

    @Test
    void shouldSave(@Autowired AuthorRepository authorRepository) {
        final int currentSize = authorRepository.findAll().size();

        final Author author = new Author(null, "new", 55);
        final Author saved = authorRepository.save(author);

        assertNotNull(saved.id());

        assertEquals(currentSize + 1 , authorRepository.findAll().size());
    }

    @Test
    void shouldSaveNewAuthor(@Autowired AuthorRepository authorRepository) throws Exception {
        final String requestBody = """
                    {
                            "name": "Jazz",
                            "age": 16
                    }
                    """;
        this.mockMvc.perform(post("/author").param("tenant", "Tenant1").contentType(APPLICATION_JSON).content(requestBody))
                .andExpectAll(status().isOk());
        TenantContext.setTenant(TENANT_DATABASE);

        final Optional<Author> authorByName = authorRepository.findByName("Jazz");
        assertTrue(authorByName.isPresent());
    }


    @Configuration
    @ComponentScan({
            "com.example.testingspringapp.config.realDataBase",
            "com.example.testingspringapp.controller",
    })
    @TestPropertySource(locations = "classpath:/application-test.properties")
    @EnableConfigurationProperties({GlobalDatabaseHikariConfig.class, TenantDatabaseHikariConfig.class})
    @EnableWebMvc
    static class TestConfig {

    }

}
