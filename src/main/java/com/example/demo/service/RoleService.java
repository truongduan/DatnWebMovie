package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;

@Service
public class RoleService {
	@Autowired
	private UserRoleRepository repository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;

	public void save(long id_user, long id_role) {
		Optional<User> users = userRepository.findById(id_user);
		Optional<Role> roles = roleRepository.findById(id_role);
		UserRole role = new UserRole();
		role.setRole(roles.get());
		role.setUser(users.get());
		repository.save(role);
	}

	public List<Role> getAllRolesExceptAdmin() {
		return roleRepository.findByIdIsNot(1L);
	}
}
