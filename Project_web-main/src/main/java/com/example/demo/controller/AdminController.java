package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.MonthlyRevenueDTO;
import com.example.demo.dto.UserCheckOutDTO;
import com.example.demo.model.CheckOutCoins;
import com.example.demo.model.Coins;
import com.example.demo.model.Firm;
import com.example.demo.model.QRCoins;
import com.example.demo.model.User;
import com.example.demo.repository.CoinsRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.QRCoinsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BuymovieService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CheckOutCoinsService;
import com.example.demo.service.CommentService;
import com.example.demo.service.FirmService;
import com.example.demo.service.QRCoinsService;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	@Value("${upload.path}")
	private String pathUploadImage;

	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentService CommentService;
	@Autowired
	QRCoinsRepository qrRepository;
	@Autowired
	UserService userService;
	@Autowired
	CoinsRepository coinsRepository;
	@Autowired
	FirmRepository firmRepository;
	@Autowired
	CheckOutCoinsService checkOutCoinsService;
	@Autowired
	FirmService firmService;
	@Autowired
	QRCoinsService coinsService;
	@Autowired
	QRCoinsRepository qrCoinsRepository;
	@Autowired
	RoleService roleService;
	@Autowired
	BuymovieService buymovieService;
	@Autowired
	CategoryService categoryService;

	@GetMapping("/index/admin")
	public String indexAdmin(Model model) {
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
	}

	@GetMapping("/components")
	public String component() {
		return "admin/components";
	}

	@GetMapping("/coins")
	public String coins(Model model) {
		model.addAttribute("qr", new QRCoins());

		return "admin/coins";
	}

	@PostMapping("/addQr")
	public ResponseEntity<Map<String, Object>> addCoin(@ModelAttribute QRCoins qrCoins, BindingResult result,
			@RequestParam("url_coins") MultipartFile imgFile) {

		Map<String, Object> response = new HashMap<>();

		if (!imgFile.isEmpty()) {
			try {
				String imgFilename = System.currentTimeMillis() + "_" + imgFile.getOriginalFilename();
				Path imgPath = Paths.get(pathUploadImage + File.separator + imgFilename);
				Files.write(imgPath, imgFile.getBytes());
				qrCoins.setUrl_coins(imgFilename);
				qrCoins.setStatus(true);
			} catch (IOException e) {
				e.printStackTrace();
				response.put("success", false);
				response.put("message", "Error saving image");
				return ResponseEntity.status(500).body(response);
			}
		}

		qrRepository.save(qrCoins);
		response.put("success", true);
		response.put("message", "Thêm thành công");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/video")
	public String videoAdmin() {
		return "admin/forms";
	}

	@GetMapping("/add-user")
	public String addUserAdmin(Model model) {
		model.addAttribute("roles", roleService.getAllRolesExceptAdmin());
		return "admin/form-user";
	}

	@GetMapping("/manager-user")
	public String managerUserAdmin(Model model) {
		List<User> users = userRepository.findAllUsersWithCoins();
		model.addAttribute("users", users);
		return "admin/manageruser";
	}

	@GetMapping("/manager-coins")
	public String managerCoinsAdmin(Model model) {
		model.addAttribute("qrs", coinsService.getAllQRCoins());
		return "admin/managercoin";
	}

	@GetMapping("/manager-video")
	public String tabAdmin(Model model) {
		model.addAttribute("firm", new Firm());
		model.addAttribute("categories", categoryService.getAllCategories());
		List<Firm> firms = firmRepository.getActiveFirms();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		for (Firm firm : firms) {
			if (firm.getFirmdate() != null) {
				firm.setFormattedDate(firm.getFirmdate().format(formatter));
			} else {
				firm.setFormattedDate(""); // or a default value if needed
			}
		}
		model.addAttribute("firms", firms);
		return "admin/managervideo";
	}

	@GetMapping("/delete-firm/{id}")
	public String deleteFirm(@PathVariable("id") Long id) {
		Optional<Firm> optional = firmRepository.findById(id);
		if (optional.isPresent()) {
			Firm firm = optional.get();
			firm.setStatus(false);
			firmRepository.save(firm);
			return "redirect:/manager-video";
		} else {
			return "redirect:/manager-video";
		}
	}

	@GetMapping("/manager-buy-phim")
	public String getAllBuyFirm(Model model) {
		model.addAttribute("buyFirmList", buymovieService.getCompletedBuymovies());
		return "admin/managerbuyfirm";
	}

	@GetMapping("/delete-qr/{id}")
	public String deleteQr(@PathVariable("id") Long id) {
		Optional<QRCoins> optional = qrCoinsRepository.findById(id);
		if (optional.isPresent()) {
			QRCoins qr = optional.get();
			qr.setStatus(false);
			qrCoinsRepository.save(qr);
			return "redirect:/manager-coins";
		} else {
			return "redirect:/manager-coins";
		}
	}

	@GetMapping("/manager-payment")
	public String managerPayment(Model model) {
		List<UserCheckOutDTO> users = userService.getUsersWithCheckOutAndQRCode();
		model.addAttribute("users", users);
		return "admin/managerpayment";
	}

	@PostMapping("/update-payment-status")
	@ResponseBody
	public Map<String, String> updatePaymentStatus(@RequestParam("id") Long id, @RequestParam("userId") Long userId,
			@RequestParam("qrId") Long qrId, @RequestParam("checkOutStatus") boolean checkOutStatus) {
		CheckOutCoins checkOutCoins = userService.checkOutCoins(id, userId, qrId);
		userService.updateCheckOutStatus(id, userId, qrId, checkOutStatus);
		if (checkOutCoins != null) {
			Optional<Coins> optional = coinsRepository.findByUserId(userId);
			if (optional.isPresent()) {
				Coins coins = optional.get();
				double coin = checkOutCoins.getMoney() / 1000;
				coins.setCoins(coins.getCoins() + coin + checkOutCoins.getQrCoins().getDiscountPercentage());
				coinsRepository.save(coins);
			}
		}
		// Create a response map
		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "Trạng thái thanh toán đã được cập nhật thành công!");

		return response;
	}

	@GetMapping("/edit-phim/{id}")
	public String editVideo(@PathVariable("id") Long id, Model model, HttpSession session) {
		model.addAttribute("categories", categoryService.getAllCategories());
		session.setAttribute("id", id);
		if (session.getAttribute("result") != null) {
			model.addAttribute("result", session.getAttribute("result"));
			session.removeAttribute("result");
		}

		if (session.getAttribute("firm") != null) {
			model.addAttribute("firm", session.getAttribute("firm"));
			session.removeAttribute("firm");
		}

		Optional<Firm> optional = firmRepository.findById(id);
		if (optional.isPresent()) {
			model.addAttribute("firm", optional.get());
			return "admin/forms-edit-firm";
		} else {
			return "redirect:/manager-video";
		}
	}

	@GetMapping("/edit-qr/{id}")
	public String editQR(@PathVariable("id") Long id, Model model, HttpSession session) {
		session.setAttribute("id_coin", id);
		Optional<QRCoins> optional = qrCoinsRepository.findById(id);
		if (optional.isPresent()) {
			QRCoins qrCoins = optional.get();

			model.addAttribute("qr", qrCoins);
			return "admin/edit-coin";
		} else {
			return "redirect:/manager-coins";
		}
	}

	@PostMapping("/edit-Qr")
	public String updateCoin(@ModelAttribute QRCoins qrCoins, BindingResult result,
			@RequestParam("url_coins") MultipartFile imgFile, Model model, HttpSession session) {
		Long id = (Long) session.getAttribute("id_coin");
		Optional<QRCoins> optional = qrCoinsRepository.findById(id);

		if (optional.isPresent()) {
			QRCoins existingQRCoins = optional.get();
			existingQRCoins.setStatus(true);
			existingQRCoins.setMoney(qrCoins.getMoney());
			existingQRCoins.setDiscountPercentage(qrCoins.getDiscountPercentage());

			if (!imgFile.isEmpty()) {
				try {
					String imgFilename = System.currentTimeMillis() + "_" + imgFile.getOriginalFilename();
					Path imgPath = Paths.get(pathUploadImage + File.separator + imgFilename);
					Files.write(imgPath, imgFile.getBytes());
					existingQRCoins.setUrl_coins(imgFilename);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

				qrCoins.setUrl_coins(existingQRCoins.getUrl_coins());
			}

			qrCoinsRepository.save(existingQRCoins);
			session.removeAttribute("id_coin");

			return "redirect:/manager-coins";
		} else {

			return "redirect:/manager-coins";
		}
	}

	@PostMapping("/update-video")
	public String updateVideo(HttpSession session, @ModelAttribute Firm firm, BindingResult result,
			@RequestParam("img_firm") MultipartFile imgFile, 
			@RequestParam("link_video_traller") MultipartFile videoTraller, Model model) {

		Long id = (Long) session.getAttribute("id");
		// Lấy danh sách các phim đã active
		List<Firm> firms = firmRepository.findByName_firmandActive(firm.getName_firm());

		if (id == null) {
			return "redirect:/edit-phim/" + id;
		}

		// Lấy phim cần cập nhật dựa trên id
		Optional<Firm> optional = firmRepository.findById(id);
		if (!optional.isPresent()) {
			session.setAttribute("error", "Phim không tồn tại.");
			return "redirect:/edit-phim/" + id;
		}

		Firm f = optional.get();

		

		
		// Cập nhật thông tin phim
		f.setFirmdate(firm.getFirmdate());

		f.setAuthor_firm(firm.getAuthor_firm());
		f.setCoins_video(firm.getCoins_video());
	
		f.setCategory(firm.getCategory());

		f.setTotal_episodes(firm.getTotal_episodes());

		// Xử lý file ảnh, video và trailer
		try {
			if (!imgFile.isEmpty()) {
				String imgFilename = System.currentTimeMillis() + "_"
						+ URLEncoder.encode(imgFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
				Path imgPath = Paths.get(pathUploadImage + File.separator + imgFilename);
				Files.write(imgPath, imgFile.getBytes());
				f.setImg_firm(imgFilename);
			}

			if (!videoTraller.isEmpty()) {
				String videoTrallerFilename = System.currentTimeMillis() + "_"
						+ URLEncoder.encode(videoTraller.getOriginalFilename(), StandardCharsets.UTF_8.toString());
				Path videoTrallerPath = Paths.get(pathUploadImage + File.separator + videoTrallerFilename);
				Files.write(videoTrallerPath, videoTraller.getBytes());
				f.setLink_video_traller(videoTrallerFilename);
			}
		} catch (IOException e) {
			e.printStackTrace();
			session.setAttribute("error", "Lỗi khi lưu file.");
			return "redirect:/edit-phim/" + id;
		}

		// Lưu thông tin phim đã cập nhật
		firmRepository.save(f);
		session.removeAttribute("errortotal");
		session.removeAttribute("error");
		// Thông báo thành công
		session.setAttribute("successMessage", "Cập nhật thành công!");
		return "redirect:/manager-video";
	}

//	@PostMapping("/update-video")
//	public String updateVideo(HttpSession session, @ModelAttribute Firm firm, BindingResult result,
//			@RequestParam("img_firm") MultipartFile imgFile, @RequestParam("link_video") MultipartFile videoFile,
//			@RequestParam("link_video_traller") MultipartFile videoTraller) {
//
//		Long id = (Long) session.getAttribute("id");
//	
//		if (id != null) {
//			Optional<Firm> optional = firmRepository.findById(id);
//			if (optional.isPresent()) {
//				Firm f = optional.get();
//
//				f.setFirmdate(firm.getFirmdate());
//				f.setTittle_firm(firm.getTittle_firm());
//				f.setAuthor_firm(firm.getAuthor_firm());
//				f.setCoins_video(firm.getCoins_video());
//				f.setPractice(firm.getPractice());
//				f.setCategory(firm.getCategory()); // Cập nhật thể loại phim
//				f.setDescribe(firm.getDescribe());
//
//				f.setTotal_episodes(firm.getTotal_episodes());
//				for (Firm ff : firms) {
//					ff.setTotal_episodes(firm.getTotal_episodes());
//					firmRepository.save(ff);
//
//				}
//
//				// Nếu không chọn ảnh mới, giữ nguyên ảnh cũ
//				if (!imgFile.isEmpty()) {
//					try {
//						String imgFilename = System.currentTimeMillis() + "_"
//								+ URLEncoder.encode(imgFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//						Path imgPath = Paths.get(pathUploadImage + File.separator + imgFilename);
//						Files.write(imgPath, imgFile.getBytes());
//						f.setImg_firm(imgFilename);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//				// Nếu không chọn video mới, giữ nguyên video cũ
//				if (!videoFile.isEmpty()) {
//					try {
//						String videoFilename = System.currentTimeMillis() + "_"
//								+ URLEncoder.encode(videoFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//						Path videoPath = Paths.get(pathUploadImage + File.separator + videoFilename);
//						Files.write(videoPath, videoFile.getBytes());
//						f.setLink_video(videoFilename);
//						f.setStatus(true);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//				// Nếu không chọn trailer video mới, giữ nguyên video cũ
//				if (!videoTraller.isEmpty()) {
//					try {
//						String videoTrallerFilename = System.currentTimeMillis() + "_" + URLEncoder
//								.encode(videoTraller.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//						Path videoTrallerPath = Paths.get(pathUploadImage + File.separator + videoTrallerFilename);
//						Files.write(videoTrallerPath, videoTraller.getBytes());
//						f.setLink_video_traller(videoTrallerFilename);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//
//				firmRepository.save(f);
//				session.setAttribute("successMessage", "update successfully added!");
//				session.removeAttribute("id");
//
//				return "redirect:/manager-video";
//			}
//		}
//
//		return "admin/forms-edit-firm";
//	}
	@PostMapping("/check-episodes")
	@ResponseBody
	public Map<String, Object> checkEpisodes(@RequestParam("totalEpisodes") int totalEpisodes,
			@RequestParam("firmName") String firmName) {
		boolean valid = firmService.getFirmCountByName(firmName) <= totalEpisodes;
		Map<String, Object> response = new HashMap<>();
		response.put("valid", valid);
		response.put("message", valid ? "" : "Tổng số tập phim không hợp lệ.");
		return response;
	}

	@GetMapping("list-load-coins")
	public String getAllListLoadCoins(Model model) {
		model.addAttribute("listLoadCoins", userService.getUserCoinsData());
		return "admin/coinlist";
	}

}
