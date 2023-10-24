package com.example.testingspringapp.repository;

import com.example.testingspringapp.entity.Author;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends ListCrudRepository<Author, Integer>, QueryByExampleExecutor<Author> {

    List<Author> findAllByName(String name);
}
