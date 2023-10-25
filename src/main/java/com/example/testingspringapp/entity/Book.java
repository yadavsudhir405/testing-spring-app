package com.example.testingspringapp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;


@Table("Book")
public record Book(@Id Integer id, String title, String author, BigDecimal price) {
}
