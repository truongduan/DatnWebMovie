package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.CoinsRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;

@Service
public class UserRoleService {
	@Autowired
	private UserRoleRepository userRoleReponsitory;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;

	public void addUserRole(long id_role, long id_user) {
		Optional<Role> role = roleRepository.findById(id_role);
		Optional<User> user = userRepository.findById(id_user);

		if (role.isPresent() && user.isPresent()) {
			UserRole userRole = new UserRole();
			userRole.setRole(role.get());
			userRole.setUser(user.get());
			userRoleReponsitory.save(userRole);
		} else {

			System.out.println("Role or User not found.");
		}
	}

	public long role(long id_user) {

		Optional<UserRole> optional = userRoleReponsitory.findByUserId(id_user);
		if (optional.isPresent()) {
			return optional.get().getRole().getId();
		} else {
			return 0;
		}
	}
}
