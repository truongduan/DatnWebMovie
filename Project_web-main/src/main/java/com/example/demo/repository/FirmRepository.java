package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Category;
import com.example.demo.model.Firm;

public interface FirmRepository extends JpaRepository<Firm, Long> {

	@Query("SELECT f FROM Firm f WHERE f.status = true")
	List<Firm> getActiveFirms();

	@Query("SELECT f FROM Firm f WHERE f.status = true")
	List<Firm> findAll();

	Optional<Firm> findById(Long id);

	@Query("SELECT f FROM MovieFirm mf JOIN mf.firm f GROUP BY f ORDER BY SUM(mf.view_number) DESC")
	List<Firm> getTop5MostViewedFirms();

	@Query("SELECT LOWER(SUBSTRING(f.name_firm, 1, LOCATE(' ', f.name_firm) - 1)) AS firmName " + "FROM Firm f "
			+ "WHERE f.name_firm LIKE '% %'")
	List<String> getFirmNamesWithCondition();

	@Query("SELECT f FROM Firm f WHERE f.name_firm = :name_firm")
	List<Firm> findByName_firm(String name_firm);

	@Query("SELECT f FROM Firm f WHERE f.name_firm = :name_firm and f.status = true")
	List<Firm> findByName_firmandActive(String name_firm);

	@Query("SELECT COUNT(f) FROM Firm f")
	long countTotalFirms();

	@Query("SELECT DISTINCT new com.example.demo.dto.FirmDto(f.id, "
			+ "(SELECT COUNT(e) FROM Episode e WHERE e.firm.id = f.id and e.status =true), "
			+ "f.coins_video, f.total_episodes, f.img_firm, f.name_firm, f.firmdate, "
			+ "f.link_video_traller, MIN(e.link_video), MIN(e.name_episode), " // Lấy tập đầu tiên và video của nó
			+ "f.author_firm, c.category, e.status) " + "FROM Firm f " + "LEFT JOIN Episode e ON e.firm.id = f.id "
			+ "JOIN Category c ON c.id = f.category.id "
			+ "WHERE f.category = :category AND f.status = true and e.status = true "
			+ "GROUP BY f.id, f.coins_video, f.total_episodes, f.img_firm, f.name_firm, f.firmdate, "
			+ "f.link_video_traller, f.author_firm, c.category, e.status")
	List<FirmDto> findByCategory(@Param("category") Category category);

	@Query("SELECT COUNT(f) FROM Firm f WHERE LOWER(f.name_firm) = LOWER(:name_firm)")
	long countByName_firm(@Param("name_firm") String name_firm);

}