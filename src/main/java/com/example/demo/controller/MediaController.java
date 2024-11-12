package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MediaController {

	@Value("${upload.path}")
	private String pathUploadMedia;

	@GetMapping("/loadImage")
	@ResponseBody
	public byte[] loadImage(@RequestParam("imageName") String imageName, HttpServletResponse response)
			throws IOException {
		response.setContentType("image/jpeg");
		return loadFile(imageName, response);
	}

	@GetMapping("/loadVideo")
	@ResponseBody
	public void loadVideo(@RequestParam("videoName") String videoName, HttpServletResponse response)
			throws IOException {
		response.setCharacterEncoding("UTF-8");
		File file = new File(pathUploadMedia + File.separator + videoName);
		if (file.exists()) {
			response.setContentType("video/mp4");
			response.setHeader("Content-Disposition",
					"inline; filename=\"" + new String(videoName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

			try (InputStream inputStream = new FileInputStream(file)) {
				IOUtils.copy(inputStream, response.getOutputStream());
				response.flushBuffer();
			} catch (IOException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading file");
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private byte[] loadFile(String fileName, HttpServletResponse response) throws IOException {
		File file = new File(pathUploadMedia + File.separator + fileName);
		if (file.exists()) {
			try (InputStream inputStream = new FileInputStream(file)) {
				return IOUtils.toByteArray(inputStream);
			} catch (IOException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading file");
			}
		}
		return null;
	}
}
