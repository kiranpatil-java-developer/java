package com.app.query.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "r_product")
public class ProductQuery {

	@Id
	private String id;

	private String name;
	private String description;
	private Double price;
	private Integer stock;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
