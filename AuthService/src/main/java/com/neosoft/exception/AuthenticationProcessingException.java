package com.neosoft.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class AuthenticationProcessingException extends RuntimeException {

	private final HttpStatus status;

	public AuthenticationProcessingException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
