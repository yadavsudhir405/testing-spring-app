package com.example.testingspringapp.config.batch.common;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing(dataSourceRef = "globalDataSource", transactionManagerRef = "globalTransactionManager")
public class JobConfig {
}
