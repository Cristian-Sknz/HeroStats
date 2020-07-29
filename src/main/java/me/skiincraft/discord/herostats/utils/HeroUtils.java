package me.skiincraft.discord.herostats.utils;

import java.util.Date;

import me.skiincraft.api.paladins.enums.Platform;

public class HeroUtils {

	public static Date getDateAfter(long aftermillis) {
		Date date = new Date();
		date.setTime(date.getTime() + aftermillis);
		return date;
	}

	public static Date getDateBefore(long aftermillis) {
		Date date = new Date();
		date.setTime(date.getTime() + aftermillis);
		return date;
	}

	public static Platform parseStringPlatform(String string) {
		for (Platform plat : Platform.values()) {
			if (plat.name().equalsIgnoreCase(string)) {
				return plat;
			}
		}
		return Platform.PC;
	}

}
