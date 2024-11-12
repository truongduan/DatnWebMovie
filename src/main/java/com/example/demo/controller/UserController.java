package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<Map<String, String>> saveUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
		Map<String, String> response = new HashMap<>();

		// Kiểm tra lỗi đầu vào từ phía server
		if (result.hasErrors()) {
			result.getFieldErrors().forEach(error -> {
				response.put(error.getField() + "Error", error.getDefaultMessage());
			});
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			// Gọi service để lưu người dùng
			userService.saveUser(userDto);
			response.put("message", "Success");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			// Lỗi liên quan đến ràng buộc cơ sở dữ liệu
			logger.error("Database constraint violation: ", e);
			response.put("generalError", "Database constraint violation: " + e.getMostSpecificCause().getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (IllegalArgumentException e) {
			// Lỗi khi có tham số không hợp lệ
			logger.error("Invalid argument: ", e);
			response.put("generalError", "Invalid input: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// Xử lý tất cả các ngoại lệ khác
			logger.error("Error saving user: ", e);
			response.put("generalError", "An unexpected error occurred: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/admin/users")
	@ResponseBody
	public List<User> getAllUsers() {
		return userService.findAllUsers();
	}
}
