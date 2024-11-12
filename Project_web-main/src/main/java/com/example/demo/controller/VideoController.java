package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Cart;
import com.example.demo.model.Comment;
import com.example.demo.model.Episode;
import com.example.demo.model.Firm;
import com.example.demo.model.MovieVideo;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.EpisodeRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieVideoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CheckOutService;
import com.example.demo.service.CoinsService;
import com.example.demo.service.CommentService;
import com.example.demo.service.FirmService;
import com.example.demo.service.MovieFirmService;
import com.example.demo.service.MovieVideoService;
import com.mysql.cj.Session;

import jakarta.servlet.http.HttpSession;

@Controller
public class VideoController {
	@Autowired
	private FirmRepository firmRepository;
	@Autowired
	FirmService firmService;
	@Autowired
	CommentService commentService;
	@Autowired
	CommentRepository commentRepository;

	@Autowired
	MovieVideoService movieVideoService;
	@Autowired
	MovieVideoService videoService;
	@Autowired
	CartService cartService;
	@Autowired
	CheckOutService checkOutService;
	@Autowired
	CoinsService coinsService;
	@Autowired
	MovieFirmService service;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private UserRepository repository;
	@Autowired
	private MovieVideoRepository movieVideoRepository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private EpisodeRepository episodeRepository;

	@GetMapping("/anime-watching")
	public String video(Model model) {

		model.addAttribute("categoryList", categoryService.getAllCategories());
		return "web/anime-watching";

	}

	@GetMapping("/movie_firm/{id}")
	public String getFirmById(@PathVariable("id") Long id, Model model, HttpSession session) {
		List<FirmDto> firmList = episodeRepository.getEpisodesByFirm(id);

		session.setAttribute("islogin", true);

		if (!firmList.isEmpty()) {
			FirmDto firm = firmList.get(0); // Get the first firm, assuming multiple firms are not needed.
			session.setAttribute("id_firm", firm.getId());
			model.addAttribute("firm", firm);

			return "redirect:/movie-firm?id=" + firm.getId();
		} else {
			return "redirect:/";
		}
	}

	@GetMapping("/movie-buy-firm/{id}")
	public String buyFirmWatchingVideo(@PathVariable("id") Long id, Model model, HttpSession session) {
		Long idUser = (Long) session.getAttribute("id_user");
		Optional<Firm> firmOptional = firmRepository.findById(id);

		if (firmOptional.isPresent()) {
			Firm firm = firmOptional.get();
			firmService.updateStatusFirm(firm.getId());
			long id_Cart = cartService.saveCart(firm.getId(), idUser);
			checkOutService.addCheckout(idUser, id_Cart);
			boolean coin = coinsService.updateCoins(idUser, firm.getCoins_video(), firm.getId(), model, session);

			if (coin == true) {

				session.setAttribute("lastPurchasedMovieId", firm.getId());
				service.saveFirm(firm.getId(), idUser);
				movieVideoService.updateMovie(idUser, firm.getId());
				return "redirect:/movie-firm?id=" + firm.getId();
			} else {

				Long lastPurchasedMovieId = (Long) session.getAttribute("lastPurchasedMovieId");

				if (lastPurchasedMovieId != null) {
					session.setAttribute("error", "Bạn không đủ xu để xem tập tiếp theo ");
					return "redirect:/movie-firm?id=" + lastPurchasedMovieId;
				} else {
					// Fallback if there's no previous movie in session
					return "redirect:/movie-firm?id=" + firm.getId();
				}
			}
		}
		return "redirect:/";
	}

	@GetMapping("/movie-firm")
	public String movieSuccess(@RequestParam("id") long firmId, HttpSession session, Model model) {
		Long idUser = (Long) session.getAttribute("id_user");
		model.addAttribute("isVideo", false);
		Optional<User> optional = repository.findById(idUser);
		Optional<Firm> optionalFirm = firmRepository.findById(firmId);
		if (optional.isPresent()) {
			Firm f = optionalFirm.get();
			List<Episode> episodes = episodeRepository.findByFirm(f);
			model.addAttribute("firms", episodes);
		}
		model.addAttribute("categoryList", categoryService.getAllCategories());
		List<Firm> firms = firmRepository.findAll();
		List<Cart> carts = cartRepository.findCartsWithUser(idUser);
		Map<Firm, List<MovieVideo>> firmMovieVideos = new HashMap<>();
		int userCoins = coinsService.getUserCoins(idUser);
		model.addAttribute("userCoins", userCoins);
		for (Firm firm : firms) {
			// Tạo một đối tượng MovieVideo mới cho mỗi Firm

			List<MovieVideo> list = movieVideoRepository.findByUserIdAndFirmId(idUser, firm.getId());

			firmMovieVideos.put(firm, list);
		}

		model.addAttribute("firmMovieVideos", firmMovieVideos);
		Boolean islogin = (Boolean) session.getAttribute("islogin");

		if (islogin == null) {
			islogin = false;
		}

		session.setAttribute("islogin", true);

		// Lấy thông tin của firm dựa trên firmId để hiển thị chi tiết
		List<FirmDto> firmOptional = episodeRepository.getEpisodesByFirm(firmId);

		FirmDto firm = firmOptional.get(0);
		List<Firm> firms1 = firmRepository.findByName_firm(firm.getName_firm().toLowerCase());
		List<Comment> comments = commentRepository.findByFirmIdWithUser(firm.getId());
		session.setAttribute("id_firm", firm.getId());

		model.addAttribute("firm", firm);
		model.addAttribute("comments", comments);
//			model.addAttribute("videoLink", firm.getLink_video());

		return "web/anime-watching";

	}

