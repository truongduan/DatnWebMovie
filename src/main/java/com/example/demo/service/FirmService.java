package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Category;
import com.example.demo.model.Firm;
import com.example.demo.repository.FirmRepository;

@Service
public class FirmService {

	@Autowired
	private FirmRepository firmRepository;

	public void updateStatusFirm(long id) {
		Optional<Firm> optional = firmRepository.findById(id);
		Firm firm = optional.get();

		firmRepository.save(firm);
	}

	public List<Firm> getTop5MostViewedFirms() {
		return firmRepository.getTop5MostViewedFirms();
	}

	public long getTotalFirms() {
		return firmRepository.countTotalFirms();
	}

	public List<FirmDto> getFirmsByCategory(Category category) {
		return firmRepository.findByCategory(category);
	}

	public long getFirmCountByName(String name) {
		return firmRepository.countByName_firm(name);
	}
	

	
}
