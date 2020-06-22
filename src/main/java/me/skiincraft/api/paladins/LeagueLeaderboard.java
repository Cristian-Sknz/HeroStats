package me.skiincraft.api.paladins;

import java.util.ArrayList;
import java.util.List;

import me.skiincraft.api.paladins.enums.PaladinsQueue;
import me.skiincraft.api.paladins.enums.Tier;

public class LeagueLeaderboard {

	private List<LeaderboardPlaces> places = new ArrayList<LeaderboardPlaces>();

	private Tier tier;
	private PaladinsQueue queue;

	public LeagueLeaderboard(List<LeaderboardPlaces> places, Tier tier, PaladinsQueue queue) {
		this.places = places;
		this.tier = tier;
		this.queue = queue;
	}

	public List<LeaderboardPlaces> getPlaces() {
		return places;
	}
	
	public LeaderboardPlaces getPlaceByName(String name) {
		for (LeaderboardPlaces place: getPlaces()) {
			if (place.getUsername().equalsIgnoreCase(name)) {
				return place;
			}
		}
		return null;
	}

	public Tier getTier() {
		return tier;
	}

	public PaladinsQueue getQueue() {
		return queue;
	}

}
