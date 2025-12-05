package com.app.query.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.app.exception.InvalidSearchParameterException;
import com.app.exception.ProductNotFoundException;
import com.app.query.entity.ProductQuery;
import com.app.query.repository.ProductQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductQueryService {

    private final ProductQueryRepository repo;

    public ProductQuery getById(String id) {

        log.debug("Fetching product by id: {}", id);

        if (!StringUtils.hasText(id)) {
            log.error("Invalid product id provided");
            throw new InvalidSearchParameterException("Product ID must not be null or empty");
        }

        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found with id: {}", id);
                    return new ProductNotFoundException(id);
                });
    }

    public List<ProductQuery> searchByName(String name) {

        log.debug("Searching products by name: {}", name);

        if (!StringUtils.hasText(name)) {
            log.error("Invalid search name provided");
            throw new InvalidSearchParameterException("Search name must not be null or blank");
        }

        List<ProductQuery> result = repo.findByNameContainingIgnoreCase(name);

        if (result.isEmpty()) {
            log.warn("No products found for name: {}", name);
            throw new ProductNotFoundException(
                    "No products found matching name: " + name);
        }

        log.info("Found {} products for name: {}", result.size(), name);
        return result;
    }

    public Page<ProductQuery> getAll(Pageable pageable) {

        log.debug("Fetching all products with pageable: {}", pageable);

        if (pageable == null) {
            log.error("Pageable is null");
            throw new InvalidSearchParameterException("Pageable must not be null");
        }

        Page<ProductQuery> page = repo.findAll(pageable);

        if (page.isEmpty()) {
            log.warn("No products available in database");
            throw new ProductNotFoundException("No products available");
        }

        log.info("Fetched {} products", page.getTotalElements());
        return page;
    }
}
