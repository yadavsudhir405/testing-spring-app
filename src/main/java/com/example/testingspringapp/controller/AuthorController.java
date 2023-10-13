package com.example.testingspringapp.controller;


import com.example.testingspringapp.config.realDataBase.tenants.TenantContext;
import com.example.testingspringapp.entity.Author;
import com.example.testingspringapp.repository.AuthorRepository;
import com.example.testingspringapp.validation.Creation;
import com.example.testingspringapp.validation.Updation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.validation.annotation.Validated;
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
    private Author save(@Validated(Creation.class) @RequestBody Author author, @RequestParam("tenant") String tenant) {
        TenantContext.setTenant(tenant);
        final Author save = this.authorRepository.save(author);
        TenantContext.clearTenant();
        return save;
    }


    @PutMapping("{id}")
    private Author update(@Validated(Updation.class) @RequestBody Author author, @RequestParam("tenant") String tenant) {
        TenantContext.setTenant(tenant);
        final Author save = this.authorRepository.save(author);
        TenantContext.clearTenant();
        return save;
    }


}
