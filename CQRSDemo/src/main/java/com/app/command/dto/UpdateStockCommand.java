package com.app.command.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateStockCommand(

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock must be >= 0")
        Integer stock
) {}
