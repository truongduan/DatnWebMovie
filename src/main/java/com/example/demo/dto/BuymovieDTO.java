package com.example.demo.dto;

import java.util.Date;

public class BuymovieDTO {
	private String namefirm;
	private String img;
	private String username;
	private Date buy;

	public BuymovieDTO() {

	}

	public BuymovieDTO(String namefirm, String img, String username, Date buy) {
		super();
		this.namefirm = namefirm;
		this.img = img;
		this.username = username;
		this.buy = buy;
	}

	public String getNamefirm() {
		return namefirm;
	}

	public void setNamefirm(String namefirm) {
		this.namefirm = namefirm;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getBuy() {
		return buy;
	}

	public void setBuy(Date buy) {
		this.buy = buy;
	}

}
