package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BuymovieDTO;
import com.example.demo.repository.CheckOutRepository;

import java.util.List;

@Service
public class BuymovieService {

	@Autowired
	private CheckOutRepository checkOutRepository;

	public List<BuymovieDTO> getCompletedBuymovies() {
		return checkOutRepository.findCompletedBuymovies();
	}
}
