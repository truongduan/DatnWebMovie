package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Comment;
import com.example.demo.model.Firm;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CommentService {

	@Autowired
	UserRepository repository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	FirmRepository firmRepository;

	public Comment saveComment(long id_user, long id_firm, String commentText) {
		Optional<User> optional = repository.findById(id_user);
		Optional<Firm> firms = firmRepository.findById(id_firm);

		if (optional.isPresent() && firms.isPresent()) {
			Comment comment = new Comment();
			comment.setUser(optional.get());
			comment.setFirm(firms.get());
			comment.setComment(commentText);
			return commentRepository.save(comment); // Return the saved Comment
		} else {
			throw new IllegalArgumentException("User or Firm not found");
		}
	}

	public List<Comment> findAllByFirmId(Long id_firm) {
		return commentRepository.findAllByFirmId(id_firm);
	}

	public Long getTotalComments() {
		return commentRepository.countTotalComments(); //
	}
}
