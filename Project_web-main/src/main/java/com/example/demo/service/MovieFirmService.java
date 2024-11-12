package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Firm;
import com.example.demo.model.MovieFirm;
import com.example.demo.model.User;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieFirmRepository;
import com.example.demo.repository.UserRepository;

@Service
public class MovieFirmService {
	@Autowired
	MovieFirmRepository mfirmRepository;
	@Autowired
	UserRepository repository;
	@Autowired
	FirmRepository firmRepository;

	public void saveFirm(long id_firm, long id_user) {
		Optional<User> userOptional = repository.findById(id_user);
		Optional<Firm> firmOptional = firmRepository.findById(id_firm);

		// Kiểm tra nếu user không tồn tại
		if (!userOptional.isPresent()) {
			throw new IllegalArgumentException("User with ID " + id_user + " not found");
		}

		// Kiểm tra nếu firm không tồn tại
		if (!firmOptional.isPresent()) {
			throw new IllegalArgumentException("Firm with ID " + id_firm + " not found");
		}

		MovieFirm firm = new MovieFirm();
		firm.setFirm(firmOptional.get());
		firm.setUser(userOptional.get());
		firm.setView_number(1);

		mfirmRepository.save(firm);
	}

	public List<Firm> getAllFirmsByUserId(Long userId) {
		return mfirmRepository.findAllFirmsByUserId(userId);
	}
}
