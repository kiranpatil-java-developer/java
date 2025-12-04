package com.neosoft.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class JwtTokenExpiredException extends RuntimeException {

	private final HttpStatus status;

	public JwtTokenExpiredException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
