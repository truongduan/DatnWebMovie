package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.QRCoins;
import com.example.demo.service.CategoryService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/manager-category")
	public String getAllCategories(Model model) {
		model.addAttribute("categoryList", categoryService.getAllCategories());
		return "admin/managercategory";
	}

	@GetMapping("/form-add-category")
	public String getaddCategory() {
		return "admin/category";
	}

	@PostMapping("/add-category")
	public ResponseEntity<String> addCategory(@RequestBody Category category) {
		try {
			if (category.getCategory() == null || category.getCategory().isEmpty()) {
				return ResponseEntity.badRequest().body("Vui lòng không để thể loại trống");
			}
			if (categoryService.isCategoryExists(category.getCategory())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Thể loại này đã tồn tại");
			} else {

				categoryService.addCategory(category);
				return ResponseEntity.ok("Thêm thể loại th	ành công!");
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
		}
	}

	@GetMapping("/edit-category/{id}")
	public String editCategory(@PathVariable("id") Long id, Model model, HttpSession session) {
		session.setAttribute("id_category", id);
		Optional<Category> optional = categoryService.getCategoryById(id);
		if (optional.isPresent()) {
			Category category = optional.get();
			model.addAttribute("cat", category);
			return "admin/edit-category";
		} else {
			return "redirect:/manager-category";
		}
	}

	@PostMapping("/update-category")
	public String updateCategory(@Valid @ModelAttribute("cat") Category category, BindingResult result,
			HttpSession session, Model model) {

		if (result.hasErrors()) {
			return "admin/edit-category"; // If validation errors, return to form
		}

		// Check if the category already exists
		if (categoryService.isCategoryExists(category.getCategory())) {
			model.addAttribute("errorMessage", "Thê loại này đã tồn tại."); // Add error message
			return "admin/edit-category"; // Return the form to display the error
		}

		Long id = (Long) session.getAttribute("id_category");

		if (id != null) {
			Optional<Category> optional = categoryService.getCategoryById(id);
			if (optional.isPresent()) {
				Category existingCategory = optional.get();
				existingCategory.setCategory(category.getCategory());
				categoryService.saveCategory(existingCategory);
				session.removeAttribute("id_category");
				model.addAttribute("successMessage", "Cập nhật thể loại thành công!");
				return "redirect:/manager-category"; // Redirect on success
			} else {
				model.addAttribute("errorMessage", "Không tìm thấy thể loại.");
			}
		} else {
			model.addAttribute("errorMessage", "ID thể loại không hợp lệ.");
		}

		return "admin/edit-category"; // Return form if error
	}

	@GetMapping("/delete-category/{id}")
	public String deleteQr(@PathVariable("id") Long id) {
		Optional<Category> optional = categoryService.getCategoryById(id);
		if (optional.isPresent()) {
			Category category = optional.get();
			category.setIs_delete(false);
			categoryService.saveCategory(category);
			return "redirect:/manager-category";
		} else {
			return "redirect:/manager-category";
		}
	}

}
