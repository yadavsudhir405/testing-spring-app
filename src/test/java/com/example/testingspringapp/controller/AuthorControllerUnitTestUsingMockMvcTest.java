package com.example.testingspringapp.controller;

import com.example.testingspringapp.config.GlobalExceptionHandler;
import com.example.testingspringapp.entity.Author;
import com.example.testingspringapp.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringJUnitWebConfig(classes = {AuthorControllerUnitTestUsingMockMvcTest.TestConfig.class, GlobalExceptionHandler.class})
class AuthorControllerUnitTestUsingMockMvcTest {

    @Autowired
    AuthorRepository authorRepository;

    MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Nested
    class GetAllMethod {
        @Test
        void shouldReturn404WhenInvalidUrlPassed() throws Exception {
            mockMvc
                    // make http request
                    .perform(get("/authors1"))
                    .andDo(print()) // dump the results of request
                    // perform expectations
                    .andExpect(status().isNotFound());
        }

        @Test
        void shouldReturnErrorWhenQueryParameterNotSent() throws Exception {
            final MvcResult mvcResult = mockMvc
                    // make http request
                    .perform(get("/author"))
                    .andDo(print())
                    // perform expections
                    .andExpect(status().is4xxClientError()).andReturn();
            assertEquals("Required request parameter 'tenant' for method parameter type String is not present", mvcResult.getResolvedException().getMessage());
        }

        @Test
        void shouldReturnAllAuthors() throws Exception {
            when(authorRepository.findAll()).thenReturn(List.of(new Author(1, "Foo", 28)));
            mockMvc
                    // make http request
                    .perform(get("/author").param("tenant", "Tenant1"))
                    .andDo(print())
                    // perform expections
                    .andExpectAll(
                            status().isOk(),
                            content().json("""
                                    [{
                                        "id": 1,
                                        "name": "Foo",
                                        "age": 28
                                    }]
                                    """)
                    );
        }
    }


    @Nested
    class PostMethod {
        @Test
        void shouldThrowValidationErrorWhenIdPassedDuringSave() throws Exception {
            final String body = """
                    {
                        "id": 1,
                        "name": null,
                        "age": 29
                    }
                    """;

            mockMvc.perform(
                            post("/author")
                                    .param("tenant", "Tenant1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body)
                    )
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            content().json("""
                                    {"message":"id must be null while creating","httpStatus":"BAD_REQUEST"}
                                    """)

                    ).andReturn();


        }
    }

    @Configuration(proxyBeanMethods = false)
    @EnableWebMvc // This extremely important to declared. This adds necessary beans for MVC test
    static class TestConfig {

        @Bean
        public AuthorRepository authorRepository() {
            return Mockito.mock(AuthorRepository.class);
        }

        @Bean
        public AuthorController authorController(AuthorRepository authorRepository) {
            return new AuthorController(authorRepository);
        }

    }

}
