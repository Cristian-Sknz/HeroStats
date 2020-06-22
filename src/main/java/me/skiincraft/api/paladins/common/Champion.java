package me.skiincraft.api.paladins.common;

import java.util.List;

import me.skiincraft.api.paladins.Queue;
import me.skiincraft.api.paladins.enums.Language;
import me.skiincraft.api.paladins.objects.Ability;
import me.skiincraft.api.paladins.objects.Card;

public class Champion {
	
	private int championId;
	private String championName;
	private String championEnglishName;
	private String championIcon;
	private String title;
	private String englishTitle;
	private String role;
	private String englishRole;
	private String lore;
	private String englishLore;
	private int health;
	private int championSpeed;
	
	private Queue queue;
	
	private List<Ability> abilityPT;
	private List<Ability> abilityEN;
	
	private List<Card> loadedCards;

	public Champion(int championId, String championName, String championEnglishName, String championIcon, String title,
			String englishTitle, String role, String englishRole, String lore, String englishLore, int health,
			int championSpeed, List<Ability> abilityPT, List<Ability> abilityEN, Queue queue) {
		this.championId = championId;
		this.championName = championName;
		this.championEnglishName = championEnglishName;
		this.championIcon = championIcon;
		this.title = title;
		this.englishTitle = englishTitle;
		this.role = role;
		this.englishRole = englishRole;
		this.lore = lore;
		this.englishLore = englishLore;
		this.health = health;
		this.championSpeed = championSpeed;
		this.abilityPT = abilityPT;
		this.abilityEN = abilityEN;
		this.queue = queue;
	}

	public int getChampionId() {
		return championId;
	}

	public String getChampionName() {
		return championName;
	}

	public String getChampionEnglishName() {
		return championEnglishName;
	}

	public String getTitle() {
		return title;
	}

	public String getRole() {
		return role;
	}

	public String getLore() {
		return lore;
	}

	public int getHealth() {
		return health;
	}

	public int getChampionSpeed() {
		return championSpeed;
	}

	public List<Ability> getAbilityPT() {
		return abilityPT;
	}

	public List<Ability> getAbilityEN() {
		return abilityEN;
	}
	
	public List<Card> getCardsPT() {
		return queue.getChampionsCards(getChampionId(), Language.Portuguese);
	}
	
	public List<Card> getCardsEN() {
		return queue.getChampionsCards(getChampionId(), Language.English);
	}

	public String getChampionIcon() {
		return championIcon;
	}
	

	public String getEnglishTitle() {
		return englishTitle;
	}

	public String getEnglishRole() {
		return englishRole;
	}

	public String getEnglishLore() {
		return englishLore;
	}

	public Queue getQueue() {
		return queue;
	}

	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public List<Card> getLoadedCards() {
		if (loadedCards == null || loadedCards.size() == 0) {
			loadedCards = getCardsPT();
			loadedCards.addAll(getCardsEN());
		}
		return loadedCards;
	}

}
