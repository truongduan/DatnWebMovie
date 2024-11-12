package com.example.demo.dto;

public class ListCoinDTO {

	private String name;
	private String email;
	private double sumprice;
	private double sumcoin;
	public ListCoinDTO(String name, String email, double sumprice, double sumcoin) {
		super();
		this.name = name;
		this.email = email;
		this.sumprice = sumprice;
		this.sumcoin = sumcoin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getSumprice() {
		return sumprice;
	}
	public void setSumprice(double sumprice) {
		this.sumprice = sumprice;
	}
	public double getSumcoin() {
		return sumcoin;
	}
	public void setSumcoin(double sumcoin) {
		this.sumcoin = sumcoin;
	}
	
}
