package com.app.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleProductNotFound(ProductNotFoundException ex,
			HttpServletRequest request) {

		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
	}

	@ExceptionHandler(InvalidSearchParameterException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidSearchParam(InvalidSearchParameterException ex,
			HttpServletRequest request) {

		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

		return buildErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, request.getRequestURI(), errors);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
			HttpServletRequest request) {

		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getRequestURI());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

		return buildErrorResponse("Internal server error. Please contact support.", HttpStatus.INTERNAL_SERVER_ERROR,
				request.getRequestURI());
	}

	private ResponseEntity<ApiErrorResponse> buildErrorResponse(String message, HttpStatus status, String path) {

		ApiErrorResponse response = ApiErrorResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.error(status.getReasonPhrase()).message(message).path(path).build();

		return new ResponseEntity<>(response, status);
	}

	private ResponseEntity<ApiErrorResponse> buildErrorResponse(String message, HttpStatus status, String path,
			Map<String, String> validationErrors) {

		ApiErrorResponse response = ApiErrorResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.error(status.getReasonPhrase()).message(message).path(path).validationErrors(validationErrors).build();

		return new ResponseEntity<>(response, status);
	}

	@ExceptionHandler(QuerySyncException.class)
	public ResponseEntity<ApiErrorResponse> handleQuerySync(QuerySyncException ex, HttpServletRequest request) {

		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Query database synchronization failed", request);
	}

	@ExceptionHandler(InvalidProductException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidProduct(InvalidProductException ex,
			HttpServletRequest request) {

		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(CommandProductNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleCommandProductNotFound(CommandProductNotFoundException ex,
			HttpServletRequest request) {

		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}

	private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message,
			HttpServletRequest request) {

		ApiErrorResponse response = ApiErrorResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
				.error(status.getReasonPhrase()).message(message).path(request.getRequestURI()).build();

		return ResponseEntity.status(status).body(response);
	}

}
