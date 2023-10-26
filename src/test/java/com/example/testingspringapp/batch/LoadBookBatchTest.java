package com.example.testingspringapp.batch;

import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.controller.AbstractTest;
import com.example.testingspringapp.entity.Book;
import com.example.testingspringapp.repository.BookRepository;
import com.example.testingspringapp.service.BookApiService;
import com.example.testingspringapp.service.IBookApiService;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(classes = {LoadBookBatchTest.BatchTestConfig.class, LoadBookBatchTest.OverrideConfig.class})
@SpringBatchTest()
public class LoadBookBatchTest extends AbstractTest {

    private static final String TENANT = "Tenant1";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


    @Test
    void shouldLaunchJobAndInsertBook() throws Exception {
        TenantContext.setTenant(TENANT);
        this.bookRepository.deleteAll();

        final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
                .addString("tenant", TENANT)
                .addLocalDateTime("time", LocalDateTime.now());
        final JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParametersBuilder.toJobParameters());

        TenantContext.setTenant(TENANT);

        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
        assertEquals(1, this.bookRepository.count());
        TenantContext.clearTenant();
    }



    @Configuration
    @ComponentScan(basePackages = {
            "com.example.testingspringapp.config.batch.common",
            "com.example.testingspringapp.config.batch.loadBook",
            "com.example.testingspringapp.service",
    })
    static class BatchTestConfig {

        @Bean
        BookApiService bookApiService() {
            return new BookApiService();
        }

    }

    // Below Annotation @TestConfiguration overrides configuration done be uper BatchtestConfig. To be more precise, BatchTestConfig
    // has component scan for service package that will instantiate BookApiService instance of IBookApiService with 3 books.
    // Below configuration overrides BookApiService with custom IBookApiService.
    @TestConfiguration
    static class OverrideConfig {
        @Bean
        IBookApiService bookApiService() {
            return () -> List.of(
                    new Book(null, "Test", "test", BigDecimal.valueOf(89.56))
            );
        }
    }
}
