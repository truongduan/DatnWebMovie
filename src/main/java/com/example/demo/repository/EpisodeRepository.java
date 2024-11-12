package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.FirmDto;
import com.example.demo.model.Comment;
import com.example.demo.model.Episode;
import com.example.demo.model.Firm;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
	@Query("SELECT e FROM Episode e WHERE e.status = true")
	List<Episode> findAllActiveEpisodes();

	Optional<Episode> findById(Long id);

	@Query("SELECT new com.example.demo.dto.FirmDto(f.id, COALESCE((SELECT COUNT(e) FROM Episode e WHERE e.firm.id = f.id and e.status = true), 0), f.coins_video, f.total_episodes, f.img_firm, f.name_firm, f.firmdate, f.link_video_traller) "
			+ "FROM Firm f where f.status = true ")
	List<FirmDto> getEpisodesByFirm();

	@Query("SELECT new com.example.demo.dto.FirmDto(f.id, "
			+ "(SELECT COUNT(e) FROM Episode e WHERE e.firm.id = f.id and e.status = true), "
			+ "f.coins_video, f.total_episodes, f.img_firm, f.name_firm, f.firmdate, "
			+ "f.link_video_traller, e.link_video, e.name_episode, f.author_firm, c.category , e.status) "
			+ "FROM Firm f " + "LEFT JOIN Episode e ON e.firm.id = f.id " + "JOIN Category c ON c.id = f.category.id "
			+ "WHERE f.id = :firm_id AND f.status = true")
	List<FirmDto> getEpisodesByFirm(@Param("firm_id") Long firm_id);

	List<Episode> findByFirm(Firm firm);

}
