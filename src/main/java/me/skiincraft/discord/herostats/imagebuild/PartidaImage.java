package me.skiincraft.discord.herostats.imagebuild;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import me.skiincraft.api.paladins.entity.LiveMatch;
import me.skiincraft.api.paladins.matches.LiveMatchChampion;
import me.skiincraft.api.paladins.matches.LivePlayer;
import me.skiincraft.discord.core.apis.ImageBuilder;
import me.skiincraft.discord.core.apis.ImageBuilder.Alignment;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.textfont.CustomFont;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class PartidaImage {

	private static Plugin plugin;

	public static InputStream drawImage(String mapname, LiveMatch match) {
		List<LivePlayer> players = match.getPlayers();
		plugin = HeroStatsBot.getMain().getPlugin();
		ImageBuilder i = new ImageBuilder("partida", 863, 313);
		try {
			System.out.println(mapname);
			i.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			i.drawImage(new File(plugin.getAssetsPath() + "/maps/" + mapname.concat(".jpg")), 863 / 2, 313 / 2,
					new Dimension(870, 490), Alignment.Center);
			i.drawImage(new File(plugin.getAssetsPath() + "/TeamFade.png"), 863 / 2, 313 / 2, i.getSize(),
					Alignment.Center);
			int y = 62;
			Font font = new CustomFont().getFont("gabriola", Font.BOLD, 22);
			// Time1
			for (int o = 0; o < 5; o++) {
				ImageBuilder profile = new ImageBuilder("champs", 478, 56);
				LivePlayer p = players.get(o);
				LiveMatchChampion champ = p.getChampion();
				profile.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				profile.drawImage(
						new File(plugin.getAssetsPath() + "/elos/" + p.getTier().getTier().name().concat(".png")), 6,
						32, new Dimension(37, 37), Alignment.Left);
				profile.drawImage(
						new File(plugin.getAssetsPath() + "/avatar/" + champ.getChampionName().concat(".png")), 46, 28,
						new Dimension(41, 41), Alignment.Left);
				// FontMetrics fm = profile.getGraphic().getFontMetrics(font);
				String pname = (p.getPlayerName().length() < 3) ? "Perfil Privado :/" : p.getPlayerName();
				profile.addCentralizedStringY(pname, 98, 21, font);
				// profile.addCentralizedStringY("(PP - " + p.getTier().getPoints() +")", 98 +
				// fm.stringWidth(pname) + 3, 21, font);
				profile.addCentralizedStringY(champ.getRole().split(" ")[1], 111, 37, font);
				BufferedImage img = profile.buildImage();
				i.drawImage(img, 0, y, profile.getSize(), Alignment.Left);
				y += 50;
			}
			int y2 = 62;
			// Time2
			for (int o = 5; o < 10; o++) {
				ImageBuilder profile = new ImageBuilder("champs", 478, 56);
				LivePlayer p = players.get(o);
				LiveMatchChampion champ = p.getChampion();
				profile.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				profile.drawImage(
						new File(plugin.getAssetsPath() + "/elos/" + p.getTier().getTier().name().concat(".png")), 472,
						32, new Dimension(37, 37), Alignment.Right);
				profile.drawImage(
						new File(plugin.getAssetsPath() + "/avatar/" + champ.getChampionName().concat(".png")), 432, 28,
						new Dimension(41, 41), Alignment.Right);
				// FontMetrics fm = profile.getGraphic().getFontMetrics(font);
				String pname = (p.getPlayerName().length() < 3) ? "Perfil Privado :/" : p.getPlayerName();
				profile.addRightStringY(pname, 379, 21, font);
				// profile.addRightStringY("(PP - " + p.getTier().getPoints() +")",
				// 379-fm.stringWidth(pname) -3, 21, font);
				profile.addRightStringY(champ.getRole().split(" ")[1], 361, 37, font);
				BufferedImage img = profile.buildImage();
				i.drawImage(img, 863, y2, profile.getSize(), Alignment.Right);
				y2 += 50;
			}

			return i.buildInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 863 313
		return null;
	}

}
