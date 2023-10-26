package com.example.testingspringapp.config.batch.loadBook;


import com.example.testingspringapp.service.BookApiService;
import com.example.testingspringapp.config.batch.common.SimpleItemReader;
import com.example.testingspringapp.config.realDataBase.tenants.TenantAwareDatasource;
import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.entity.Book;
import com.example.testingspringapp.service.BookDataProvider;
import com.example.testingspringapp.service.IBookApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class LoadBookBatchJobConfig {

    @Bean
    public Job loadBookJob(JobRepository jobRepository) {
       return new JobBuilder("LoadBookJob", jobRepository)
               .listener(setContextListener())
               .start(loadBookStep(null,null))
               .build();
    }


    @Bean
    public Step loadBookStep(JobRepository jobRepository, PlatformTransactionManager globalTransactionManager) {
        return new StepBuilder("LoadBookStep", jobRepository)
                .<Book, Book>chunk(100, globalTransactionManager)
                .reader(bookItemReader(null))
                .writer(bookItemWriter(null))
                .build();
    }


    @Bean
    public JdbcBatchItemWriter<Book> bookItemWriter(TenantAwareDatasource dataSource) {
        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO Book (Title, Author, Price) VALUES ( :title, :author, :price)")
                .build();
    }

    @Bean
    @StepScope
    public SimpleItemReader<Book> bookItemReader(IBookApiService bookApiService) {
        return new SimpleItemReader<>("bookItemReader",new BookDataProvider(bookApiService));
    }

    @Bean
    public JobExecutionListener setContextListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("Job started: {}", jobExecution.getJobInstance().getJobName());
                TenantContext.setTenant(jobExecution.getJobParameters().getString("tenant"));
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("Job finished: {}", jobExecution.getJobInstance().getJobName());
                TenantContext.clearTenant();
            }
        };
    }
}
