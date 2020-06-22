package me.skiincraft.api.paladins;

import me.skiincraft.api.paladins.enums.Tier;

public class LeaderboardPlaces {
	
	private String username;
	private int wins;
	private int losses;
	private int leaves;
	private int points;
	
	private int season;
	private Tier tier;
	
	private int userId;
	
	private int trend;

	public LeaderboardPlaces(String username, int wins, int losses, int leaves, int points, int season, Tier tier,
			int userId, int trend) {
		this.username = (username != "") ?username: "???";
		this.wins = wins;
		this.losses = losses;
		this.leaves = leaves;
		this.points = points;
		this.season = season;
		this.tier = tier;
		this.userId = userId;
		this.trend = trend;
	}

	public String getUsername() {
		return username;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}

	public int getLeaves() {
		return leaves;
	}

	public int getPoints() {
		return points;
	}

	public int getSeason() {
		return season;
	}

	public Tier getTier() {
		return tier;
	}

	public int getUserId() {
		return userId;
	}

	public int getTrend() {
		return trend;
	}

	@Override
	public String toString() {
		return "LeaderboardPlaces [username=" + username + ", wins=" + wins + ", losses=" + losses + ", leaves="
				+ leaves + ", points=" + points + ", season=" + season + ", tier=" + tier + ", userId=" + userId
				+ ", trend=" + trend + "]";
	}

	

}
