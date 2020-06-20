package me.skiincraft.api.paladins.parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.enums.DamageType;
import me.skiincraft.api.paladins.objects.Ability;

public class JsonChampions {

	private String jsonPt;
	private String jsonEn;
	private Queue queue;
	
	public JsonChampions(String jsonPt, String jsonEn, Queue queue) { 
		this.jsonPt = jsonPt;
		this.jsonEn = jsonEn;
		this.queue = queue;
	}
	
	public List<Champion> refreshchampions() {
		JsonArray arrayPt = (jsonPt == null) ? getElement("pt").getAsJsonArray()
				: new JsonParser().parse(jsonPt).getAsJsonArray();
		
		JsonArray arrayEn = (jsonEn == null) ? getElement("en").getAsJsonArray()
				: new JsonParser().parse(jsonPt).getAsJsonArray();
		List<Champion> champions = new ArrayList<Champion>();

		for (JsonElement a : arrayPt) {
			Champion champion = null;
			for (JsonElement b : arrayEn) {
			JsonObject object = a.getAsJsonObject();
			JsonObject objectb = b.getAsJsonObject();
			
			List<Ability> abilityPT = new ArrayList<Ability>();
			List<Ability> abilityEN = new ArrayList<Ability>();
			
			for (int i = 1; i < 6; i++) {
				JsonObject h = object.getAsJsonObject().get("Ability_" + i).getAsJsonObject();
				JsonObject h2 = objectb.getAsJsonObject().get("Ability_" + i).getAsJsonObject();
				
				Ability aPt = new Ability(object.get("Ability" + i).getAsString(),
						h.get("Description").getAsString(),
						h.get("Id").getAsInt(),
						object.get("ChampionAbility" + i + "_URL").getAsString(),
						DamageType.getByOriginal(h.get("damageType").getAsString()),
						h.get("rechargeSeconds").getAsInt(),
						(h.get("rechargeSeconds").getAsInt() != 0));
				
				Ability aEn = new Ability(objectb.get("Ability" + i).getAsString(),
						h2.get("Description").getAsString(),
						h.get("Id").getAsInt(),
						object.get("ChampionAbility" + i + "_URL").getAsString(),
						DamageType.getByOriginal(h.get("damageType").getAsString()),
						h.get("rechargeSeconds").getAsInt(),
						(h.get("rechargeSeconds").getAsInt() != 0));
				
				abilityPT.add(aPt);
				abilityEN.add(aEn);
			}
			
			champion = new Champion(object.get("id").getAsInt(),
					object.get("Name").getAsString(),
					object.get("Name_English").getAsString(),
					object.get("ChampionIcon_URL").getAsString(),
					object.get("Title").getAsString(),
					objectb.get("Title").getAsString(),
					object.get("Roles").getAsString(),
					objectb.get("Roles").getAsString(),
					object.get("Lore").getAsString(),
					objectb.get("Lore").getAsString(),
					object.get("Health").getAsInt(),
					object.get("Speed").getAsInt(),
					abilityPT,
					abilityEN,
					queue.getPaladinsAPI());
			}
			champions.add(champion);
		}
		return champions;
	}
	
	public void championsLocalJsonParse() {
		
	}

	public static int getLastCharAt(char regex, String str) {
		int i = 0;
		int achou = 0;
		for (char c : str.toCharArray()) {
			if (c == regex) {
				achou = i + 1;
			}
			i++;
		}
		return achou;
	}

	public static void generateImage(String url, String filename, String path) {
		try {
			BufferedImage image;
			image = ImageIO.read(new URL(url));
			File file = new File(path + filename + ".png");
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JsonElement getElement(String lang) {
		try {
			String filename = (lang.toLowerCase() == "pt")? "championsrawPt.json" : "championsrawEn.json";
			InputStream in = new FileInputStream(new File(Paladins.CHAMPIONS_PATH + filename));
			Reader reader = new InputStreamReader(in);
			JsonElement ola = new JsonParser().parse(reader);
			return ola;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
