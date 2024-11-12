package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query("SELECT c FROM Category c WHERE c.is_delete = true")
	List<Category> findAllByIsDeleteTrue();
	
	  @Query("SELECT c FROM Category c WHERE LOWER(c.category) = LOWER(:category) AND c.is_delete = true")
	    Optional<Category> findByCategoryIgnoreCaseAndIsDeleteTrue(String category);
}
