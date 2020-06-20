package me.skiincraft.api.paladins.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.entity.PaladinsMatch;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.matches.MatchChampion;
import me.skiincraft.api.paladins.matches.MatchPlayer;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.objects.Kills;
import me.skiincraft.api.paladins.objects.LeagueSeason;
import me.skiincraft.api.paladins.utils.JsonUtils;
import me.skiincraft.api.paladins.utils.PaladinsUtils;

public class PaladinsMatchBuilder implements PaladinsMatch {

	public JsonObject object;
	public JsonArray array;
	public Paladins paladins;
	
	public PaladinsMatchBuilder(String json, Paladins paladins) {
		array = new JsonParser().parse(json).getAsJsonArray();
		object = new JsonParser().parse(json).getAsJsonArray().get(0).getAsJsonObject();
		this.paladins = paladins;
	}

	@Override
	public List<MatchPlayer> getPlayers() {
		List<MatchPlayer> lista = new ArrayList<MatchPlayer>();
		for (JsonElement e : array) {
			JsonObject ob = e.getAsJsonObject();
			
			Champion original = null;
			for (Champion champ : paladins.getQueue().getLoadedchampions()) {
				if (champ.getChampionId() == ob.get("ChampionId").getAsInt()) {
					original = champ;
					break;
				}
			}
			
			List<Card> cardPurch = new ArrayList<Card>();
			
			for (Card c : original.getCardsPT()) {
				for (int i = 1; i < 6;i++) {
					if (c.getCardId2() == ob.get("ItemId" + i).getAsInt()) {
						cardPurch.add(c);
					}
				}
			}
			Kills killsDetails = new Kills(
					JsonUtils.getInt(ob, "Multi_kill_Max"),
					JsonUtils.getInt(ob, "Kills_First_Blood"),
					JsonUtils.getInt(ob, "Kills_Single"),
					JsonUtils.getInt(ob, "Kills_Double"),
					JsonUtils.getInt(ob, "Kills_Triple"),
					JsonUtils.getInt(ob, "Kills_Quadra"),
					JsonUtils.getInt(ob, "Kills_Penta"),
					JsonUtils.getInt(ob, "Kills_Gold_Fury"),
					JsonUtils.getInt(ob, "Kills_Phoenix"),
					JsonUtils.getInt(ob, "Kills_Siege_Juggernaut"),
					JsonUtils.getInt(ob, "Kills_Wild_Juggernaut"));
			
			MatchPlayer player = new MatchPlayer(
					JsonUtils.getInt(ob, "Account_Level"),
					JsonUtils.getInt(ob, "Assists"),
					new MatchChampion(original, cardPurch, ob.get("Skin").getAsString(), ob.get("SkinId").getAsInt(), paladins),
					JsonUtils.getLong(ob, "Damage_Bot"),
					JsonUtils.getLong(ob, "Damage_Done_In_Hand"),
					JsonUtils.getLong(ob, "Damage_Done_Magical"),
					JsonUtils.getLong(ob, "Damage_Done_Physical"),
					JsonUtils.getLong(ob, "Damage_Mitigated"),
					JsonUtils.getLong(ob, "Damage_Player"),
					JsonUtils.getLong(ob, "Damage_Taken"),
					JsonUtils.getLong(ob, "Damage_Taken_Magical"),
					JsonUtils.getLong(ob, "Damage_Taken_Physical"),
					killsDetails,
					JsonUtils.getInt(ob, "Killing_Spree"),
					JsonUtils.getInt(ob, "Kills_Player") + JsonUtils.getInt(ob, "Kills_Bot"),
					JsonUtils.getInt(ob, "Deaths"),
					JsonUtils.getInt(ob, "Gold_Earned"),
					JsonUtils.getInt(ob, "Gold_Per_Minute"),
					JsonUtils.getLong(ob, "Healing"),
					JsonUtils.getLong(ob, "Healing_Player_Self"),
					this,
					JsonUtils.getLong(ob ,"playerId"),
					JsonUtils.get(ob ,"Region"),
					JsonUtils.get(ob ,"playerName"),
					new LeagueSeason(
							JsonUtils.getInt(ob, "League_Wins"),
							JsonUtils.getInt(ob, "League_Losses"),
							JsonUtils.getInt(ob, "League_Points"),
							Tier.getTierById(JsonUtils.getInt(ob, "League_Tier"))),
					JsonUtils.getInt(ob ,"playerPortalId"),
					JsonUtils.getLong(ob ,"playerPortalUserId"),
					PaladinsUtils.getActiveItens(),
					JsonUtils.get(ob,"Win_Status").contains("Winner"));
			lista.add(player);
		}
		
		return null;
	}

	@Override
	public String getWinner() {
		return (getWinnerTeam() == 1) ? "Blue" : "Red";
	}

	@Override
	public List<Champion> getBans() {
		List<Champion> banslist = new ArrayList<Champion>();

		for (Champion champ : paladins.getQueue().getLoadedchampions()) {
			if (object.get("BanId").getAsInt() == 0) {
				break;
			}
			for (int i = 1; i < 5;i++) {
				if (champ.getChampionId() == object.get("BanId" + i).getAsInt()) {
					banslist.add(champ);
				}	
			}
		}
		return banslist;
	}

	@Override
	public long getMatchId() {
		return object.get("Match").getAsLong();
	}

	@Override
	public long getMatchDuration() {
		return TimeUnit.SECONDS.toMillis(object.get("Match").getAsLong());
	}

	@Override
	public String getMapGame() {
		return object.get("Map_Game").getAsString();
	}

	@Override
	public int getMatchMinutes() {
		return object.get("Minutes").getAsInt();
	}

	@Override
	public int getObjectiveAssists() {
		return object.get("Objective_Assists").getAsInt();
	}

	@Override
	public int getTeam1Score() {
		return object.get("Team1Score").getAsInt();
	}

	@Override
	public int getTeam2Score() {
		return object.get("Team2Score").getAsInt();
	}

	@Override
	public List<MatchPlayer> getTeam1Players() {
		List<MatchPlayer> players = new ArrayList<MatchPlayer>();
		if (getTeam1Score() > getTeam2Score()) {
			for (MatchPlayer player : getPlayers()) {
				if (player.HasWin()) {
					players.add(player);
				}
			}
			return players;
		} else {
			for (MatchPlayer player : getPlayers()) {
				if (!player.HasWin()) {
					players.add(player);
				}
			}
			return players;
		}
	}

	@Override
	public List<MatchPlayer> getTeam2Players() {
		List<MatchPlayer> players = new ArrayList<MatchPlayer>();
		if (getTeam2Score() > getTeam1Score()) {
			for (MatchPlayer player : getPlayers()) {
				if (player.HasWin()) {
					players.add(player);
				}
			}
			return players;
		} else {
			for (MatchPlayer player : getPlayers()) {
				if (!player.HasWin()) {
					players.add(player);
				}
			}
			return players;
		}
	}

	@Override
	public int getWinnerTeam() {
		return (getTeam1Score() > getTeam2Score()) ? 1 : 2;
	}

	@Override
	public boolean hasReplay() {
		return object.get("hasReplay").getAsString().equalsIgnoreCase("y");
	}

	@Override
	public int getMatchQueueId() {
		return object.get("match_queue_id").getAsInt();
	}

	@Override
	public String getGamemode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ret_msg() {
		return object.get("ret_msg").getAsString();
	}

	@Override
	public JsonObject getJsonObject() {
		return getJsonObject();
	}
	
}
