package com.example.testingspringapp.profileTestRelated;

import org.springframework.stereotype.Component;

@Component
public class DefaultDataSourceProvider implements DataSourceProvider{
    @Override
    public String getUrl() {
        return "defaultUrl";
    }

    @Override
    public String getUsername() {
        return "defaultUsername";
    }

    @Override
    public String getPassword() {
        return "defaultPassword";
    }
}
