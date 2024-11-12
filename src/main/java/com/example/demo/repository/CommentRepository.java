package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	@Query("SELECT c FROM Comment c JOIN c.firm f  WHERE f.id = :firmId")
	List<Comment> findByFirmIdWithUser(@Param("firmId") Long firmId);

	@Query("SELECT c FROM Comment c WHERE c.firm.id = :id_firm ORDER BY c.createdDate DESC")
	List<Comment> findAllByFirmId(@Param("id_firm") Long id_firm);

	@Query("SELECT COUNT(c) FROM Comment c")
	Long countTotalComments();
}
