package me.skiincraft.api.paladins.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.common.ChampionRank;
import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;

public class JsonChampionRanks {
		
	private String json;
	private Queue queue;
	
	public JsonChampionRanks(String json, Queue queue) {
		this.json = json;
		this.queue = queue;
	}
	
	public List<ChampionRank> ranksJsonParser(){
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		List<ChampionRank> ranks = new ArrayList<ChampionRank>();
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			
			List<Champion> champlist = (queue.getLoadedchampions().size() != 0) 
					? queue.getLoadedchampions() : queue.refreshChampions();
			
			Champion champion = null;
			int championId = object.get("champion_id").getAsInt(); 
			for (Champion champ : champlist) {
				if (champ.getChampionId() == championId) {
					champion = champ;
				}
			}
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
			
			try {
				ranks.add(new ChampionRank(champion,
						object.get("Assists").getAsInt(), object.get("Kills").getAsInt(),
						object.get("Deaths").getAsInt(),
						df.parse(object.get("LastPlayed").getAsString().replace("\\", "")),
						object.get("Wins").getAsInt(), object.get("Losses").getAsInt(),
						object.get("Rank").getAsInt(), object.get("Minutes").getAsInt(),
						object.get("MinionKills").getAsInt(),
						object.get("Worshippers").getAsLong(),
						championId, object.get("champion").getAsString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return ranks;
	}
}
