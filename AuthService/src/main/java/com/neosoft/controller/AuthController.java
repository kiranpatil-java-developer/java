package com.neosoft.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neosoft.dto.AuthRegisterDTO;
import com.neosoft.dto.AuthRequest;
import com.neosoft.dto.AuthResponse;
import com.neosoft.dto.RefreshTokenRequest;
import com.neosoft.dto.RefreshTokenResponse;
import com.neosoft.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/api")
public class AuthController {

	private final AuthService userService;

	public AuthController(AuthService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<Object> registerUser(@Valid @RequestBody AuthRegisterDTO registerUser) {
		userService.registerUser(registerUser);
		return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody AuthRequest loginUser) {
		AuthResponse loggedUser = userService.loginUser(loginUser);
		return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<RefreshTokenResponse> refreshToken(
	        @Valid @RequestBody RefreshTokenRequest request) {
	    RefreshTokenResponse response = userService.refreshAccessToken(request.refreshToken());
	    return ResponseEntity.ok(response);
	}


}
