package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.QRCoins;

@Repository
public interface QRCoinsRepository extends JpaRepository<QRCoins, Long> {
	@Query("SELECT q FROM QRCoins q WHERE q.status = true")
	List<QRCoins> findAllQRCoins();

	Optional<QRCoins> findById(Long id);

	@Query("SELECT q FROM QRCoins q WHERE q.status = true")
	List<QRCoins> findAll();

}
