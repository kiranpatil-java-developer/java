package com.app.exception;

public class CommandProductNotFoundException extends RuntimeException {
	public CommandProductNotFoundException(Long id) {
		super("Product not found with id: " + id);
	}
}
