package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Firm;
import com.example.demo.model.MovieFirm;
import com.example.demo.model.MovieVideo;

@Repository
public interface MovieFirmRepository extends JpaRepository<MovieFirm, Long> {
	@Query("SELECT mf.firm FROM MovieFirm mf WHERE mf.user.id = :userId")
	List<Firm> findAllFirmsByUserId(Long userId);
}
