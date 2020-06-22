package me.skiincraft.api.paladins.parser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.objects.LoadoutItems;
import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.common.ChampionLoadouts;

public class JsonPaladinsLoadouts {
		
	private String json;
	private Queue queue;
	
	public JsonPaladinsLoadouts(String json, Queue queue) {
		this.json = json;
		this.queue = queue;
	}
	
	public List<ChampionLoadouts> loadoutsJsonParser(){
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		List<ChampionLoadouts> loadouts = new ArrayList<ChampionLoadouts>();
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			
			List<Champion> champlist = (queue.getLoadedchampions().size() != 0) 
					? queue.getLoadedchampions() : queue.refreshChampions();
			
			Champion champion = null;
			int championId = object.get("ChampionId").getAsInt(); 
			for (Champion champ : champlist) {
				if (champ.getChampionId() == championId) {
					champion = champ;
				}
			}
			
			
			JsonArray arrayitems = object.get("LoadoutItems").getAsJsonArray();
			
			ChampionLoadouts champl = new ChampionLoadouts(champion,
					object.get("DeckName").getAsString(),
					object.get("DeckId").getAsInt(),
					loadoutitems(arrayitems),
					championId,
					object.get("DeckName").getAsString());
			loadouts.add(champl);

		}
		return loadouts;
	}
	
	private List<LoadoutItems> loadoutitems(JsonArray array) {
		List<LoadoutItems> itens = new ArrayList<LoadoutItems>();
		
		for (JsonElement ele : array) {
			JsonObject object = ele.getAsJsonObject();
			itens.add(new LoadoutItems(object.get("ItemId").getAsInt(),
					object.get("ItemName").getAsString(), object.get("Points").getAsInt()));
		}
		return itens;
		
	}
}
