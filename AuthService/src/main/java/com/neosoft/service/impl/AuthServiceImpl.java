package com.neosoft.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neosoft.dto.AuthRegisterDTO;
import com.neosoft.dto.AuthRequest;
import com.neosoft.dto.AuthResponse;
import com.neosoft.dto.RefreshTokenResponse;
import com.neosoft.entity.User;
import com.neosoft.exception.CustomAuthException;
import com.neosoft.exception.InvalidTokenException;
import com.neosoft.exception.UserExistException;
import com.neosoft.repository.AuthRepository;
import com.neosoft.security.JwtUtil;
import com.neosoft.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public AuthServiceImpl(AuthRepository repository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void registerUser(AuthRegisterDTO registerUser) {
	    String username = registerUser.username().trim();

	    if (repository.existsByUsernameIgnoreCase(username)) {
	        throw new UserExistException(
	                "User exists with this username: " + username,
	                HttpStatus.BAD_REQUEST
	        );
	    }

	    User user = User.builder()
	            .username(username)
	            .password(passwordEncoder.encode(registerUser.password()))
	            .email(registerUser.email().trim())
	            .build();

	    repository.save(user);
	}


	
	@Override
	public AuthResponse loginUser(AuthRequest loginUser) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginUser.username(), loginUser.password()));
		} catch (BadCredentialsException ex) {
			throw new CustomAuthException("Invalid username or password", HttpStatus.BAD_REQUEST);
		}

		return AuthResponse.builder()
				.accessToken(jwtUtil.generateAccessToken(loginUser.username()))
				.refreshToken(jwtUtil.generateRefreshToken(loginUser.username()))
				.expiresAt(jwtUtil.getAccessTokenExpiryTime()).build();

	}
	
	@Override
	public RefreshTokenResponse refreshAccessToken(String refreshToken) {

		if (!jwtUtil.isTokenValid(refreshToken)) {
			throw new InvalidTokenException("Invalid or expired refresh token", HttpStatus.BAD_REQUEST);
		}

		String username = jwtUtil.extractUsername(refreshToken);

		String newAccessToken = jwtUtil.generateAccessToken(username);

		return new RefreshTokenResponse(newAccessToken, jwtUtil.getAccessTokenExpiryTime());
	}

	

}
