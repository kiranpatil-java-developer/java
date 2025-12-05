package com.app.query.controller;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.app.query.entity.ProductQuery;
import com.app.query.service.ProductQueryService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/queries/products")
@RequiredArgsConstructor
public class ProductQueryController {

	private final ProductQueryService service;

	@GetMapping("/{id}")
	public ResponseEntity<ProductQuery> getById(
			@PathVariable @NotBlank(message = "Product ID must not be blank") String id) {

		return ResponseEntity.ok(service.getById(id));
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductQuery>> searchByName(
			@RequestParam @NotBlank(message = "Search name must not be blank") String name) {

		return ResponseEntity.ok(service.searchByName(name));
	}

	@GetMapping
	public ResponseEntity<Page<ProductQuery>> getAll(@RequestParam(defaultValue = "0") @Min(0) int pageNumber,
			@RequestParam(defaultValue = "10") @Min(1) int size) {

		Pageable pageable = PageRequest.of(pageNumber, size);
		return ResponseEntity.ok(service.getAll(pageable));
	}
}
