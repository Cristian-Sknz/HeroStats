package me.skiincraft.discord.herostats.session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.skiincraft.discord.core.sqlite.Database;
import me.skiincraft.discord.core.sqlobjects.DBObject;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class SessionDB extends Database {

	private String get = "last";
	
	public SessionDB() {
		super(HeroStatsBot.getMain().getPlugin());
	}

	public String databaseName() {
		return "sessions";
	}

	public DBObject dbObject() {
		return new DBObject("order", get);
	}
	
	public void changeGet(String string) {
		this.get = string;
	}

	public Map<String, String> tableValues() {
		Map<String, String> map = new HashMap<>();
		map.put("order", "last");
		map.put("session", "lastsessionhere");
		map.put("date", new SimpleDateFormat("dd/MM/yyy HH.mm.ss").format(new Date()));
		
		return map;
	}
	
	public void setSession(String sessionId) {
		set("session", sessionId);
	}
	
	public String getLastSession() {
		get = "last";
		return get("session");
	}
	
	@Deprecated
	public void set(String column, String value) {
		if (column == "session") {
			super.set("order", "oldest");
		}
		super.set(column, value);
	}

}
