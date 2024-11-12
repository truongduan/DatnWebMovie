package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.model.Coins;
import com.example.demo.model.Firm;
import com.example.demo.model.User;
import com.example.demo.repository.CoinsRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

@Service
public class CoinsService {
	@Autowired
	private CoinsRepository coinsRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FirmRepository firmRepository;

	public double saveCoin(double coin, long id_user) {
		Optional<User> optional = userRepository.findById(id_user);
		if (optional.isPresent()) {
			Coins coins = new Coins();
			coins.setCoins(coin);
			coins.setUser(optional.get());
			coinsRepository.save(coins);
			return coins.getCoins();
		}
		return 0.0;
	}

	public double getCoinsUser(User user) {
		Optional<Coins> optional = coinsRepository.findByUser(user);
		if (!optional.isEmpty()) {
			return optional.get().getCoins();
		} else {
			return 0.0;
		}
	}

	public boolean updateCoins(long id_user, double coin, long id_firm, Model model, HttpSession session) {
		Optional<Coins> optional = coinsRepository.findByUserId(id_user);
		Optional<Firm> fOptional = firmRepository.findById(id_firm);

		if (optional.isPresent() && fOptional.isPresent()) {
			Coins coins = optional.get();
			Firm firm = fOptional.get();

			if (coins.getCoins() >= firm.getCoins_video()) {
				double coin_user = coins.getCoins(); // Sử dụng kiểu dữ liệu double
				double result = coin_user - coin;

				coins.setCoins(result);
				coinsRepository.save(coins);

				
				session.setAttribute("coins", coins.getCoins());
				return true;
			} else {
				session.setAttribute("coins", coins.getCoins());
				session.setAttribute("errorMessage", "Xu của bạn không đủ");
				return false;
			}
		} else {
			model.addAttribute("errorMessage", "User or Firm not found.");
			return false;
		}
	}

	public int getUserCoins(Long userId) {
		
		Integer userCoins = coinsRepository.findCoinsByUserId(userId);
		return userCoins != null ? userCoins : 0; 
	}
}
