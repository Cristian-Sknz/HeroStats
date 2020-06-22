package me.skiincraft.api.paladins.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.common.ChampionSkin;
import me.skiincraft.api.paladins.enums.Rarity;

public class JsonChampionSkins {
		
	private String json;
	private Queue queue;
	
	public JsonChampionSkins(String json, Queue queue) {
		this.json = json;
		this.queue = queue;
	}
	
	public List<ChampionSkin> skinJsonParser(){
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		List<ChampionSkin> skins = new ArrayList<ChampionSkin>();
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			
			List<Champion> champlist = (queue.getLoadedchampions().size() != 0) 
					? queue.getLoadedchampions() : queue.refreshChampions();
			
			Champion champion = null;
			String championname = object.get("champion_name").getAsString(); 
			for (Champion champ : champlist) {
				if (champ.getChampionName().equalsIgnoreCase(championname)) {
					champion = champ;
				}
			}
			
			skins.add(new ChampionSkin(champion,
					championname,
					Rarity.getRarityByName(object.get("rarity").getAsString()),
					object.get("skin_id1").getAsInt(),
					object.get("skin_id2").getAsInt(),
					object.get("skin_name").getAsString(),
					object.get("skin_name_english").getAsString()));
		}
		return skins;
	}
}
