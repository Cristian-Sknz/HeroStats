package me.skiincraft.discord.herostats.imagebuild;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.objects.LoadoutItem;
import me.skiincraft.discord.core.configuration.LanguageManager;
import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.textfont.CustomFont;
import me.skiincraft.discord.core.utils.ImageBuilder;
import me.skiincraft.discord.core.utils.ImageBuilder.Alignment;
import me.skiincraft.discord.herostats.HeroStatsBot;

public class DeckPreviewImage {
	
	public static class CardDeck {
		private Card card;
		private LoadoutItem item;
		
		public CardDeck(Card card, LoadoutItem item) {
			this.card = card;
			this.item = item;
		}
		
		public Card getCard() {
			return card;
		}
		public LoadoutItem getItem() {
			return item;
		}
	}

	public static InputStream drawImage(Loadout loadouts, LanguageManager lang) {
		Plugin plugin = HeroStatsBot.getMain().getPlugin();
		List<Card> c = HeroStatsBot.getPaladins().getSessions().get(0).getEndPoint()
		.getChampionCards(loadouts.getChampionId(), Language.Portuguese)
		.get().getAsList();
		
		List<CardDeck> loadoutCards = loadouts.getItems().stream().map(o ->{
			for (Card carta : c) {
				if (carta.getCardId2() == o.getItemId()) {
					return new CardDeck(carta, o);
				}
			}
			
			return null;
		}).collect(Collectors.toList());
		loadoutCards.remove(null);
		
		ImageBuilder im = new ImageBuilder("deckpreview", 900, 239);
		im.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Champion champion = loadouts.getChampion().get();
		try {
			String champname = champion.getName();
			im.drawImage(
					new File(
							plugin.getAssetsPath().getAbsolutePath() + "/backgrounds/" + champname + " Background.png"),
					0, 0, new Dimension(900, 239), Alignment.Bottom_left);

			CustomFont font = new CustomFont();
			Font arial = font.getFont("arial_rounded", Font.PLAIN, 12);
			int x = 85;

			for (CardDeck itens : loadoutCards) {
				Card card = itens.getCard();
				LoadoutItem item = itens.getItem();
						im.drawImage(new URL(card.getIcon()), x, 76, new Dimension(127, 99), Alignment.Center);
						im.drawImage(
								new File(plugin.getAssetsPath().getAbsolutePath() + "/cards/" + "Level "
										+ item.getPoints() + " Paladins Card.png"),
								x, 122, new Dimension(161, 244), Alignment.Center);
						// +180

						// 59
						im.getGraphic().setColor(Color.WHITE);
						im.addCentralizedString(card.getName(), x, 132, arial);
						im.addCentralizedString(item.getPoints() + "", x - 57, 229, arial.deriveFont(18F));
						im.getGraphic().setColor(Color.BLACK);
						
						String desc = card.getDescription().split("]")[1].replace("{multiply}",
								card.getMultiplicator() * item.getPoints() + "");
						im.addStringBox(desc, x, 151, 18, new Font("arial", Font.BOLD, 10));
						x += 180;
			}

			return im.buildInput();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
