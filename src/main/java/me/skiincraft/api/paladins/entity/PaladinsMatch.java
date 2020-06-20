package me.skiincraft.api.paladins.entity;

import java.util.List;

import com.google.gson.JsonObject;

import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.matches.MatchPlayer;

public interface PaladinsMatch {

	List<MatchPlayer> getPlayers();
	String getWinner();
	List<Champion> getBans();
	long getMatchId();
	long getMatchDuration();
	String getMapGame();
	int getMatchMinutes();
	int getObjectiveAssists();
	int getTeam1Score();
	int getTeam2Score();
	List<MatchPlayer> getTeam1Players();
	List<MatchPlayer> getTeam2Players();
	int getWinnerTeam();
	boolean hasReplay();
	int getMatchQueueId();
	String getGamemode();
	String ret_msg();
	
	JsonObject getJsonObject();
}
