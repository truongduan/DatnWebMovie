package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserDto {
	@NotEmpty(message = "Vui lòng nhập email !")
	private String email;

	@NotEmpty(message = "Vui lòng nhập tên !")
	private String name;

	@NotEmpty(message = "Vui lòng nhập password !")
	private String password;

	@NotNull(message = "Vui lòng chọn vài trò người dùng")
	private Long roleId;

	// Getters and setters
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
