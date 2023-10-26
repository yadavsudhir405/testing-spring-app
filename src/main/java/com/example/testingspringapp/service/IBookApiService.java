package com.example.testingspringapp.service;

import com.example.testingspringapp.entity.Book;

import java.util.List;

public interface IBookApiService {

    List<Book> getBooks();
}
