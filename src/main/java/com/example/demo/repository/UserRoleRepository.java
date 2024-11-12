package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserRole;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	 @Query("SELECT ur FROM UserRole ur JOIN ur.user u WHERE u.id = :userId")
		Optional<UserRole> findByUserId(@Param("userId") Long userId);
}
