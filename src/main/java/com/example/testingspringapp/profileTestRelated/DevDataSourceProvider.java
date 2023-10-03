package com.example.testingspringapp.profileTestRelated;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("Dev")
public class DevDataSourceProvider implements DataSourceProvider{
    @Override
    public String getUrl() {
        return "devUrl";
    }

    @Override
    public String getUsername() {
        return "devUsername";
    }

    @Override
    public String getPassword() {
        return "devPassword";
    }
}
