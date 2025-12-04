package com.neosoft.service;

import com.neosoft.dto.AuthRequest;
import com.neosoft.dto.AuthResponse;
import com.neosoft.dto.RefreshTokenResponse;

import jakarta.validation.constraints.NotBlank;

import com.neosoft.dto.AuthRegisterDTO;

public interface AuthService {

	void registerUser(AuthRegisterDTO registerUser);

	AuthResponse loginUser(AuthRequest loginUser);

	RefreshTokenResponse refreshAccessToken(String refreshToken);

}
