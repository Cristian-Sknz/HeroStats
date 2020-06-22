package me.skiincraft.api.paladins.entity;

import java.util.List;

import me.skiincraft.api.paladins.enums.PaladinsQueue;
import me.skiincraft.api.paladins.matches.LivePlayer;

public class LiveMatch {
	
	private List<LivePlayer> player;
	private int matchId;
	private PaladinsQueue queue;
	private String mapGame;
	
	public LiveMatch(List<LivePlayer> player, int matchId, PaladinsQueue queue,
			String mapGame) {
		this.player = player;
		this.matchId = matchId;
		this.queue = queue;
		this.mapGame = mapGame;
	}
	public List<LivePlayer> getPlayer() {
		return player;
	}
	public int getMatchId() {
		return matchId;
	}
	public PaladinsQueue getQueue() {
		return queue;
	}
	public String getMapGame() {
		return mapGame;
	}
	@Override
	public String toString() {
		return "LiveMatch [player=" + player + ", matchId=" + matchId + ", queue=" + queue + ", mapGame=" + mapGame
				+ "]";
	}
	
	

	
}
