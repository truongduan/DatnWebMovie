package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.model.QRCoins;
import com.example.demo.repository.QRCoinsRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CheckOutCoinsService;

import jakarta.servlet.http.HttpSession;

@Controller
public class QrCoinsController {
	@Autowired
	private QRCoinsRepository qrCoinsRepository;
	@Autowired
	CheckOutCoinsService checkOutCoinsService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/qr-coin")
	public String qrCoins(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size,
			Model model, HttpSession session) {
		Long idUser = (Long) session.getAttribute("id_user");
		model.addAttribute("categoryList", categoryService.getAllCategories());
		// Cài đặt phân trang
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

		// Lấy dữ liệu phân trang
		Page<QRCoins> qrCoinsPage = qrCoinsRepository.findAll(pageable);

		// Đưa thông tin vào mô hình
		model.addAttribute("qrCoinsList", qrCoinsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", qrCoinsPage.getTotalPages());
		model.addAttribute("totalItems", qrCoinsPage.getTotalElements());

		// Lưu id_user vào session (nếu cần)
		session.setAttribute("id_user", idUser);

		return "web/coins";
	}

	@PostMapping("/save-coin-id")
	@ResponseBody
	public ResponseEntity<String> saveCoinIdToSession(@RequestParam("coinId") Long coinId, HttpSession session) {
		session.setAttribute("coinId", coinId);
		return ResponseEntity.ok("Coin ID " + coinId + " saved to session.");
	}

	@GetMapping("/payment-success")
	public String payment(HttpSession session) {
		Long idUser = (Long) session.getAttribute("id_user");
		Long idQr = (Long) session.getAttribute("coinId");

		if (idUser == null || idQr == null) {

			return "redirect:/qr-coin"; //
		}
		Optional<QRCoins> qrCoinsOptional = qrCoinsRepository.findById(idQr);
		if (qrCoinsOptional.isPresent()) {
			QRCoins qrCoins = qrCoinsOptional.get();
			double money = qrCoins.getMoney();
			checkOutCoinsService.saveCheckOutcoin(idUser, idQr, money);
			return "redirect:/qr-coin";
		} else {

			return "redirect:/qr-coin";
		}
	}

}
