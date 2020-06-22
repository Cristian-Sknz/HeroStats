package me.skiincraft.api.paladins.common;

import java.util.Date;

public class ChampionRank {

	private Champion champion;
	private int assists;
	private int kills;
	private int deaths;
	private Date lastplayed;
	private int wins;
	private int losses;
	
	private int championLevel;
	private int minutes;

	private int minionKills;
	private long worshippers;
	private int championId;
	private String championName;
	
	public ChampionRank(Champion champion, int assists, int kills, int deaths, Date lastplayed, int wins, int losses,
			int championLevel, int minutes, int minionKills, long worshippers, int championId, String championName) {
		this.champion = champion;
		this.assists = assists;
		this.kills = kills;
		this.deaths = deaths;
		this.lastplayed = lastplayed;
		this.wins = wins;
		this.losses = losses;
		this.championLevel = championLevel;
		this.minutes = minutes;
		this.minionKills = minionKills;
		this.worshippers = worshippers;
		this.championId = championId;
		this.championName = championName;
	}
	public Champion getChampion() {
		return champion;
	}
	public int getAssists() {
		return assists;
	}
	public int getKills() {
		return kills;
	}
	public int getDeaths() {
		return deaths;
	}
	public Date getLastplayed() {
		return lastplayed;
	}
	public int getWins() {
		return wins;
	}
	public int getLosses() {
		return losses;
	}
	public int getChampionLevel() {
		return championLevel;
	}
	public int getMinutes() {
		return minutes;
	}
	public int getMinionKills() {
		return minionKills;
	}
	public long getWorshippers() {
		return worshippers;
	}
	public int getChampionId() {
		return championId;
	}
	public String getChampionName() {
		return championName;
	}
	@Override
	public String toString() {
		return "ChampionRank [champion=" + champion.getChampionName() + ", assists=" + assists + ", kills=" + kills + ", deaths=" + deaths
				+ ", lastplayed=" + lastplayed + ", wins=" + wins + ", losses=" + losses + ", championLevel="
				+ championLevel + ", minutes=" + minutes + ", minionKills=" + minionKills + ", worshippers="
				+ worshippers + ", championId=" + championId + ", championName=" + championName + "]";
	}
	
	
}

