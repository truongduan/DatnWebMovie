package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.model.Cart;
import com.example.demo.repository.CartRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ProjectWebApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProjectWebApplication.class, args);

	}

}
