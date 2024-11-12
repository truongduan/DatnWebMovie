package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.ListCoinDTO;
import com.example.demo.dto.UserCheckOutDTO;
import com.example.demo.model.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	// Notice the change to match 'accountwith'
	@Query("SELECT u FROM User u WHERE u.email = :email AND u.accountWith = :accountwith")
	Optional<User> findByEmailAndAccountwith(@Param("email") String email, @Param("accountwith") String accountwith);

	Optional<User> findById(Long id);

	@Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password  AND u.accountWith = :accountWith")
	Optional<User> login(@Param("email") String email, @Param("password") String password,
			@Param("accountWith") String accountWith);

	List<User> findByGoogleId(String googleId);

	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.coins")
	List<User> findAllUsersWithCoins();

	@Query("SELECT new com.example.demo.dto.UserCheckOutDTO(u.id, u.name, u.email, co.date, co.status, qr.money , qr.id, co.id, qr.discountPercentage) "
			+ "FROM User u " + "JOIN u.checkOutCoins co " + "JOIN co.qrCoins qr")
	List<UserCheckOutDTO> findUsersWithCheckOutAndQRCode();

	@Query("SELECT COUNT(u) FROM User u")
	long countTotalUsers();

	@Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.coins WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<User> searchUsersWithCoins(@Param("name") String name);

	@Query("SELECT new com.example.demo.dto.ListCoinDTO(u.name, u.email, SUM(coc.money), SUM(coc.money) / 1000) "
			+ "FROM User u " + "JOIN Coins c ON u.id = c.user.id " + "JOIN CheckOutCoins coc ON u.id = coc.user.id "
			+ "GROUP BY u.id, u.name, u.email")
	List<ListCoinDTO> findUserCoinsData();

}
