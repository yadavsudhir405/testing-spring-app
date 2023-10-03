package com.example.testingspringapp.profileTestRelated;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("Test")
public class TestDataSourceProvider implements DataSourceProvider{
    @Override
    public String getUrl() {
        return "testUrl";
    }

    @Override
    public String getUsername() {
        return "testUsername";
    }

    @Override
    public String getPassword() {
        return "testPassword";
    }
}
