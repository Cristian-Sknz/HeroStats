package me.skiincraft.discord.herostats.imagebuild;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.textfont.CustomFont;
import me.skiincraft.discord.core.utils.ImageBuilder;
import me.skiincraft.discord.core.utils.IntegerUtils;
import me.skiincraft.discord.core.utils.ImageBuilder.Alignment;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class ChampionImage {

	public static InputStream drawImage(PlayerChampion rank) {
		Plugin plugin = HeroStatsBot.getMain().getPlugin();
		Champion champion = rank.getChampion(Language.Portuguese).get();
		ImageBuilder image = new ImageBuilder("stats", 900, 239);
		image.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		CustomFont font = new CustomFont();
		try {
			Font kghappy = font.getFont("kghappy", Font.PLAIN, 23F);
			image.drawImage(new File(plugin.getAssetsPath().getAbsolutePath() + "/backgrounds/" + rank.getChampionName()
					+ " Background.png"), 0, 0, new Dimension(900, 239), Alignment.Bottom_left);
			image.drawImage(new File(plugin.getAssetsPath().getAbsolutePath() + "/ChampionOverlayer.png"), 0, 0,
					new Dimension(900, 239), Alignment.Bottom_left);
			image.drawImage(new URL(champion.getIcon()), 118, 111, new Dimension(154, 154),
					Alignment.Center);

			float kda = (float) (rank.getKills() + rank.getAssists()) / rank.getDeaths();
			String winrate = IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins());
			Font eras = font.getFont("eras_bold", Font.PLAIN, 23);
			Font golden = font.getFont("GoldenHills", Font.PLAIN, 50);
			
			String champname = rank.getChampionName();

			DecimalFormat df = new DecimalFormat("#.00");
			image.addCentralizedStringY(rank.getKills() + "", 281, 74, kghappy);
			image.addCentralizedStringY(rank.getDeaths() + "", 281, 111, kghappy);
			image.addCentralizedStringY(rank.getAssists() + "", 281, 155, kghappy);
			image.addCentralizedString(df.format(kda), 311, 205, kghappy);
			image.addCentralizedString(winrate, 749, 92, golden);
			image.addCentralizedString(rank.getWins() + "", 654, 202, golden.deriveFont(33F));
			image.addCentralizedString(rank.getLosses() + "", 815, 202, golden.deriveFont(33F));
			
			image.addCentralizedString(champname, 116, 205, eras);
			image.addCentralizedString(champion.getRole().replace("Paladins ", ""), 116, 227, eras);
			// 116 205
			return image.buildInput();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
