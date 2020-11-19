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
import java.util.Objects;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.objects.LoadoutItem;
import me.skiincraft.discord.core.OusuCore;
import me.skiincraft.discord.core.common.CustomFont;
import me.skiincraft.discord.herostats.HeroStatsBot;
import me.skiincraft.discord.herostats.assets.PaladinsImage;
import me.skiincraft.discord.herostats.utils.ImageBuilder;

import javax.imageio.ImageIO;

public class DeckPreviewImage {
	
	public static class CardDeck {
		private final Card card;
		private final LoadoutItem item;
		
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

	public static InputStream drawImage(Loadout loadouts) {
		List<Card> c = HeroStatsBot.getPaladins().getSessions().get(0)
				.getEndPoint()
				.getChampionCards(loadouts.getChampionId(), Language.Portuguese)
				.get()
				.getAsList();

		List<CardDeck> loadoutCards = new ArrayList<>();
		for (LoadoutItem item : loadouts.getItems()) {
			boolean internBoolean = false;
			for (Card card : c){
				if (internBoolean){
					break;
				}
				if (item.getItemId() == card.getCardId2()){
					loadoutCards.add(new CardDeck(card, item));
					internBoolean = true;
				}
			}
		}
		
		ImageBuilder im = new ImageBuilder("deckpreview", 900, 239);
		im.getGraphic().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Champion champion = loadouts.getChampion().get();
		try {
			String champname = champion.getName();
			im.drawImage(ImageIO.read(PaladinsImage.getBackground(champion)),
					0, 0, new Dimension(900, 239), ImageBuilder.Alignment.Bottom_left);

			Font arial = CustomFont.getFont("arial_rounded", Font.PLAIN, 12);
			int x = 85;

			for (CardDeck items : loadoutCards) {
				Card card = items.getCard();
				LoadoutItem item = items.getItem();
				        try {
							im.drawImage(new URL(card.getIcon()), x, 76, new Dimension(127, 99), ImageBuilder.Alignment.Center);
						} catch (Exception e){
							im.drawImage(ImageIO.read(Objects.requireNonNull(PaladinsImage.getAssetsImage("default_card"))), x, 76, new Dimension(127, 99), ImageBuilder.Alignment.Center);
						}
						im.drawImage(new File(OusuCore.getAssetsPath() + "/cards/" + "Level "
										+ item.getPoints() + " Paladins Card.png"),
								x, 122, new Dimension(161, 244), ImageBuilder.Alignment.Center);
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
