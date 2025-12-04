package com.neosoft.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class UserAuthenticationFailedException extends RuntimeException {

	private final HttpStatus status;

	public UserAuthenticationFailedException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

}
