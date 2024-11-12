package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Cart;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findById(Long id);

	@Query("SELECT c FROM Cart c " + "JOIN c.user u " + "WHERE u.id = :userId")
	List<Cart> findCartsWithUser(@Param("userId") Long userId);
}
