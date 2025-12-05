package com.neosoft.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class CustomAuthException extends RuntimeException {

	private final HttpStatus httpStatus;

	public CustomAuthException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
