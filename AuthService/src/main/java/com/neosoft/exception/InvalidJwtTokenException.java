package com.neosoft.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class InvalidJwtTokenException extends RuntimeException {

	private final HttpStatus status;

	public InvalidJwtTokenException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
