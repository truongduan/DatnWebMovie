package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.Firm;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private FirmRepository firmRepository;
	@Autowired
	private UserRepository userRepository;

	public long saveCart(long id_firm, long id_user) {
		Optional<Firm> firms = firmRepository.findById(id_firm);
		Optional<User> users = userRepository.findById(id_user);
		Cart cart = new Cart();
		cart.setFirm(firms.get());
		cart.setUser(users.get());
		cartRepository.save(cart);
		return cart.getId();
	}
}
