package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MonthlyRevenueDTO;
import com.example.demo.model.CheckOutCoins;
import com.example.demo.model.QRCoins;
import com.example.demo.model.User;
import com.example.demo.repository.CheckOutCoinsRepository;
import com.example.demo.repository.QRCoinsRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CheckOutCoinsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CheckOutCoinsRepository checkOutCoinsRepository;

	@Autowired
	private QRCoinsRepository qrCoinsRepository;

	public long getTotalCheckOutCoins() {
		return checkOutCoinsRepository.countTotalCheckOutCoins();
	}

	public double getTotalMoney() {
		Double totalMoney = checkOutCoinsRepository.sumTotalMoney();
		return (totalMoney != null) ? totalMoney : 0.0;
	}

	public List<MonthlyRevenueDTO> getMonthlyRevenue() {
		return checkOutCoinsRepository.findMonthlyRevenue();
	}

	public double getSumMoneyByStatusFalse() {
		Double result = checkOutCoinsRepository.sumMoneyByStatusFalse();
		return (result != null) ? result : 0.0;
	}

	public double getSumMoneyByStatusTrue() {
		Double totalMoney = checkOutCoinsRepository.sumMoneyByStatusTrue();
		return (totalMoney != null) ? totalMoney : 0.0;
	}

	public void saveCheckOutcoin(long id_user, long id_qr, double money) {
		Optional<User> userOptional = userRepository.findById(id_user);
		Optional<QRCoins> qrCoinsOptional = qrCoinsRepository.findById(id_qr);

		if (userOptional.isPresent() && qrCoinsOptional.isPresent()) {
			CheckOutCoins checkOutCoins = new CheckOutCoins();
			checkOutCoins.setDate(new Date());
			checkOutCoins.setMoney(money);
			checkOutCoins.setQrCoins(qrCoinsOptional.get());
			checkOutCoins.setUser(userOptional.get());

			checkOutCoinsRepository.save(checkOutCoins);
		} else {

		}
	}
}
