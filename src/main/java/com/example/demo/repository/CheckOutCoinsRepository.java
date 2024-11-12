package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.MonthlyRevenueDTO;
import com.example.demo.model.CheckOutCoins;

@Repository
public interface CheckOutCoinsRepository extends JpaRepository<CheckOutCoins, Long> {

	@Query("SELECT c FROM CheckOutCoins c WHERE c.id =:id AND  c.user.id = :idUser AND c.qrCoins.id = :idQr")
	Optional<CheckOutCoins> findByUserIdAndQrId(@Param("id") Long id, @Param("idUser") Long idUser,
			@Param("idQr") Long idQr);

	// Đếm tổng số hàng
	@Query("SELECT COUNT(c) FROM CheckOutCoins c")
	long countTotalCheckOutCoins();

	@Query("SELECT SUM(c.money) FROM CheckOutCoins c")
	Double sumTotalMoney();

	@Query("SELECT SUM(c.money) FROM CheckOutCoins c WHERE c.status = false")
	Double sumMoneyByStatusFalse();

	@Query("SELECT SUM(c.money) FROM CheckOutCoins c WHERE c.status = true")
	 Double  sumMoneyByStatusTrue();

	@Query("SELECT new com.example.demo.dto.MonthlyRevenueDTO(MONTH(c.date), YEAR(c.date), SUM(c.money)) "
			+ "FROM CheckOutCoins c " + "WHERE c.status = true  " + "GROUP BY YEAR(c.date), MONTH(c.date) "
			+ "ORDER BY YEAR(c.date), MONTH(c.date)")
	List<MonthlyRevenueDTO> findMonthlyRevenue();
}
