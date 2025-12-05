package com.app.query.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.app.query.entity.ProductQuery;

public interface ProductQueryRepository extends MongoRepository<ProductQuery, String> {

	List<ProductQuery> findByNameContainingIgnoreCase(String name);
}
