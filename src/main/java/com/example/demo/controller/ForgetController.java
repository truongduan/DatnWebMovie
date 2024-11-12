package com.example.demo.controller;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

@Controller
public class ForgetController {
	@Autowired
	EmailService emailService;
	@Autowired
	UserService service;
	@Autowired
	UserRepository repository;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/forget-password")
	public String forget(Model model) {
		model.addAttribute("categoryList", categoryService.getAllCategories());
		return "web/forget";
	}

	@PostMapping("/forgot-password")
	public String forgotPassword(@RequestParam String email, Model model) {
		if (email.isEmpty()) {
			model.addAttribute("error", "Vui lòng điền email.");
			return "web/forget";
		}
		Optional<User> optional = repository.findByEmailAndAccountwith(email, "LOCAL");
		if (optional.isPresent()) {
			User user = optional.get();
			String newPassword = generateNewPassword();
			user.setPassword(newPassword);

			emailService.sendOtpEmail(email, newPassword);
			repository.save(user);
			return "redirect:/login";
		} else {
			model.addAttribute("error", "Email này không tồn tại.");
			return "web/forget";
		}

	}

	private String generateNewPassword() {
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(6);

		for (int i = 0; i < 6; i++) {
			int index = random.nextInt(letters.length());
			password.append(letters.charAt(index));
		}

		return password.toString();
	}
}
