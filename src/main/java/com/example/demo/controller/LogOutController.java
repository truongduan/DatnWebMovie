package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Firm;
import com.example.demo.repository.FirmRepository;
import com.example.demo.service.CategoryService;
import com.example.demo.service.EpisodeService;
import com.example.demo.service.FirmService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogOutController {

	@Autowired
	private FirmRepository firmRepository;

	@Autowired
	private FirmService firmService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private EpisodeService episodeService;

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// Xóa phiên
		session.invalidate();

		// Chuyển hướng đến trang logout-success
		return "redirect:/logout-success";
	}

	@GetMapping("/logout-success")
	public String logoutSuccess(Model model) {
		// Tạo lại mô hình mà không sử dụng phiên
		List<FirmDto> firms = episodeService.getFirm();
		model.addAttribute("firms", firms);
		model.addAttribute("categoryList", categoryService.getAllCategories());
		List<Firm> topFirms = firmService.getTop5MostViewedFirms();

		model.addAttribute("topFirms", topFirms);

		// Trả về trang logout-success
		return "web/test";
	}
}
