package me.skiincraft.api.paladins.entity;

import java.util.Date;

import com.google.gson.JsonObject;

import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.objects.Team;
import me.skiincraft.api.paladins.objects.TierResults;
import me.skiincraft.api.paladins.ranked.RankedConquest;
import me.skiincraft.api.paladins.ranked.RankedController;
import me.skiincraft.api.paladins.ranked.RankedKBM;

public interface PaladinsPlayer {
	
	int getActivePlayerId();
	int getAvatarId();
	String getAvatarURL();
	Date getCreated();
	int getHoursPlayed();
	int getId();
	Date getLastLogin();
	int getLeaves();
	int getLevel();
	String getLoadingFrame();
	int getLosses();
	int getMaestryLevel();
	Object getMergedPlayers();
	int getMinutesPlayed();
	String getName();
	String getPersonalStatusMessage();
	Platform getPlatform();
	RankedConquest getRankedConquest();
	RankedController getRankedController();
	RankedKBM getRankedKBM();
	String getRegion();
	Team getTeam();
	TierResults getTier();
	String getTitle();
	int getAchievementsSize();
	long getWorshippersSize();
	long getTotalXP();
	int getWins();
	String getPlayername();
	String getGamerTag();
	String ret_msg();
	
	JsonObject getJsonObject();
}
