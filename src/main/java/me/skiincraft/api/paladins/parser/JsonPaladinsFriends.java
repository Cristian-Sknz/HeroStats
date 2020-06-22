package me.skiincraft.api.paladins.parser;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.entity.PaladinsFriend;
import me.skiincraft.api.paladins.entity.PaladinsPlayer;
import me.skiincraft.api.paladins.exceptions.PlayerNotFoundException;

public class JsonPaladinsFriends {
	
	private String json;
	private Queue queue;
	
	public JsonPaladinsFriends(String json, Queue queue) {
		this.json = json;
		this.queue = queue;
	}
	
	public List<PaladinsFriend> friendJsonParser() {
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		List<PaladinsFriend> friends = new ArrayList<PaladinsFriend>();
		List<Integer> ids = new ArrayList<Integer>();
		
		int i = 0;
		for (JsonElement ele : array) {
			if (i == 19) break;
			JsonObject object = ele.getAsJsonObject();
			int accoundId = object.get("account_id").getAsInt();
			if (accoundId == 0)continue;
			
			ids.add(object.get("player_id").getAsInt());
			i++;
		}
		
		Integer[] id = new Integer[ids.size()];
		ids.toArray(id);
		
		List<PaladinsPlayer> players = null;
		try {
			players = queue.getPlayerBatch(id);
		} catch (PlayerNotFoundException e) {
			e.printStackTrace();
		}
		i = 0;
		for (JsonElement ele : array) {
			if (i == 19) break;
			JsonObject object = ele.getAsJsonObject();
			int accoundId = object.get("account_id").getAsInt();
			if (accoundId == 0)continue;
			
			new PaladinsFriend(accoundId,
					object.get("friend_flags").getAsInt(),
					object.get("name").getAsString(),
					object.get("player_id").getAsInt(),
					object.get("status").getAsString(),
					players.get(i),
					queue);
		}
		return friends;
	}

}
