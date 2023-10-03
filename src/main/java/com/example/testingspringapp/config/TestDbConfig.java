package com.example.testingspringapp.config;

import org.springframework.context.annotation.Profile;

@Profile("Test")
public record TestDbConfig(String url, String username, String password){
 public TestDbConfig {
     if(url == null) url="testUrl";
     if(username == null) username="testUsername";
     if(password == null) password="testPassword";
 }
};
