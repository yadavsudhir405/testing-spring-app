package com.example.testingspringapp.service;

import com.example.testingspringapp.config.batch.common.StepDataProvider;
import com.example.testingspringapp.entity.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class BookDataProvider implements StepDataProvider<Book> {

    private final IBookApiService bookApiService;

    @Override
    public List<Book> getData() {
        log.info("Getting books from BookApiService");
        return this.bookApiService.getBooks();
    }
}
