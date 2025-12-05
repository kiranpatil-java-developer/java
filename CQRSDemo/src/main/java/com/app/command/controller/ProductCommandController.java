package com.app.command.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.command.dto.CreateProductCommand;
import com.app.command.dto.UpdateStockCommand;
import com.app.command.service.ProductCommandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commands/products")
@RequiredArgsConstructor
public class ProductCommandController {

	private final ProductCommandService service;

	@PostMapping
	public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CreateProductCommand cmd) {
		Map<String, Object> response = new HashMap<>();
		Long productId = service.createProduct(cmd);
		response.put("id", productId);
		response.put("message", "Successfully created");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}/stock")
	public ResponseEntity<Void> updateStock(@PathVariable Long id, @Valid @RequestBody UpdateStockCommand cmd) {
		service.updateStock(id, cmd.stock());
		Map<String, Object> response = new HashMap<>();
		response.put("updatedProductId", id);
		response.put("message", "Successfully updated stock");
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
		service.deleteProduct(id);
		Map<String, Object> response = new HashMap<>();
		response.put("deletedProductId", id);
		response.put("message", "Successfully deleted");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
