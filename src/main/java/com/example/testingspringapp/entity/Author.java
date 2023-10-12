package com.example.testingspringapp.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table(name = "Author")
public record Author (Integer id, String name, Integer age) {
}
