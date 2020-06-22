package me.skiincraft.api.paladins.utils;

import com.google.gson.JsonObject;

public class JsonUtils {

	public static String get(JsonObject ob, String campo) {
		try {
			return ob.get(campo).getAsString();
		} catch (UnsupportedOperationException e) {
			return "";
		}
	}

	public static int getInt(JsonObject ob, String campo) {
		try {
			return ob.get(campo).getAsInt();
		} catch (UnsupportedOperationException e) {
			return 0;
		}
	}

	public static long getLong(JsonObject ob, String campo) {
		try {
			return ob.get(campo).getAsLong();
		} catch (UnsupportedOperationException e) {
			return 0;
		}
	}

}
