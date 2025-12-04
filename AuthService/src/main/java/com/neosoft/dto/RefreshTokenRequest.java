package com.neosoft.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank(message = "Referesh Token Mandatory") String refreshToken) {

}
