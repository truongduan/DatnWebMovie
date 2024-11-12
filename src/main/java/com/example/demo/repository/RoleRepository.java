package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Role;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findById(Long id);

	List<Role> findByIdIsNot(Long id);
}
