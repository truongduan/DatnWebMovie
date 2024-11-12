package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendOtpEmail(String to, String password) {
		String subject = "forget password";
		String body = "Mật khẩu mới của bạn là: " + password;

		sendEmail(to, subject, body);
	}

	public void sendOtpCodePayment(String to, String code) {
		String subject = "Code payment";
		String body = "Mã thanh toán qr khi mua coin  của bạn là: " + code + "  vui lòng giữ nó";

		sendEmail(to, subject, body);
	}

	private void sendEmail(String to, String subject, String body) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		javaMailSender.send(message);
	}
}