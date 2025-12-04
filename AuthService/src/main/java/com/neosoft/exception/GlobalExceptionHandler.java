package com.neosoft.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.neosoft.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserExistException.class)
	public ResponseEntity<ErrorResponse> userNotFountException(
			UserExistException ex,
			HttpServletRequest request) {

		LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

		return ResponseEntity
				.status(ex.getStatus())
				.body(ErrorResponse.builder()
						.status(ex.getStatus().value())
						.errorMessage(ex.getMessage())
						.path(request.getRequestURI())
						.timestamp(dateTime)
						.build());
	}

	@ExceptionHandler(UserAuthenticationFailedException.class)
	public ResponseEntity<ErrorResponse> userAuthenticationFailedException(
			UserAuthenticationFailedException ex,
			HttpServletRequest request) {

		LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

		return ResponseEntity
				.status(ex.getStatus())
				.body(ErrorResponse.builder()
						.status(ex.getStatus().value())
						.errorMessage(ex.getMessage())
						.path(request.getRequestURI())
						.timestamp(dateTime)
						.build());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> fieldErrors = new HashMap<>();

		ex.getBindingResult()
		  .getFieldErrors()
		  .forEach(error ->
			  fieldErrors.put(error.getField(), error.getDefaultMessage())
		  );

		LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder()
						.status(HttpStatus.BAD_REQUEST.value())
						.errorMessage(fieldErrors.toString())
						.path(request.getRequestURI())
						.timestamp(dateTime)
						.build());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(
			ConstraintViolationException ex,
			HttpServletRequest request) {

		Map<String, String> violations = new HashMap<>();

		ex.getConstraintViolations().forEach(v ->
				violations.put(
						v.getPropertyPath().toString(),
						v.getMessage()
				));

		LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder()
						.status(HttpStatus.BAD_REQUEST.value())
						.errorMessage(violations.toString())
						.path(request.getRequestURI())
						.timestamp(dateTime)
						.build());
	}
	
	
	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<ErrorResponse> invalidJwtTokenException(
	        InvalidJwtTokenException ex,
	        HttpServletRequest request) {

	    LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

	    return ResponseEntity
	            .status(ex.getStatus())
	            .body(ErrorResponse.builder()
	                    .status(ex.getStatus().value())
	                    .errorMessage(ex.getMessage())
	                    .path(request.getRequestURI())
	                    .timestamp(dateTime)
	                    .build());
	}

	
	@ExceptionHandler(JwtTokenExpiredException.class)
	public ResponseEntity<ErrorResponse> jwtTokenExpiredException(
	        JwtTokenExpiredException ex,
	        HttpServletRequest request) {

	    LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

	    return ResponseEntity
	            .status(ex.getStatus())
	            .body(ErrorResponse.builder()
	                    .status(ex.getStatus().value())
	                    .errorMessage(ex.getMessage())
	                    .path(request.getRequestURI())
	                    .timestamp(dateTime)
	                    .build());
	}

	
	@ExceptionHandler(AuthenticationProcessingException.class)
	public ResponseEntity<ErrorResponse> authenticationProcessingException(
	        AuthenticationProcessingException ex,
	        HttpServletRequest request) {

	    LocalDateTime dateTime = LocalDateTime.now(ZoneId.systemDefault());

	    return ResponseEntity
	            .status(ex.getStatus())
	            .body(ErrorResponse.builder()
	                    .status(ex.getStatus().value())
	                    .errorMessage(ex.getMessage())
	                    .path(request.getRequestURI())
	                    .timestamp(dateTime)
	                    .build());
	}

}
