package com.example.testingspringapp.entity;

import com.example.testingspringapp.validation.Creation;
import com.example.testingspringapp.validation.Updation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "Author")
public record Author (
        @NotNull(groups = Updation.class, message = "id is mandatory during update")
        @Null(groups = Creation.class, message = "id must be null while creating")
        Integer id,

        @NotBlank(message = "name must not be empty")
        String name,
        Integer age) {
}
