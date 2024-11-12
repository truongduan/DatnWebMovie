package com.example.demo.dto;

import java.time.LocalDateTime;

public class FirmDto {
	private Long id;
	private Long episodeCount;
	private Double coins;
	private Integer total_episodes;
	private String img_link;
	private String name_firm;
	private LocalDateTime firmdate;
	private String link_video_traller;
	private String link_video;
	private String name_episode;
	private String author_firm;
	private String category;
	private Boolean status;

	public FirmDto() {
		super();
	}

	public FirmDto(Long id, Long episodeCount, Double coins, Integer total_episodes, String img_link, String name_firm,
			LocalDateTime firmdate, String link_video_traller) {
		super();
		this.id = id;
		this.episodeCount = episodeCount;
		this.coins = coins;
		this.total_episodes = total_episodes;
		this.img_link = img_link;
		this.name_firm = name_firm;
		this.firmdate = firmdate;
		this.link_video_traller = link_video_traller;
	}

	public FirmDto(Long id, Long episodeCount, Double coins, Integer total_episodes, String img_link, String name_firm,
			LocalDateTime firmdate, String link_video_traller, String link_video, String name_episode,
			String author_firm, String category, Boolean status) {
		super();
		this.id = id;
		this.episodeCount = episodeCount;
		this.coins = coins;
		this.total_episodes = total_episodes;
		this.img_link = img_link;
		this.name_firm = name_firm;
		this.firmdate = firmdate;
		this.link_video_traller = link_video_traller;
		this.link_video = link_video;
		this.name_episode = name_episode;
		this.author_firm = author_firm;
		this.category = category;
		this.status = status;
	}

	public Boolean isStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAuthor_firm() {
		return author_firm;
	}

	public void setAuthor_firm(String author_firm) {
		this.author_firm = author_firm;
	}

	public String getLink_video() {
		return link_video;
	}

	public void setLink_video(String link_video) {
		this.link_video = link_video;
	}

	public String getName_episode() {
		return name_episode;
	}

	public void setName_episode(String name_episode) {
		this.name_episode = name_episode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEpisodeCount() {
		return episodeCount;
	}

	public void setEpisodeCount(Long episodeCount) {
		this.episodeCount = episodeCount;
	}

	public Double getCoins() {
		return coins;
	}

	public void setCoins(Double coins) {
		this.coins = coins;
	}

	public Integer getTotal_episodes() {
		return total_episodes;
	}

	public void setTotal_episodes(Integer total_episodes) {
		this.total_episodes = total_episodes;
	}

	public String getImg_link() {
		return img_link;
	}

	public void setImg_link(String img_link) {
		this.img_link = img_link;
	}

	public String getName_firm() {
		return name_firm;
	}

	public void setName_firm(String name_firm) {
		this.name_firm = name_firm;
	}

	public LocalDateTime getFirmdate() {
		return firmdate;
	}

	public void setFirmdate(LocalDateTime firmdate) {
		this.firmdate = firmdate;
	}

	public String getLink_video_traller() {
		return link_video_traller;
	}

	public void setLink_video_traller(String link_video_traller) {
		this.link_video_traller = link_video_traller;
	}

}
