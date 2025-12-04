package com.neosoft.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ErrorResponse(
		int status,
        String errorMessage,
        String path,
        LocalDateTime timestamp) {

}
