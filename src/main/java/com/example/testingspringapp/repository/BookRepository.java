package com.example.testingspringapp.repository;

import com.example.testingspringapp.entity.Book;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ListCrudRepository<Book, Integer>, QueryByExampleExecutor<Book> {

}
