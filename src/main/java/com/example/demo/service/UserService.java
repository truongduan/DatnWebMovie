package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.dto.ListCoinDTO;
import com.example.demo.dto.UserCheckOutDTO;
import com.example.demo.dto.UserDto;
import com.example.demo.model.CheckOutCoins;
import com.example.demo.model.Coins;
import com.example.demo.model.Episode;
import com.example.demo.model.Firm;
import com.example.demo.model.MovieVideo;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.CheckOutCoinsRepository;
import com.example.demo.repository.CoinsRepository;
import com.example.demo.repository.EpisodeRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieVideoRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private FirmRepository firmRepository;
	@Autowired
	private MovieVideoRepository movieVideoRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	CoinsRepository coinsRepository;
	@Autowired
	private CheckOutCoinsRepository checkOutCoinsRepository; // Assuming you have a repository for the table
	@Autowired
	private EpisodeRepository episodeRepository;

	public void updateCheckOutStatus(Long id, Long idUser, Long idQr, boolean checkOutStatus) {
		CheckOutCoins checkOutCoins = checkOutCoinsRepository.findByUserIdAndQrId(id, idUser, idQr)
				.orElseThrow(() -> new RuntimeException(
						"CheckOutCoins not found for user with ID: " + idUser + " and QR ID: " + idQr));
		checkOutCoins.setStatus(checkOutStatus);
		checkOutCoinsRepository.save(checkOutCoins);
	}

	public CheckOutCoins checkOutCoins(Long id, Long idUser, Long idQr) {
		Optional<CheckOutCoins> optional = checkOutCoinsRepository.findByUserIdAndQrId(id, idUser, idQr);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}

	}

	@Transactional
	public void saveUser(UserDto userDto) throws Exception {
		if (userRepository.findByEmailAndAccountwith(userDto.getEmail(), "LOCAL").isPresent()) {
			throw new Exception("Email đã tồn tại");
		}
		if (!StringUtils.hasText(userDto.getEmail()) || !StringUtils.hasText(userDto.getName())
				|| !StringUtils.hasText(userDto.getPassword()) || userDto.getRoleId() == null) {
			throw new Exception("All fields are required");
		}

		User user = new User();
		user.setEmail(userDto.getEmail());
		user.setName(userDto.getName());
		user.setPassword(userDto.getPassword());
		user.setAccountWith("LOCAL");
		User savedUser = userRepository.save(user);

		Coins coins = new Coins();
		coins.setUser(savedUser);
		coins.setCoins(50.0);
		coinsRepository.save(coins);
		List<Episode> firms = episodeRepository.findAll();
		Optional<User> optional = userRepository.findById(savedUser.getId());

		if (optional.isPresent()) {
			User u = optional.get();
			Map<Episode, List<MovieVideo>> firmMovieVideos = new HashMap<>();

			for (Episode firm : firms) {

				MovieVideo movieVideo = new MovieVideo();
				movieVideo.setEpisode(firm);
				movieVideo.setUser(u);
				movieVideo.setStatus(0);

				movieVideoRepository.save(movieVideo);

				List<MovieVideo> list = movieVideoRepository.findByUserIdAndFirmId(savedUser.getId(), firm.getId());
				System.out.println("Firm: " + firm.getId() + ", List Size: " + list.size());

				firmMovieVideos.put(firm, list);
			}

		}
		Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new Exception("Role not found"));

		UserRole userRole = new UserRole();
		userRole.setUser(savedUser);
		userRole.setRole(role);

		userRoleRepository.save(userRole);
	}

	public boolean checkEmail(String email, String account) {
		Optional<User> optional = userRepository.findByEmailAndAccountwith(email, account);
		if (optional.isPresent())
			return true;
		else
			return false;
	}

	public long saveUser(String email, String username, String password) {
		User user = new User();
		user.setEmail(email);
		user.setName(username);
		user.setPassword(password);
		user.setAccountWith("LOCAL");
		userRepository.save(user);
		return user.getId();
	}

	public long saveUserGoogle(String email, String username, String googleId) {
		User user = new User();
		user.setEmail(email);
		user.setName(username);
		user.setGoogleId(googleId);
		user.setAccountWith("GOOGLE");
		userRepository.save(user);
		return user.getId();
	}

	public Optional<User> checkLogin(String email, String password) {

		Optional<User> users = userRepository.login(email, password, "LOCAL");
		return users;
	}

	public List<User> findAllUsers() {
		return userRepository.findAllUsersWithCoins();
	}

	public long getTotalUsers() {
		return userRepository.countTotalUsers();
	}

	public List<User> searchUsers(String query) {
		return userRepository.searchUsersWithCoins(query);
	}

	public List<UserCheckOutDTO> getUsersWithCheckOutAndQRCode() {
		return userRepository.findUsersWithCheckOutAndQRCode();
	}

	public List<ListCoinDTO> getUserCoinsData() {
		return userRepository.findUserCoinsData();
	}

	@Transactional
	 public User updateUserName(Long id, String newName) {
        // Tìm kiếm người dùng theo ID
        Optional<User> userOpt = userRepository.findById(id);
        
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        
        // Lấy thông tin người dùng và cập nhật tên
        User user = userOpt.get();
        user.setName(newName);
        
        // Lưu thông tin người dùng sau khi cập nhật
        return userRepository.save(user);
    }
	public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        // Find the user by ID
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the current password matches the user's password
            if (currentPassword.equals(user.getPassword())) {
                // Update the user's password
                user.setPassword(newPassword);

                // Save the updated user back to the repository
                userRepository.save(user);

                return true;  // Password change successful
            } else {
                return false;  // Current password does not match
            }
        } else {
            return false;  // User not found
        }
    }
}
