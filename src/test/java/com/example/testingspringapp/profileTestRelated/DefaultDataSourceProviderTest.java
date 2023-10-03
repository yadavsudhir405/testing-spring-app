package com.example.testingspringapp.profileTestRelated;

import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = {
        DefaultDataSourceProvider.class,
        DevDataSourceProvider.class,
        TestDataSourceProvider.class
})
abstract class DefaultDataSourceProviderTest {
}
