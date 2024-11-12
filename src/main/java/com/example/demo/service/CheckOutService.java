package com.example.demo.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Cart;
import com.example.demo.model.CheckOut;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CheckOutRepository;
import com.example.demo.repository.UserRepository;

@Service
public class CheckOutService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CheckOutRepository checkOutRepository;

	public void addCheckout(long id_user, long id_cart) {
		Optional<User> users = userRepository.findById(id_user);
		Optional<Cart> cart = cartRepository.findById(id_cart);
		CheckOut checkOut = new CheckOut();
		checkOut.setUser(users.get());
		checkOut.setCart(cart.get());
		checkOut.setStatus(1);
		checkOut.setDate(new Date());
		checkOutRepository.save(checkOut);
	}


}
