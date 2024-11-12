package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.CoinModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/storeCoinId")
public class CoinController {

	@PostMapping
	public ResponseEntity<String> storeCoinIdInSession(@ModelAttribute CoinModel coinModel, HttpSession session) {
		try {
			// Lưu coinId vào session
			session.setAttribute("coinId", coinModel.getCoinId());

			// Trả về phản hồi thành công
			return ResponseEntity.ok("Coin ID stored successfully");
		} catch (Exception e) {
			// In lỗi ra console và trả về phản hồi lỗi
			e.printStackTrace();
			return ResponseEntity.status(500).body("Failed to store Coin ID");
		}
	}
}