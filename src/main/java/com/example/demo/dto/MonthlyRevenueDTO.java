package com.example.demo.dto;

public class MonthlyRevenueDTO {
	private int month;
	private int year;
	private double totalRevenue;

	public MonthlyRevenueDTO(int month, int year, double totalRevenue) {
		this.month = month;
		this.year = year;
		this.totalRevenue = totalRevenue;
	}

	// Getters và Setters
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
}
