package me.skiincraft.api.paladins.matches;

import java.util.Date;

import me.skiincraft.api.paladins.objects.LeagueSeason;

public class LivePlayer {

	private LiveMatchChampion champion;
	private int championsPlayed;
	private int accountLevel;
	private Date playerCreated;
	private int playerId;
	private String playerName;
	private String region;
	private LeagueSeason tier;
	
	
	
	public LivePlayer(LiveMatchChampion champion, int championsPlayed, int accountLevel, Date playerCreated, int playerId, String playerName,
			String region, LeagueSeason tier) {
		this.championsPlayed = championsPlayed;
		this.champion = champion;
		this.accountLevel = accountLevel;
		this.playerCreated = playerCreated;
		this.playerId = playerId;
		this.playerName = playerName;
		this.region = region;
		this.tier = tier;
	}
	
	public LiveMatchChampion getChampion() {
		return champion;
	}
	
	public int getChampionsPlayed() {
		return championsPlayed;
	}
	public int getAccountLevel() {
		return accountLevel;
	}
	public Date getPlayerCreated() {
		return playerCreated;
	}
	public int getPlayerId() {
		return playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public String getRegion() {
		return region;
	}
	public LeagueSeason getTier() {
		return tier;
	}

	@Override
	public String toString() {
		return "LivePlayer [champion=" + champion.getChampionName() + ", championsPlayed=" + championsPlayed + ", accountLevel="
				+ accountLevel + ", playerCreated=" + playerCreated + ", playerId=" + playerId + ", playerName="
				+ playerName + ", region=" + region + ", tier=" + tier + "]";
	} 
	
	
	
}
