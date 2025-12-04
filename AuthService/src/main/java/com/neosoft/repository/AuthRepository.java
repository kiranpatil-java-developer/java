package com.neosoft.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neosoft.entity.User;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

	boolean existsByUsernameIgnoreCase(String username);

	Optional<User> findByUsername(String username);

}
