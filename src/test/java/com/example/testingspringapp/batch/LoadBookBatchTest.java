package com.example.testingspringapp.batch;

import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.controller.AbstractTest;
import com.example.testingspringapp.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig(LoadBookBatchTest.BatchTestConfig.class)
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
        assertEquals(3, this.bookRepository.count());
        TenantContext.clearTenant();
    }



    @Configuration
    @ComponentScan(basePackages = {
            "com.example.testingspringapp.config.batch.common",
            "com.example.testingspringapp.config.batch.loadBook",
            "com.example.testingspringapp.service",
    })
    static class BatchTestConfig {

    }
}
