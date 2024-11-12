package com.example.demo.dto;

import java.util.Date;

public class UserCheckOutDTO {
	private Long userId;
	private String userName;
	private String userEmail;
	private Date checkOutDate;
	private boolean checkOutStatus;
	private double checkOutMoney;
	private Long qrId;
	private Long checkOutId;
	private double discountPercentage;
	public UserCheckOutDTO(Long userId, String userName, String userEmail, Date checkOutDate, boolean checkOutStatus,
			double checkOutMoney, Long qrId, Long checkOutId, double discountPercentage) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.checkOutDate = checkOutDate;
		this.checkOutStatus = checkOutStatus;
		this.checkOutMoney = checkOutMoney;
		this.qrId = qrId;
		this.checkOutId = checkOutId;
		this.discountPercentage = discountPercentage;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public Date getCheckOutDate() {
		return checkOutDate;
	}
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}
	public boolean isCheckOutStatus() {
		return checkOutStatus;
	}
	public void setCheckOutStatus(boolean checkOutStatus) {
		this.checkOutStatus = checkOutStatus;
	}
	public double getCheckOutMoney() {
		return checkOutMoney;
	}
	public void setCheckOutMoney(double checkOutMoney) {
		this.checkOutMoney = checkOutMoney;
	}
	public Long getQrId() {
		return qrId;
	}
	public void setQrId(Long qrId) {
		this.qrId = qrId;
	}
	public Long getCheckOutId() {
		return checkOutId;
	}
	public void setCheckOutId(Long checkOutId) {
		this.checkOutId = checkOutId;
	}
	public double getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	

	

}
