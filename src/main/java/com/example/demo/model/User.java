package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = true)
	private String googleId;

	private String email;
	private String name;
	@Column(nullable = false)
	private String password;

	@Column(name = "accountwith")
	private String accountWith;

	@OneToMany(mappedBy = "user")
	private List<Coins> coins = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<CheckOutCoins> checkOutCoins = new ArrayList<>();

}
