package me.skiincraft.api.paladins.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.entity.PaladinsPlayer;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.objects.LeagueSeason;
import me.skiincraft.api.paladins.objects.Team;
import me.skiincraft.api.paladins.objects.TierResults;
import me.skiincraft.api.paladins.ranked.RankedConquest;
import me.skiincraft.api.paladins.ranked.RankedController;
import me.skiincraft.api.paladins.ranked.RankedKBM;

public class PaladinsPlayerBuilder implements PaladinsPlayer {

	public JsonObject object;
	
	public PaladinsPlayerBuilder(String json) {
		object = new JsonParser().parse(json).getAsJsonArray().get(0).getAsJsonObject();
	}
	
	@Override
	public int getActivePlayerId() {
		return object.get("ActivePlayerId").getAsInt();
	}

	@Override
	public int getAvatarId() {
		return object.get("AvatarId").getAsInt();
	}

	@Override
	public String getAvatarURL() {
		return object.get("AvatarURL").getAsString().replace("/", "");
	}

	@Override
	public Date getCreated() {
		String date = object.get("Created_Datetime").getAsString().replace("\\", "");
		DateFormat inFormat3 = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss aa");
		try {
			return inFormat3.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getHoursPlayed() {
		return object.get("HoursPlayed").getAsInt();
	}

	@Override
	public int getId() {
		return object.get("Id").getAsInt();
	}

	@Override
	public Date getLastLogin() {
		String date = object.get("Last_Login_Datetime").getAsString().replace("\\", "");
		DateFormat inFormat3 = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss aa");
		try {
			return inFormat3.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getLeaves() {
		return object.get("Leaves").getAsInt();
	}

	@Override
	public int getLevel() {
		return object.get("Level").getAsInt();
	}

	@Override
	public String getLoadingFrame() {
		return object.get("LoadingFrame").getAsString();
	}

	@Override
	public int getLosses() {
		return object.get("Losses").getAsInt();
	}

	@Override
	public int getMaestryLevel() {
		return object.get("MasteryLevel").getAsInt();
	}

	@Override
	public Object getMergedPlayers() {
		return null;
	}

	@Override
	public int getMinutesPlayed() {
		return object.get("MinutesPlayed").getAsInt();
	}

	@Override
	public String getName() {
	   return object.get("Name").getAsString();
	}

	@Override
	public String getPersonalStatusMessage() {
		return object.get("Personal_Status_Message").getAsString();
	}

	@Override
	public Platform getPlatform() {
		String plataforma = object.get("Platform").getAsString();
		for (Platform plat : Platform.values()) {
			if (plat.name().equalsIgnoreCase(plataforma)) {
				return plat;
			}
		}
		return Platform.Steam;
	}

	@Override
	public RankedConquest getRankedConquest() {
		JsonObject r = object.getAsJsonObject("RankedConquest");
		LeagueSeason l = new LeagueSeason(r.get("Wins").getAsInt(),
				r.get("Losses").getAsInt(),
				r.get("Points").getAsInt(),
				Tier.getTierById(r.get("Tier").getAsInt()));
		
		return new RankedConquest(l, 
				r.get("Leaves").getAsInt(), 
				r.get("PrevRank").getAsInt(), 
				r.get("Rank").getAsInt(), 
				r.get("Season").getAsInt(), 
				r.get("Trend").getAsInt(), 
				r.get("player_id").getAsInt(), r.get("ret_msg").getAsString());
	}

	@Override
	public RankedController getRankedController() {
		JsonObject r = object.getAsJsonObject("RankedController");
		LeagueSeason l = new LeagueSeason(r.get("Wins").getAsInt(),
				r.get("Losses").getAsInt(),
				r.get("Points").getAsInt(),
				Tier.getTierById(r.get("Tier").getAsInt()));
		
		return new RankedController(l, 
				r.get("Leaves").getAsInt(), 
				r.get("PrevRank").getAsInt(), 
				r.get("Rank").getAsInt(), 
				r.get("Season").getAsInt(), 
				r.get("Trend").getAsInt(), 
				r.get("player_id").getAsInt(), r.get("ret_msg").getAsString());
	}

	@Override
	public RankedKBM getRankedKBM() {
		JsonObject r = object.getAsJsonObject("RankedKBM");
		LeagueSeason l = new LeagueSeason(r.get("Wins").getAsInt(),
				r.get("Losses").getAsInt(),
				r.get("Points").getAsInt(),
				Tier.getTierById(r.get("Tier").getAsInt()));
		
		return new RankedKBM(l, 
				r.get("Leaves").getAsInt(), 
				r.get("PrevRank").getAsInt(), 
				r.get("Rank").getAsInt(), 
				r.get("Season").getAsInt(), 
				r.get("Trend").getAsInt(), 
				r.get("player_id").getAsInt(), r.get("ret_msg").getAsString());
	}

	@Override
	public String getRegion() {
		return object.get("Brazil").getAsString();
	}

	@Override
	public Team getTeam() {
		return new Team(object.get("TeamId").getAsInt(), object.get("Team_Name").getAsString());
	}

	@Override
	public TierResults getTier() {
		return new TierResults(object.get("Tier_Conquest").getAsInt(),
				object.get("Tier_RankedController").getAsInt(),
				object.get("Tier_RankedKBM").getAsInt());
	}

	@Override
	public String getTitle() {
		return object.get("Title").getAsString();
	}

	@Override
	public int getAchievementsSize() {
		return object.get("Total_Achievements").getAsInt();
	}

	@Override
	public long getWorshippersSize() {
		return object.get("Total_Worshippers").getAsLong();
	}

	@Override
	public long getTotalXP() {
		return object.get("Total_XP").getAsLong();
	}

	@Override
	public int getWins() {
		return object.get("Wins").getAsInt();
	}

	@Override
	public String getPlayername() {
		return object.get("hz_player_name").getAsString();
	}

	@Override
	public String getGamerTag() {
		return object.get("hz_gamer_tag").getAsString();
	}

	@Override
	public String ret_msg() {
		return object.get("ret_msg").getAsString();
	}

	@Override
	public JsonObject getJsonObject() {
		return object;
	}

}
