package me.skiincraft.api.paladins.utils;

import com.google.gson.JsonObject;

public class JsonUtils {
	
	public static String get(JsonObject ob, String campo) {
		return ob.get(campo).getAsString();
	}
	
	public static int getInt(JsonObject ob, String campo) {
		return ob.get(campo).getAsInt();
	}
	
	public static long getLong(JsonObject ob, String campo) {
		return ob.get(campo).getAsLong();
	}

}
