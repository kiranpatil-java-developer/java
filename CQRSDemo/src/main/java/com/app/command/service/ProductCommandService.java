package com.app.command.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.command.dto.CreateProductCommand;
import com.app.command.entity.ProductCommand;
import com.app.command.repository.ProductCommandRepository;
import com.app.exception.CommandProductNotFoundException;
import com.app.exception.InvalidProductException;
import com.app.exception.QuerySyncException;
import com.app.query.entity.ProductQuery;
import com.app.query.repository.ProductQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductCommandService {

    private final ProductCommandRepository commandRepo;
    private final ProductQueryRepository queryRepo;

    public Long createProduct(CreateProductCommand cmd) {

        log.debug("Creating product with command: {}", cmd);

        validateCreateCommand(cmd);

        ProductCommand product = ProductCommand.builder()
                .name(cmd.name())
                .description(cmd.description())
                .price(cmd.price())
                .stock(cmd.stock())
                .build();

        ProductCommand saved = commandRepo.save(product);
        log.info("Product created in command DB with id: {}", saved.getId());

        try {
            queryRepo.save(mapToQuery(saved));
            log.info("Product synced to query DB with id: {}", saved.getId());
        } catch (Exception ex) {
            log.error("Failed to sync product to query DB for id: {}", saved.getId(), ex);
            throw new QuerySyncException("Failed to sync product to query DB ", ex);
        }

        return saved.getId();
    }

    public void updateStock(Long id, Integer stock) {

        log.debug("Updating stock for product id: {}, new stock: {}", id, stock);

        if (id == null) {
            log.error("Product id is null");
            throw new InvalidProductException("Product id must not be null");
        }

        if (stock == null || stock < 0) {
            log.error("Invalid stock value: {}", stock);
            throw new InvalidProductException("Stock must be >= 0");
        }

        ProductCommand product = commandRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Command product not found for id: {}", id);
                    return new CommandProductNotFoundException(id);
                });

        LocalDateTime now = LocalDateTime.now();

        product.setStock(stock);
        product.setUpdatedAt(now);
        log.info("Stock updated in command DB for product id: {}", id);

        ProductQuery view = queryRepo.findById(id.toString())
                .orElseThrow(() -> {
                    log.error("Query product missing for id: {}", id);
                    return new QuerySyncException("Query product missing for id: " + id, null);
                });

        view.setStock(stock);
        view.setUpdatedAt(now);

        try {
            queryRepo.save(view);
            log.info("Stock updated in query DB for product id: {}", id);
        } catch (Exception ex) {
            log.error("Failed to update query DB product for id: {}", id, ex);
            throw new QuerySyncException("Failed to update query DB product: " + id, ex);
        }
    }

    public void deleteProduct(Long id) {

        log.debug("Deleting product with id: {}", id);

        if (id == null) {
            log.error("Product id is null");
            throw new InvalidProductException("Product id must not be null");
        }

        if (!commandRepo.existsById(id)) {
            log.warn("Command product not found for deletion, id: {}", id);
            throw new CommandProductNotFoundException(id);
        }

        commandRepo.deleteById(id);
        log.info("Product deleted from command DB, id: {}", id);

        try {
            queryRepo.deleteById(id.toString());
            log.info("Product deleted from query DB, id: {}", id);
        } catch (Exception ex) {
            log.error("Failed to delete product from query DB, id: {}", id, ex);
            throw new QuerySyncException("Failed to delete product from query DB", ex);
        }
    }

    private void validateCreateCommand(CreateProductCommand cmd) {

        log.debug("Validating CreateProductCommand: {}", cmd);

        if (cmd == null) {
            log.error("CreateProductCommand is null");
            throw new InvalidProductException("CreateProductCommand must not be null");
        }

        if (!hasText(cmd.name())) {
            log.error("Invalid product name");
            throw new InvalidProductException("Product name must not be empty");
        }

        if (cmd.price() == null || cmd.price().doubleValue() <= 0) {
            log.error("Invalid product price: {}", cmd.price());
            throw new InvalidProductException("Price must be greater than 0");
        }

        if (cmd.stock() == null || cmd.stock() < 0) {
            log.error("Invalid product stock: {}", cmd.stock());
            throw new InvalidProductException("Stock must be >= 0");
        }
    }

    private ProductQuery mapToQuery(ProductCommand product) {
        return ProductQuery.builder()
                .id(product.getId().toString())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