	@GetMapping("/click-movie-firm")
	public String clickVideo(@RequestParam("id") long id, Model model, HttpSession session) {
		Long idUser = (Long) session.getAttribute("id_user");
		Optional<Episode> optionalEpisode = episodeRepository.findById(id);
		Optional<User> optional = repository.findById(idUser);
		Episode episode = optionalEpisode.get();
		model.addAttribute("episode", episode); // Thêm Episode vào Model
		Optional<Firm> optionalFirm = firmRepository.findById(episode.getFirm().getId());
		if (optional.isPresent()) {
			Firm f = optionalFirm.get();
			List<Episode> episodes = episodeRepository.findByFirm(f);
			model.addAttribute("firms", episodes);
		}
		model.addAttribute("isVideo", true);
		model.addAttribute("categoryList", categoryService.getAllCategories());
		List<Firm> firms = firmRepository.findAll();
		List<Cart> carts = cartRepository.findCartsWithUser(idUser);
		Map<Firm, List<MovieVideo>> firmMovieVideos = new HashMap<>();
		int userCoins = coinsService.getUserCoins(idUser);
		model.addAttribute("userCoins", userCoins);
		for (Firm firm : firms) {
			// Tạo một đối tượng MovieVideo mới cho mỗi Firm

			List<MovieVideo> list = movieVideoRepository.findByUserIdAndFirmId(idUser, firm.getId());

			firmMovieVideos.put(firm, list);
		}

		model.addAttribute("firmMovieVideos", firmMovieVideos);
		Boolean islogin = (Boolean) session.getAttribute("islogin");

		if (islogin == null) {
			islogin = false;
		}

		session.setAttribute("islogin", true);

		// Lấy thông tin của firm dựa trên firmId để hiển thị chi tiết
		List<FirmDto> firmOptional = episodeRepository.getEpisodesByFirm(episode.getFirm().getId());

		FirmDto firm = firmOptional.get(0);

		List<Comment> comments = commentRepository.findByFirmIdWithUser(firm.getId());
		session.setAttribute("id_firm", firm.getId());

		model.addAttribute("firm", firm);
		model.addAttribute("comments", comments);

		return "web/anime-watching";
	}

	@GetMapping("/click_movie_firm_episode/{id}")
	public String getClickFirmById(@PathVariable("id") Long id, Model model, HttpSession session) {
		// Tìm Episode theo id
		Optional<Episode> optional = episodeRepository.findById(id);
		if (optional.isPresent()) {
			Episode episode = optional.get(); // Lấy đối tượng Episode từ Optional
			model.addAttribute("episode", episode); // Thêm Episode vào Model

			List<FirmDto> firmList = episodeRepository.getEpisodesByFirm(episode.getFirm().getId());

			if (!firmList.isEmpty()) {
				FirmDto firm = firmList.get(0);
				session.setAttribute("id_firm", firm.getId());

				return "redirect:/click-movie-firm?id=" + episode.getId();
			}
		} else {

			return "redirect:/";
		}

		return "redirect:/"; // Cần có một hành động mặc định
	}

	@PostMapping("/submit-comment")
	@ResponseBody
	public Map<String, Object> submitComment(@RequestParam("comment") String commentText, HttpSession session) {

		Long userId = (Long) session.getAttribute("id_user");
		Long id_firm = (Long) session.getAttribute("id_firm");

		if (userId == null) {
			return Map.of("status", "error", "message", "User not logged in");
		}

		Comment newComment = commentService.saveComment(userId, id_firm, commentText);

		// Returning the comment as a JSON response with formatted date
		return Map.of("status", "success", "commentText", newComment.getComment(), "formattedDate",
				newComment.getFormattedDate(), "userName", newComment.getUser().getName());
	}

	@GetMapping("/comments")
	@ResponseBody
	public List<Comment> getComments(@RequestParam("id_firm") Long id_firm) {
		return commentService.findAllByFirmId(id_firm);
	}
}
