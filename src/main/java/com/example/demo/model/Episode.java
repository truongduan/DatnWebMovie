package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "episode")
public class Episode {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Tác phim không được để trống")
	@ManyToOne
	@JoinColumn(name = "id_firm")
	private Firm firm;

	private String link_video;

	@NotNull(message = "Tên tập không được để trống")
	private String name_episode;

	private boolean status;

	@Override
	public String toString() {
		return "Episode [id=" + id + ", firm=" + (firm != null ? firm.getName_firm() : null) + ", link_video="
				+ link_video + ", name_episode=" + name_episode + ", status=" + status + "]";
	}
}
