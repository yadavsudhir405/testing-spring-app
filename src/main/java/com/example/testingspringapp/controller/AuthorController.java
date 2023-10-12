package com.example.testingspringapp.controller;


import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.entity.Author;
import com.example.testingspringapp.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping
    public List<Author> getAllAuthors(@RequestParam("tenant") String tenant) {
        TenantContext.setTenant(tenant);
        final List<Author> all = this.authorRepository.findAll();
        TenantContext.clearTenant();
        return all;
    }

    @GetMapping("{id}")
    public Author getAuthorDetail(@PathVariable("id") Integer id,
                                  @RequestParam("tenant") String tenant) {
        TenantContext.setTenant(tenant);
        final Example<Author> integerExample = Example.of(new Author(id, null, null));
        final Author author = this.authorRepository.findOne(integerExample).orElse(null);
        TenantContext.clearTenant();
        return author;
    }

    @PostMapping
    private Author save(@RequestBody Author author, @RequestParam("tenant") String tenant) {
        TenantContext.setTenant(tenant);
        final Author save = this.authorRepository.save(author);
        TenantContext.clearTenant();
        return save;
    }
}
