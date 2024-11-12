package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.MovieVideo;
import com.example.demo.repository.MovieFirmRepository;
import com.example.demo.repository.MovieVideoRepository;

@Service
public class MovieVideoService {
	@Autowired
	private MovieVideoRepository movieVideoRepository;

	public void updateMovie(long id_user, long id_firm) {
		Optional<MovieVideo> optional = movieVideoRepository.findById(id_user, id_firm);
		if (optional.isPresent()) {
			MovieVideo movieVideo = optional.get();
			movieVideo.setStatus(1);
			movieVideoRepository.save(movieVideo);
		} else {
			System.out.println("not null");
		}
	}
}
