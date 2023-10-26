package com.example.testingspringapp.service;

import com.example.testingspringapp.entity.Book;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookApiService implements IBookApiService {
    // method that returns list of books
    @Override
    public List<Book> getBooks() {
        return List.of(
                new Book(null, "The Lord of the Rings", "J. R. R. Tolkien", BigDecimal.valueOf(9.99)),
                new Book(null, "The Hobbit", "J. R. R. Tolkien", BigDecimal.valueOf(13.99)),
                new Book(null, "The Silmarillion", "J. R. R. Tolkien", BigDecimal.valueOf(12.99))
        );
    }

}
