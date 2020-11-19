package me.skiincraft.discord.herostats.imagebuild;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.QueueChampion;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.discord.core.common.CustomFont;
import me.skiincraft.discord.herostats.assets.PaladinsImage;
import me.skiincraft.discord.herostats.utils.ImageBuilder;
import me.skiincraft.discord.herostats.utils.IntegerUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Objects;

public class ChampionImage {

	public static InputStream drawImage(PlayerChampion rank) {
		Champion champion = rank.getChampion(Language.Portuguese).get();
		ImageBuilder image = new ImageBuilder("stats", 900, 239);
		image.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			Font kghappy = CustomFont.getFont("kghappy", Font.PLAIN, 23F);
			image.drawImage(ImageIO.read(PaladinsImage.getBackground(champion)), 0, 0, new Dimension(900, 239), ImageBuilder.Alignment.Bottom_left);
			image.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAssetsImage("ChampionOverlayer"))), 0, 0,
					new Dimension(900, 239), ImageBuilder.Alignment.Bottom_left);
			image.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAvatar(champion))), 118, 111, new Dimension(154, 154),
					ImageBuilder.Alignment.Center);

			float kda = (float) (rank.getKills() + rank.getAssists()) / rank.getDeaths();
			String winrate = IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins());
			Font eras = CustomFont.getFont("eras_bold", Font.PLAIN, 23);
			Font golden = CustomFont.getFont("GoldenHills", Font.PLAIN, 50);
			
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

	public static InputStream drawImage(QueueChampion rank) {
		Champion champion = rank.getChampion(Language.Portuguese).get();
		ImageBuilder image = new ImageBuilder("stats", 900, 239);
		image.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			Font kgHappy = CustomFont.getFont("kghappy", Font.PLAIN, 23F);
			image.drawImage(ImageIO.read(PaladinsImage.getBackground(champion)), 0, 0, new Dimension(900, 239), ImageBuilder.Alignment.Bottom_left);
			image.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAssetsImage("ChampionOverlayer"))), 0, 0,
					new Dimension(900, 239), ImageBuilder.Alignment.Bottom_left);
			image.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAvatar(champion))), 118, 111, new Dimension(154, 154),
					ImageBuilder.Alignment.Center);

			float kda = (float) (rank.getKills() + rank.getAssists()) / rank.getDeaths();
			String winRate = IntegerUtils.getPorcentagem(rank.getWins() + rank.getLosses(), rank.getWins());
			Font eras = CustomFont.getFont("eras_bold", Font.PLAIN, 23);
			Font golden = CustomFont.getFont("GoldenHills", Font.PLAIN, 50);

			String champname = rank.getChampionName();

			DecimalFormat df = new DecimalFormat("#.00");
			image.addCentralizedStringY(rank.getKills() + "", 281, 74, kgHappy);
			image.addCentralizedStringY(rank.getDeaths() + "", 281, 111, kgHappy);
			image.addCentralizedStringY(rank.getAssists() + "", 281, 155, kgHappy);
			image.addCentralizedString(df.format(kda), 311, 205, kgHappy);
			image.addCentralizedString(winRate, 749, 92, golden);
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
