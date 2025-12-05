package com.app.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.command.entity.ProductCommand;

@Repository
public interface ProductCommandRepository extends JpaRepository<ProductCommand, Long> {

}
