package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.QRCoins;
import com.example.demo.repository.QRCoinsRepository;

import java.util.List;

@Service
public class QRCoinsService {

	@Autowired
	private QRCoinsRepository qrCoinsRepository;

	public List<QRCoins> getAllQRCoins() {
		return qrCoinsRepository.findAll();
	}
}
