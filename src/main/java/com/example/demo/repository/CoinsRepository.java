package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Coins;
import com.example.demo.model.User;

import java.util.List;

@Repository
public interface CoinsRepository extends JpaRepository<Coins, Long> {
	Optional<Coins> findByUser(User user);

	@Query("SELECT c FROM Coins c WHERE c.user.id = :id_user")
	Optional<Coins> findByUserId(@Param("id_user") Long id_user);

	@Query("SELECT c.coins FROM Coins c WHERE c.user.id = :userId")
	Integer findCoinsByUserId(@Param("userId") Long userId);
}
