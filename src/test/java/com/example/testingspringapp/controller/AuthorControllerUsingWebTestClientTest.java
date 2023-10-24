package com.example.testingspringapp.controller;

import com.example.testingspringapp.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringJUnitWebConfig(AuthorControllerUsingWebTestClientTest.WebConfig.class)
public class AuthorControllerUsingWebTestClientTest extends AbstractTest{

    WebTestClient webTestClient;

    MockMvc mockMvc;

    @BeforeEach
    void setupWebTestClient(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        webTestClient = MockMvcWebTestClient.bindTo(mockMvc).build();
    }



    @Test
    void shouldGetSeededData() {

        this.webTestClient.get().uri("/author?tenant=Tenant1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Author.class).contains(new Author(1,"Foo",28), new Author(2,"Bar",30));

    }

    @Configuration
    @EnableWebMvc
    @ComponentScan({
            "com.example.testingspringapp.controller",
    })
    static class WebConfig {

    }

}
