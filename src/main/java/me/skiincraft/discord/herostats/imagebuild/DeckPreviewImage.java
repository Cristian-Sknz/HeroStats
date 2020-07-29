package me.skiincraft.discord.herostats.imagebuild;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.skiincraft.api.paladins.common.ChampionLoadout;
import me.skiincraft.api.paladins.enums.Rarity;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.objects.LoadoutItems;
import me.skiincraft.discord.core.apis.ImageBuilder;
import me.skiincraft.discord.core.apis.ImageBuilder.Alignment;
import me.skiincraft.discord.core.multilanguage.LanguageManager;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.textfont.CustomFont;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class DeckPreviewImage {

	public static InputStream drawImage(ChampionLoadout loadouts, LanguageManager lang) {
		Plugin plugin = HeroStatsBot.getMain().getPlugin();
		List<Card> cards = (lang.getLanguage().name() == "Portuguese") ? loadouts.getChampion().getCardsPT()
				: loadouts.getChampion().getCardsEN();
		ImageBuilder im = new ImageBuilder("deckpreview", 900, 239);
		im.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		try {
			String champname = loadouts.getChampion().getChampionName();
			im.drawImage(
					new File(
							plugin.getAssetsPath().getAbsolutePath() + "/backgrounds/" + champname + " Background.png"),
					0, 0, new Dimension(900, 239), Alignment.Bottom_left);

			List<Card> loadoutCards = new ArrayList<>();
			CustomFont font = new CustomFont();
			Font arial = font.getFont("arial_rounded", Font.PLAIN, 12);
			int x = 85;

			for (LoadoutItems itens : loadouts.getItens()) {
				for (Card card : cards) {
					if (itens.getItemId() == card.getCardId2()) {
						if (card.getRarity() == Rarity.Legendary) {
							continue;
						}

						loadoutCards.add(card);
						im.drawImage(new URL(card.getIconUrl()), x, 76, new Dimension(127, 99), Alignment.Center);
						im.drawImage(
								new File(plugin.getAssetsPath().getAbsolutePath() + "/cards/" + "Level "
										+ itens.getPoints() + " Paladins Card.png"),
								x, 122, new Dimension(161, 244), Alignment.Center);
						// +180

						// 59
						im.getGraphic().setColor(Color.WHITE);
						im.addCentralizedString(card.getCardName(), x, 132, arial);
						im.addCentralizedString(itens.getPoints() + "", x - 57, 229, arial.deriveFont(18F));
						im.getGraphic().setColor(Color.BLACK);
						String desc = card.getDescription().split("]")[1].replace("{multiply}",
								card.getMultiplicator() * itens.getPoints() + "");
						im.addStringBox(desc, x, 151, 18, new Font("arial", Font.BOLD, 12));
						x += 180;
					}
				}
			}

			return im.buildInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
