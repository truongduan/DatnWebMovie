package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll() // Cho phép tất cả các
																								// yêu cầu
		).csrf(csrf -> csrf.disable()) // Tắt CSRF để đơn giản hóa quá trình thử nghiệm
				.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login") // Trang đăng nhập tùy chỉnh cho OAuth2
						.defaultSuccessUrl("/current", true) // Chuyển hướng sau khi đăng nhập thành công qua OAuth2
				).logout(logout -> logout.logoutUrl("/logout") // URL để gửi yêu cầu đăng xuất
						.logoutSuccessUrl("/logout-success") // URL để chuyển hướng sau khi đăng xuất thành công
						.deleteCookies("JSESSIONID") // Xóa cookie phiên làm việc
						.clearAuthentication(true) // Xóa thông tin xác thực
						.permitAll() // Cho phép tất cả mọi người truy cập vào URL đăng xuất
				);

		return http.build();
	}
}
