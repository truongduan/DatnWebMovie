package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.FirmDto;
import com.example.demo.dto.MonthlyRevenueDTO;
import com.example.demo.model.Cart;
import com.example.demo.model.Firm;
import com.example.demo.model.MovieVideo;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieVideoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CheckOutCoinsService;
import com.example.demo.service.CoinsService;
import com.example.demo.service.CommentService;
import com.example.demo.service.EpisodeService;
import com.example.demo.service.FirmService;
import com.example.demo.service.UserRoleService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {
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
	CheckOutCoinsService checkOutCoinsService;
	@Autowired
	CommentService CommentService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private EpisodeService episodeService;

	@GetMapping("/login")
	public String home(Model model) {
		model.addAttribute("categoryList", categoryService.getAllCategories());
		return "web/login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
		if (email.isEmpty() || password.isEmpty()) {
			model.addAttribute("error", "Vui lòng điền đầy đủ thông tin");
			return "web/login";
		}
		Optional<User> user = userService.checkLogin(email, password);
		if (user.isPresent()) {
			double coin = service.getCoinsUser(user.get());
			session.setAttribute("id_user", user.get().getId());
			session.setAttribute("coins", coin);
			session.setAttribute("status", true);
			System.out.println("kq" + coin);

			return "redirect:/login-success?id=" + user.get().getId();
		} else {
			model.addAttribute("error", "Thông tin đăng nhập không đúng");
			return "web/login";
		}
	}

	@GetMapping("/login-success")
	public String loginSuccess(@RequestParam("id") long idUser, HttpSession session, Model model) {

		System.out.println("User ID: " + idUser);

		Boolean islogin = (Boolean) session.getAttribute("islogin");
		List<Firm> topFirms = firmService.getTop5MostViewedFirms();
		model.addAttribute("topFirms", topFirms);
		if (islogin == null) {
			islogin = false;
		}
		model.addAttribute("categoryList", categoryService.getAllCategories());
		List<Firm> firms = firmRepository.findAll();
		
		List<FirmDto> firmDtos = episodeService.getFirm();
		Optional<User> optional = repository.findById(idUser);
		String username = optional.get().getName();
		session.setAttribute("username", optional.get().getEmail());
		session.setAttribute("email", username);
		session.setAttribute("islogin", true);
		if (optional.isPresent()) {
			Map<Firm, List<MovieVideo>> firmMovieVideos = new HashMap<>();

			for (Firm firm : firms) {
				// Tạo một đối tượng MovieVideo mới cho mỗi Firm

				List<MovieVideo> list = movieVideoRepository.findByUserIdAndFirmId(idUser, firm.getId());

				firmMovieVideos.put(firm, list);
			}

			model.addAttribute("firmMovieVideos", firmMovieVideos);
		}

		model.addAttribute("firms", firmDtos);
		long id = roleService.role(idUser);
		if (id == 1) {
			return "web/test";
		} else {
			if (id == 2) {
				session.setAttribute("id", id);
				session.setAttribute("isAdmintrator_Role", false);
				List<MonthlyRevenueDTO> monthlyRevenue = checkOutCoinsService.getMonthlyRevenue();

				// Tạo danh sách labels và data từ monthlyRevenue
				List<String> labels = monthlyRevenue.stream().map(mr -> mr.getMonth() + "/" + mr.getYear())
						.collect(Collectors.toList());
				List<Double> data = monthlyRevenue.stream().map(MonthlyRevenueDTO::getTotalRevenue)
						.collect(Collectors.toList());

				model.addAttribute("labels", labels);
				model.addAttribute("data", data);
				model.addAttribute("sumcomment", CommentService.getTotalComments());
				model.addAttribute("ordersum", checkOutCoinsService.getTotalCheckOutCoins());
				model.addAttribute("sumpeople", userService.getTotalUsers());
				model.addAttribute("sumfirm", firmService.getTotalFirms());
				model.addAttribute("summoney", checkOutCoinsService.getTotalMoney());
				model.addAttribute("orderfalse", checkOutCoinsService.getSumMoneyByStatusFalse());
				model.addAttribute("totalMoneyTrueOrders", checkOutCoinsService.getSumMoneyByStatusTrue());
				return "admin/index";
			} else {
				session.setAttribute("id", id);
				List<MonthlyRevenueDTO> monthlyRevenue = checkOutCoinsService.getMonthlyRevenue();
				session.setAttribute("isAdmintrator_Role", true);
				// Tạo danh sách labels và data từ monthlyRevenue
				List<String> labels = monthlyRevenue.stream().map(mr -> mr.getMonth() + "/" + mr.getYear())
						.collect(Collectors.toList());
				List<Double> data = monthlyRevenue.stream().map(MonthlyRevenueDTO::getTotalRevenue)
						.collect(Collectors.toList());

				model.addAttribute("labels", labels);
				model.addAttribute("data", data);
				model.addAttribute("sumcomment", CommentService.getTotalComments());
				model.addAttribute("ordersum", checkOutCoinsService.getTotalCheckOutCoins());
				model.addAttribute("sumpeople", userService.getTotalUsers());
				model.addAttribute("sumfirm", firmService.getTotalFirms());
				model.addAttribute("summoney", checkOutCoinsService.getTotalMoney());
				model.addAttribute("orderfalse", checkOutCoinsService.getSumMoneyByStatusFalse());
				model.addAttribute("totalMoneyTrueOrders", checkOutCoinsService.getSumMoneyByStatusTrue());
				return "admin/index";
			}

		}

	}
}
