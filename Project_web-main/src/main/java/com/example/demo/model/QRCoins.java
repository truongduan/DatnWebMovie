package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "qr_coins")
public class QRCoins {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Giá tiền không được để trống")
	@Min(value = 0, message = "Giá tiền phải lớn hơn hoặc bằng 0")
	private Double money;

	private String url_coins;

	@NotNull(message = "Khuyến mãi xu không được để trống")
	@Min(value = 0, message = "Khuyến mãi phải lớn hơn hoặc bằng 0")
	private Double discountPercentage;
	private Boolean status;
}
