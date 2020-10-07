package me.skiincraft.discord.herostats.imagebuild;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IteratorUtils;

import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.api.paladins.ranked.RankedKBM;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.textfont.CustomFont;
import me.skiincraft.discord.core.utils.ImageBuilder;
import me.skiincraft.discord.core.utils.ImageBuilder.Alignment;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class StatusImage {

	public static InputStream drawImage(PlayerChampion rank, Player player) {
		Plugin plugin = HeroStatsBot.getMain().getPlugin();
		ImageBuilder image = new ImageBuilder("stats", 900, 239);
		image.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		CustomFont font = new CustomFont();
		try {
			Font kghappy = font.getFont("kghappy", Font.PLAIN, 23F);
			DirectoryStream<Path> dStream = Files.newDirectoryStream(Paths.get(plugin.getAssetsPath().getAbsolutePath() + "/backgrounds/"));
			List<Path> backgrounds = IteratorUtils.toList(dStream.iterator());
			backgrounds = backgrounds.stream().filter(o -> o.toFile().getName().contains(".png")).collect(Collectors.toList());
			
			Path championImage = backgrounds.stream().filter(f-> f.toFile().getName().contains(rank.getChampionName())).findAny().get();
			
			image.drawImage(championImage.toFile(), 0, 0, new Dimension(900, 239), Alignment.Bottom_left);
			image.drawImage(new File(plugin.getAssetsPath().getAbsolutePath() + "/BestChampionOverlayer.png"), 0, 0,
					new Dimension(900, 239), Alignment.Bottom_left);
			image.drawImage(new URL(rank.getChampion(Language.Portuguese).get().getIcon()), 118, 111, new Dimension(154, 154),
					Alignment.Center);

			float kda = (float) (rank.getKills() + rank.getAssists()) / rank.getDeaths();
			image.drawImage(
					new File(plugin.getAssetsPath().getAbsolutePath() + "/elos/"
							+ player.getTier().name() + ".png"),
					799, 103, new Dimension(135, 135), Alignment.Center);
			Font eras = font.getFont("eras_bold", Font.PLAIN, 23);
			if (player.getTier() == Tier.Unranked) {
				RankedKBM ranked = player.getRankedKBM();
				image.addCentralizedString(ranked.getWins() + ranked.getLosses() + "/5", 799, 103, eras);
			}

			image.addCentralizedString(player.getTier().getName(Language.Portuguese), 799, 196,
					kghappy);
			if (player.getRankedKBM().getPoints() != 0) {
				image.addCentralizedString(player.getRankedKBM().getPoints() + "/100", 799, 220, kghappy);
			}
			DecimalFormat df = new DecimalFormat("#.00");
			image.addCentralizedStringY(rank.getKills() + "", 281, 74, kghappy);
			image.addCentralizedStringY(rank.getDeaths() + "", 281, 111, kghappy);
			image.addCentralizedStringY(rank.getAssists() + "", 281, 155, kghappy);
			image.addCentralizedString(df.format(kda), 311, 205, kghappy);

			image.addCentralizedString(rank.getChampionName(), 116, 205, eras);
			image.addCentralizedString(rank.getChampion(Language.Portuguese).get().getRole().replace("Paladins ", ""), 116, 227, eras);
			// 116 205
			return image.buildInput();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
