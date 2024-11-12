package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Episode;
import com.example.demo.model.Firm;
import com.example.demo.model.MovieVideo;
import com.example.demo.model.QRCoins;
import com.example.demo.model.User;
import com.example.demo.repository.EpisodeRepository;
import com.example.demo.repository.FirmRepository;
import com.example.demo.repository.MovieVideoRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.service.EpisodeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EpisodeController {
	@Autowired
	private EpisodeService episodeService;
	@Autowired
	private EpisodeRepository episodeRepository;
	@Value("${upload.path}")
	private String pathUploadImage;
	@Autowired
	private MovieVideoRepository movieVideoRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FirmRepository firmRepository;

	@GetMapping("/manager-episode")
	public String getAllEpisode(Model model) {
		model.addAttribute("episode", new Episode());
		model.addAttribute("firms", firmRepository.getActiveFirms());
		List<Episode> episodes = episodeService.getAllActiveEpisodes();
		model.addAttribute("episodes", episodes);
		return "admin/managerepoise";
	}

	@GetMapping("/delete-epoise/{id}")
	public String deleteEpisode(@PathVariable("id") Long id) {
		Optional<Episode> optional = episodeRepository.findById(id);
		Episode episode = optional.get();
		episode.setStatus(false);
		episodeRepository.save(episode);
		return "redirect:/manager-episode";
	}

	@PostMapping("/update-episode")
	public String updateCoin(@ModelAttribute Episode episode, BindingResult result,
			@RequestParam("link_video") MultipartFile video, Model model, HttpSession session) {
		Long id = (Long) session.getAttribute("id_episode");
		Optional<Episode> optional = episodeRepository.findById(id);

		if (optional.isPresent()) {
			Episode existingEpisode = optional.get();
			existingEpisode.setStatus(true);

			existingEpisode.setName_episode(episode.getName_episode());
			existingEpisode.setFirm(episode.getFirm());
			if (!video.isEmpty()) {
				try {
					String videoTrallerFilename = System.currentTimeMillis() + "_"
							+ URLEncoder.encode(video.getOriginalFilename(), StandardCharsets.UTF_8.toString());
					Path videoTrallerPath = Paths.get(pathUploadImage + File.separator + videoTrallerFilename);
					Files.write(videoTrallerPath, video.getBytes());
					episode.setLink_video(videoTrallerFilename);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {

				existingEpisode.setLink_video(existingEpisode.getLink_video());

			}
			episodeRepository.save(existingEpisode);
			session.removeAttribute("id_episode");
			session.setAttribute("successMessage", "Cập nhật thành công!");
			return "redirect:/manager-episode";

		} else

		{

			return "redirect:/manager-episode";
		}
	}

	@GetMapping("/edit-episode/{id}")
	public String editQR(@PathVariable("id") Long id, Model model, HttpSession session) {
		session.setAttribute("id_episode", id);

		Optional<Episode> optional = episodeRepository.findById(id);
		if (optional.isPresent()) {
			Episode episode = optional.get();
			model.addAttribute("firms", firmRepository.getActiveFirms());
			List<Episode> episodes = episodeService.getAllActiveEpisodes();
			model.addAttribute("episodes", episodes);
			model.addAttribute("episode", episode);
			return "admin/edit-episode";
		} else {
			return "redirect:/manager-episode";
		}
	}

	@PostMapping("/addepisode")
	public String addFirm(@ModelAttribute Episode episode, BindingResult result,

			@RequestParam("link_video") MultipartFile video, Model model, HttpSession session) {
		episode.setStatus(true);

		if (!video.isEmpty()) {
			try {
				String videoTrallerFilename = System.currentTimeMillis() + "_"
						+ URLEncoder.encode(video.getOriginalFilename(), StandardCharsets.UTF_8.toString());
				Path videoTrallerPath = Paths.get(pathUploadImage + File.separator + videoTrallerFilename);
				Files.write(videoTrallerPath, video.getBytes());
				episode.setLink_video(videoTrallerFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		episodeRepository.save(episode);

		List<User> users = userRepository.findAll();
		List<Episode> firms = episodeRepository.findAll();

		// Vòng lặp kiểm tra và thêm mới các MovieVideo
		for (User user : users) {
			for (Episode f : firms) {
				boolean exists = movieVideoRepository.existsByUserIdAndFirmId(user.getId(), f.getId());

				// Nếu bản ghi chưa tồn tại, thêm mới
				if (!exists) {
					MovieVideo movieVideo = new MovieVideo();
					movieVideo.setUser(user);
					movieVideo.setEpisode(f);
					movieVideo.setStatus(0);
					movieVideoRepository.save(movieVideo);
				}
			}
		}
		session.setAttribute("successMessage", "Thêm thành công!");
		model.addAttribute("episode", new Episode());
		return "redirect:/manager-episode";
	}

}
