package com.app.command.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductCommand(

        @NotBlank(message = "Product name must not be empty")
        String name,

        String description,

        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        Double price,

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be >= 0")
        Integer stock
) {}
