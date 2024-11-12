package com.example.demo.controller;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Firm;
import com.example.demo.model.MovieVideo;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieVideoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CheckOutService;
import com.example.demo.service.CoinsService;
import com.example.demo.service.EmailService;
import com.example.demo.service.EpisodeService;
import com.example.demo.service.FirmService;
import com.example.demo.service.UserRoleService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller

public class GoogleController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	UserService userService;
	@Autowired
	CoinsService service;
	@Autowired
	private FirmRepository firmRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private MovieVideoRepository movieVideoRepository;
	@Autowired
	private UserRepository repository;
	@Autowired
	UserRoleService roleService;
	@Autowired
	FirmService firmService;
	@Autowired
	UserRepository repositorUserRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	EpisodeService episodeService;

	@GetMapping("/current")
	public String current(OAuth2AuthenticationToken authentication, HttpSession session, Model model) {
		String result = "web/test"; // Giá trị mặc định nếu không tìm thấy user
		session.setAttribute("islogin", true);
		Map<String, Object> attributes = authentication.getPrincipal().getAttributes();
		String googleId = (String) attributes.get("sub");
		String name = (String) attributes.get("name");
		String email = (String) attributes.get("email");
		List<User> list = repositorUserRepository.findByGoogleId(googleId);
		session.setAttribute("status", true);
		// Nếu danh sách user tìm thấy không rỗng, điều này có nghĩa là đã có user trong
		// hệ thống
		if (!list.isEmpty()) {
			User user = list.get(0); // Lấy user đầu tiên trong danh sách
			result = "redirect:/login-google-success?id=" + user.getId(); // Chuyển hướng tới trang login thành công
		} else {
			// Nếu không tìm thấy user trong hệ thống, tạo mới user
			long idUser = userService.saveUserGoogle(email, name, googleId);
			emailService.sendOtpCodePayment(email, generateCode());
			double coin = service.saveCoin(50, idUser);
			session.setAttribute("id_user", idUser);
			roleService.addUserRole(1, idUser);
			session.setAttribute("coins", coin);
			session.setAttribute("islogin", true);
			result = "redirect:/google-succes?id=" + idUser; // Chuyển hướng tới trang google-succes
		}

		return result;
	}

	private String generateCode() {
		String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		SecureRandom random = new SecureRandom();
		StringBuilder password = new StringBuilder(6);

		for (int i = 0; i < 6; i++) {
			int index = random.nextInt(letters.length());
			password.append(letters.charAt(index));
		}

		return password.toString() + "google";
	}

	@GetMapping("/google-succes")
	public String registergoogle(@RequestParam("id") long idUser, HttpSession session, Model model) {

		Boolean islogin = (Boolean) session.getAttribute("islogin");

		if (islogin == null) {
			islogin = false;
		}

		session.setAttribute("islogin", true);
		List<Firm> firms = firmRepository.findAll();
		Optional<User> optional = repository.findById(idUser);
		List<FirmDto> firmDtos = episodeService.getFirm();
		List<Firm> topFirms = firmService.getTop5MostViewedFirms();
		model.addAttribute("topFirms", topFirms);
		if (optional.isPresent()) {
			User user = optional.get();
			double coin = service.getCoinsUser(optional.get());
			session.setAttribute("id_user", optional.get().getId());
			session.setAttribute("coins", coin);
			session.setAttribute("status", true);
			Map<Firm, List<MovieVideo>> firmMovieVideos = new HashMap<>();

//			for (Firm firm : firms) {
//				// Tạo một đối tượng MovieVideo mới cho mỗi Firm
//				MovieVideo movieVideo = new MovieVideo();
//				movieVideo.setFirm(firm);
//				movieVideo.setUser(user);
//				movieVideo.setStatus(0);
//
//				// Lưu từng MovieVideo vào cơ sở dữ liệu
//				movieVideoRepository.save(movieVideo);
//
//				// Lấy danh sách MovieVideo tương ứng với User và Firm hiện tại
//				List<MovieVideo> list = movieVideoRepository.findByUserIdAndFirmId(idUser, firm.getId());
//				System.out.println("Firm: " + firm.getId() + ", List Size: " + list.size());
//				// Đưa danh sách vào map với key là Firm
//				firmMovieVideos.put(firm, list);
//			}

			// Thêm map vào model
			model.addAttribute("firmMovieVideos", firmMovieVideos);
		}

		model.addAttribute("firms", firmDtos);
		return "web/test";
	}

	@GetMapping("/login-google-success")
	public String loginSuccess(@RequestParam("id") long idUser, HttpSession session, Model model) {

		System.out.println("User ID: " + idUser);
		List<FirmDto> firmDtos = episodeService.getFirm();
		Boolean islogin = (Boolean) session.getAttribute("islogin");
		List<Firm> topFirms = firmService.getTop5MostViewedFirms();
		model.addAttribute("topFirms", topFirms);
		if (islogin == null) {
			islogin = false;
		}
		model.addAttribute("categoryList", categoryService.getAllCategories());
		session.setAttribute("islogin", true);
		List<Firm> firms = firmRepository.findAll();

		Optional<User> optional = repository.findById(idUser);
		String username = optional.get().getName();
		session.setAttribute("username", username);
		session.setAttribute("islogin", true);
		if (optional.isPresent()) {
			double coin = service.getCoinsUser(optional.get());
			session.setAttribute("id_user", optional.get().getId());
			session.setAttribute("coins", coin);
			session.setAttribute("status", true);
			Map<Firm, List<MovieVideo>> firmMovieVideos = new HashMap<>();

			for (Firm firm : firms) {
				// Tạo một đối tượng MovieVideo mới cho mỗi Firm

				List<MovieVideo> list = movieVideoRepository.findByUserIdAndFirmId(idUser, firm.getId());

				firmMovieVideos.put(firm, list);
			}

			model.addAttribute("firmMovieVideos", firmMovieVideos);
		}

		model.addAttribute("firms", firmDtos);

		return "web/test";

	}
}
