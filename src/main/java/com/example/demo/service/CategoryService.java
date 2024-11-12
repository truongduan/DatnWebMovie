package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	// Lấy tất cả các Category
	public List<Category> getAllCategories() {
		return categoryRepository.findAllByIsDeleteTrue();
	}

	// Thêm mới một Category
	public Category addCategory(Category category) {
		category.setIs_delete(true);
		return categoryRepository.save(category);
	}

	public Optional<Category> getCategoryById(Long id) {
		return categoryRepository.findById(id);
	}

	public void saveCategory(Category category) {
		categoryRepository.save(category);
	}

	public boolean isCategoryExists(String categoryName) {
		Optional<Category> category = categoryRepository.findByCategoryIgnoreCaseAndIsDeleteTrue(categoryName);
		return category.isPresent();
	}

}
