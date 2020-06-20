package me.skiincraft.api.paladins.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.enums.Rarity;
import me.skiincraft.api.paladins.objects.Card;

public class JsonChampionCards {
	
	private String json;
	
	public JsonChampionCards(String json) {
		this.json = json;
	}
	
	public JsonChampionCards(JsonElement json) {
		this.json = json.toString();
	}
	
	private static JsonElement getElement() {
		try {
			InputStream in = new FileInputStream(new File("getchampioncards.json"));
			Reader reader = new InputStreamReader(in);
			JsonElement ola = new JsonParser().parse(reader);
			return ola;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Card> cardJsonParse() {
		JsonElement elementjson = (json == null) ? getElement() : new JsonParser().parse(json);
		JsonArray jsonarray = elementjson.getAsJsonArray();
		List<Card> cards = new ArrayList<Card>();
		for (JsonElement ele : jsonarray) {
			JsonObject ob = ele.getAsJsonObject();
			
			Rarity rarity = null;
			for (Rarity r : Rarity.values()) {
				if (r.name().equalsIgnoreCase(ob.get("rarity").getAsString())) {
					rarity = r;
				}
			}
			
			String ret_msg = ob.get("ret_msg").toString();
			float multiply = cardValueMultiplicator(ob.get("card_description").getAsString());
			String multiplystring = String.valueOf(multiply).replace(".0", "");
			String carddescription = ob.get("card_description").getAsString().replace("{scale=" + multiplystring + "|" + multiplystring + "}", "{multiply}");
			Card card;
			card = new Card(ob.get("card_name").getAsString(), 
					ob.get("card_name_english").getAsString(), 
					ob.get("card_id1").getAsInt(), 
					ob.get("card_id2").getAsInt(), 
					carddescription, 
					ob.get("exclusive").getAsString().contains("y"), 
					ob.get("rank").getAsInt(), 
					rarity,  
					ob.get("recharge_seconds").getAsInt(),
					multiply,
					ob.get("championIcon_URL").getAsString(), 
					null, 
					ret_msg);
			
			cards.add(card);
		}
		
		return cards;
	}
	
	private float cardValueMultiplicator(String cardDescription) {
		int i = 0;
		int chaveinicial = 0;
		int chavefinal = 0;
		int middle = 0;
		for (char ch : cardDescription.toCharArray()) {
			if (ch == '{') {
				chaveinicial = i;
			}
			if (ch == '}') {
				chavefinal = i+1;
			}
			i++;
		}
		String desc = cardDescription.substring(chaveinicial, chavefinal);
		if (desc.length() == 0 || desc.length() == 1) {
			return 0;
		}
		String splited = desc.split("=")[1];
		for (char ch : splited.toCharArray()) {
			if (ch == '|') {
				break;
			}
			middle++;
		}
		return Float.valueOf(splited.substring(0, middle));
	}

}
