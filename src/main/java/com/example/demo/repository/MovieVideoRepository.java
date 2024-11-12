package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

import com.example.demo.model.MovieVideo;

@Repository
public interface MovieVideoRepository extends JpaRepository<MovieVideo, Long> {
	@Query("SELECT mv FROM MovieVideo mv WHERE mv.user.id = :userId AND mv.episode.id = :episodeId")
	List<MovieVideo> findByUserIdAndFirmId(@Param("userId") Long userId, @Param("episodeId") Long episodeId);

	@Query("SELECT CASE WHEN COUNT(mv) > 0 THEN true ELSE false END FROM MovieVideo mv WHERE mv.user.id = :userId AND mv.episode.id = :episodeId")
	boolean existsByUserIdAndFirmId(@Param("userId") long userId, @Param("episodeId") long episodeId);

	@Query("SELECT mv FROM MovieVideo mv WHERE mv.user.id = :userId AND mv.episode.id = :episodeId")
	Optional<MovieVideo> findById(@Param("userId") Long userId, @Param("episodeId") Long episodeId);

	@Query("SELECT COUNT(mv) FROM MovieVideo mv JOIN mv.episode e JOIN e.firm f WHERE mv.status = 0 AND f.status = true AND f.id = :firmId and mv.user.id = :user_id")
	int counGetAll(@Param("firmId") Long firmId, @Param("user_id") Long user_id);

	@Query("SELECT mv FROM MovieVideo mv JOIN mv.episode e JOIN e.firm f WHERE mv.status = 0 AND f.status = true AND f.id = :firmId and mv.user.id = :user_id")
	List<MovieVideo> getAll(@Param("firmId") Long firmId, @Param("user_id") Long user_id);
}
