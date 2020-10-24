package me.skiincraft.discord.herostats.utils;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.leaderboard.LeaderBoard;
import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.enums.Platform;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.impl.LeaderboardImpl;
import me.skiincraft.api.paladins.objects.Place;
import org.apache.commons.collections4.ListUtils;

public class HeroUtils {

	public static Color paladinsClassColor(Champion champion) {
		return paladinsClassColor(champion.getRole().split(" ")[1]);
	}

	public static Color paladinsClassColor(String name) {
		String rename = name.replace("Paladins ", "");
		if (rename.equalsIgnoreCase("dano")){
			return new Color(255, 46, 46);
		}
		if (rename.equalsIgnoreCase("flanco")){
			return new Color(255, 203, 46);
		}
		if (rename.equalsIgnoreCase("tanque")){
			return new Color(46, 206, 255);
		}
		if (rename.equalsIgnoreCase("suporte")){
			return new Color(53, 255, 46);
		}
		return Color.BLACK;
	}

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

	public static boolean isGrandMaster(List<Place> leaderboardMaster, Player player){
		Place first = leaderboardMaster.get(0);
		if (first.getTier() != Tier.Master){
			return false;
		}

		if (first.getPoints() < 99){
			return false;
		}
		LeaderBoard leaderboard = new LeaderboardImpl(ListUtils.partition(leaderboardMaster,100).get(0), Tier.Grandmaster);
		return leaderboard.getById(player.getId()) != null;
	}

	public static String getTierImage(Tier tier){
		switch (tier){
			case Bronze_I:
				return "https://i.imgur.com/LBWqCBy.png";
			case Bronze_II:
				return "https://i.imgur.com/DSJzakS.png";
			case Bronze_III:
				return "https://i.imgur.com/wDBNrsu.png";
			case Bronze_IV:
				return "https://i.imgur.com/iCh7xnk.png";
			case Bronze_V:
				return "https://i.imgur.com/5fExc4s.png";
			case Silver_I:
				return "https://i.imgur.com/6zNkFlr.png";
			case Silver_II:
				return "https://i.imgur.com/BLhNfL3.png";
			case Silver_III:
				return "https://i.imgur.com/KBf7Mx7.png";
			case Silver_IV:
				return "https://i.imgur.com/pRE6MdC.png";
			case Silver_V:
				return "https://i.imgur.com/VilYXOa.png";
			case Gold_I:
				return "https://i.imgur.com/k8UY9gZ.png";
			case Gold_II:
				return "https://i.imgur.com/bwPIS94.png";
			case Gold_III:
				return "https://i.imgur.com/9YdmwJP.png";
			case Gold_IV:
				return "https://i.imgur.com/Xhj6Zck.png";
			case Gold_V:
				return "https://i.imgur.com/nDQp07q.png";
			case Platinum_I:
				return "https://i.imgur.com/xMc3yXA.png";
			case Platinum_II:
				return "https://i.imgur.com/404oSsY.png";
			case Platinum_III:
				return "https://i.imgur.com/MJslu4g.png";
			case Platinum_IV:
				return "https://i.imgur.com/fVvzmRB.png";
			case Platinum_V:
				return "https://i.imgur.com/hAa8g4b.png";
			case Diamond_I:
				return "https://i.imgur.com/dxUyc6w.png";
			case Diamond_II:
				return "https://i.imgur.com/puHoZKa.png";
			case Diamond_III:
				return "https://i.imgur.com/9BGhnvD.png";
			case Diamond_IV:
				return "https://i.imgur.com/UkCdWHn.png";
			case Diamond_V:
				return "https://i.imgur.com/GuzqVJH.png";
			case Master:
				return "https://i.imgur.com/p5kLZ8p.png";
			case Grandmaster:
				return "https://i.imgur.com/SyAyPHF.png";
			default:
				return "https://i.imgur.com/JMNVqNz.png";
		}
	}

	public static void createFile(String json, String filename) throws IOException {
		File file = new File(filename + ".json");
		boolean b = file.createNewFile();
		if (b) {
			FileWriter writer = new FileWriter(file);
			writer.write(json);
			writer.close();
		}
	}

}
