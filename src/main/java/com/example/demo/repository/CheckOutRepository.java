package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import com.example.demo.dto.*;
import com.example.demo.model.CheckOut;
import com.example.demo.model.CheckOutCoins;

@Controller
public interface CheckOutRepository extends JpaRepository<CheckOut, Long> {

	@Query("SELECT new com.example.demo.dto.BuymovieDTO(f.name_firm, f.img_firm, u.name, c.date) " + "FROM CheckOut c "
			+ "JOIN c.cart ca " + "JOIN ca.firm f " + "JOIN c.user u ")
	List<BuymovieDTO> findCompletedBuymovies();


}
