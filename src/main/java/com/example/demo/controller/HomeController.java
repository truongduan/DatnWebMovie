package com.example.demo.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Cart;
import com.example.demo.model.Firm;
import com.example.demo.model.MovieVideo;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieVideoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.EpisodeService;
import com.example.demo.service.FirmService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private FirmRepository firmRepository;
	@Autowired
	FirmService firmService;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private MovieVideoRepository movieVideoRepository;
	@Autowired
	private UserRepository repository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private EpisodeService episodeService;
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home(Model model, HttpSession session) {
		List<FirmDto> firms = episodeService.getFirm();
		model.addAttribute("firms", firms);
		List<Firm> topFirms = firmService.getTop5MostViewedFirms();
		model.addAttribute("categoryList", categoryService.getAllCategories());
		model.addAttribute("topFirms", topFirms);
		session.setAttribute("islogin", false);
		return "web/test";
	}

	@GetMapping("/home")
	public String quit(Model model, HttpSession session) {
		Long idUser = (Long) session.getAttribute("id_user");
		Boolean islogin = (Boolean) session.getAttribute("islogin");
		session.setAttribute("status", true);
		if (islogin == null) {
			islogin = false;
		}
		model.addAttribute("categoryList", categoryService.getAllCategories());
		List<Firm> topFirms = firmService.getTop5MostViewedFirms();
		model.addAttribute("topFirms", topFirms);
		List<FirmDto> firmDtos = episodeService.getFirm();

		List<Firm> firms = firmRepository.findAll();
		List<Cart> carts = cartRepository.findCartsWithUser(idUser);

		Optional<User> optional = repository.findById(idUser);
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
		return "web/test";
	}

	@GetMapping("/profile")
	public String showProfile(Model model, HttpSession session) {
		Long idUser = (Long) session.getAttribute("id_user");
		Boolean islogin = (Boolean) session.getAttribute("islogin");

		if (idUser == null || !Boolean.TRUE.equals(islogin)) {
			// Nếu người dùng chưa đăng nhập, chuyển hướng đến trang đăng nhập hoặc trang
			// chủ
			return "redirect:/login";
		}

		Optional<User> optional = repository.findById(idUser);
		if (optional.isPresent()) {
			User user = optional.get(); // Lấy thông tin User
			String userName = user.getName(); // Lấy tên của User
			model.addAttribute("userName", userName); // Thêm tên vào Model
			model.addAttribute("user", user); // Thêm đối tượng User vào Model để hiển thị thêm thông tin khác nếu cần
		} else {
			// Nếu không tìm thấy người dùng, có thể hiển thị thông báo lỗi hoặc chuyển
			// hướng
			model.addAttribute("error", "User not found");
		}

		return "web/profile"; // Trả về trang profile.html
	}

	@PostMapping("/update-profile")
	public String updateProfile(@RequestParam("id") Long id, @RequestParam("name") String newName, Model model) {
		try {
			// Gọi phương thức updateUserName từ UserService để cập nhật tên
			User updatedUser = userService.updateUserName(id, newName);

			// Nếu cập nhật thành công, trả về view profile với người dùng đã cập nhật
			model.addAttribute("user", updatedUser);
			model.addAttribute("message", "Cập nhật thông tin thành công");
			return "web/profile"; // Trả về trang profile (giả sử trang này tồn tại)
		} catch (RuntimeException e) {
			// Xử lý khi có lỗi, ví dụ như không tìm thấy người dùng
			model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
			return "error"; // Trả về trang lỗi (giả sử trang này tồn tại)
		}
	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("userId") Long userId,
			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword,
			Model model, RedirectAttributes re) {

		boolean success = userService.changePassword(userId, currentPassword, newPassword);

		if (success) {
			re.addFlashAttribute("message", "Thay đổi mật khẩu thành công");
		} else {
			re.addFlashAttribute("error", "Thay đổi mật khẩu thất bại");
		}

		return "redirect:/profile";
	}

}
