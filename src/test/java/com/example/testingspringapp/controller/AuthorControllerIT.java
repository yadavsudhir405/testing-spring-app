package com.example.testingspringapp.controller;


import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.entity.Author;
import com.example.testingspringapp.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AuthorControllerIT extends AbstractTest {
    private static final String TENANT_DATABASE = "Tenant1";

    @BeforeEach
    void setTenant() {
        TenantContext.setTenant(TENANT_DATABASE);
    }

    @Test
    void shouldContainSeededData(@Autowired AuthorRepository authorRepository) {
        assertTrue(authorRepository.findAll().containsAll(List.of(new Author(1,"Foo",28), new Author(2, "Bar", 30))));
    }

    @Test
    void shouldSave(@Autowired AuthorRepository authorRepository) {
        final int currentSize = authorRepository.findAll().size();

        final Author author = new Author(null, "new", 55);
        final Author saved = authorRepository.save(author);

        assertNotNull(saved.id());

        assertEquals(currentSize + 1 , authorRepository.findAll().size());
    }


    @Nested
    @SpringJUnitWebConfig(WebTest.WebConfig.class)
    class WebTest {

        MockMvc mockMvc;

        @BeforeEach
        void setupMockMvc(@Autowired WebApplicationContext webApplicationContext) {
            this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }

        @Test
        void shouldSaveNewAuthor(@Autowired AuthorRepository authorRepository) throws Exception {

            final int oldAuthorWithJazzCount = authorRepository.findAllByName("Jazz").size();
            final String requestBody = """
                    {
                            "name": "Jazz",
                            "age": 16
                    }
                    """;
            this.mockMvc.perform(post("/author").param("tenant", "Tenant1").contentType(APPLICATION_JSON).content(requestBody))
                    .andExpectAll(status().isOk());
            TenantContext.setTenant(TENANT_DATABASE);

            final List<Author> authorByNames = authorRepository.findAllByName("Jazz");
            assertEquals(oldAuthorWithJazzCount + 1, authorRepository.findAllByName("Jazz").size());

        }

        @Configuration
        @EnableWebMvc
        @ComponentScan({
                "com.example.testingspringapp.controller",
        })
        static class WebConfig {

        }

    }




}
