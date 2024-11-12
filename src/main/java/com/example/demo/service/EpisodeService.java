package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.EpisodeRepository;
import com.example.demo.dto.FirmDto;
import com.example.demo.model.Episode;

@Service
public class EpisodeService {
	@Autowired
	private EpisodeRepository episodeRepository;

	public List<Episode> getAllActiveEpisodes() {
		return episodeRepository.findAllActiveEpisodes(); // Hoặc findAllActiveEpisodes() nếu dùng JPQL
	}

	public List<FirmDto> getFirm() {
		return episodeRepository.getEpisodesByFirm();
	}
}
