package me.skiincraft.api.paladins.objects;

import me.skiincraft.api.paladins.enums.Tier;

public class LeagueSeason {
	
	private int wins;
	private int losses;
	private int points;
	private Tier tier;
	
	public LeagueSeason(int wins, int losses, int points, Tier tier) {
		super();
		this.wins = wins;
		this.losses = losses;
		this.points = points;
		this.tier = tier;
	}
	
	public int getWins() {
		return wins;
	}
	public int getLosses() {
		return losses;
	}
	public int getPoints() {
		return points;
	}
	public Tier getTier() {
		return tier;
	}
	
	

}
