package me.skiincraft.api.paladins.matches;

import java.util.List;

import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.enums.Rarity;
import me.skiincraft.api.paladins.objects.Card;
import me.skiincraft.api.paladins.objects.Legendary;

public class MatchChampion extends Champion {
	
	private List<Card> cardpurch;
	private Legendary legendary;
	private String skinName;
	private long skinId;

	public MatchChampion(Champion champion, List<Card> cardPurch, String skinName, int skinId, Paladins paladinsapi) {
		super(champion.getChampionId(),
				champion.getChampionName(),
				champion.getChampionEnglishName(), champion.getChampionIcon(),
				champion.getTitle(),
				champion.getEnglishTitle(),
				champion.getRole(),
				champion.getEnglishRole(),
				champion.getLore(),
				champion.getEnglishLore(),
				champion.getHealth(),
				champion.getChampionSpeed(),
				champion.getAbilityPT(),
				champion.getAbilityEN(),
				paladinsapi);
		
		this.cardpurch = cardPurch;
		this.skinName = skinName;
		this.skinId = skinId;
		for (Card card : getCardpurch()) {
			if (card.getRarity() == Rarity.Legendary) {
				legendary = new Legendary(card);
			}
		}
	}

	public List<Card> getCardpurch() {
		return cardpurch;
	}

	public Legendary getLegendary() {
		return legendary;
	}

	public String getSkinName() {
		return skinName;
	}

	public long getSkinId() {
		return skinId;
	}
}
