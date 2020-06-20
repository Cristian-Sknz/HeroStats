package me.skiincraft.api.paladins.objects;

import me.skiincraft.api.paladins.enums.Rarity;

public class Card {
	
	private String cardName;
	private String cardNameEnglish;
	private int cardId1;
	private int cardId2;
	private String description;
	private boolean exclusive;
	private int rank;
	private Rarity rarity;
	private int recharge_seconds;
	
	private float multiplicator;
	
	private String iconUrl;
	private String iconFilePath;
	
	private String ret_msg;
	
	

	public Card(String cardName, String cardNameEnglish, int cardId1, int cardId2, String description, boolean exclusive,
			int rank, Rarity rarity, int recharge_seconds, float multiply, String iconUrl, String iconFilePath, String ret_msg) {
		super();
		this.cardName = cardName;
		this.cardNameEnglish = cardNameEnglish;
		this.cardId1 = cardId1;
		this.cardId2 = cardId2;
		this.description = description;
		this.exclusive = exclusive;
		this.rank = rank;
		this.rarity = rarity;
		this.recharge_seconds = recharge_seconds;
		this.multiplicator = multiply;
		this.iconUrl = iconUrl;
		this.iconFilePath = iconFilePath;
		this.ret_msg = ret_msg;
	}

	public String getCardName() {
		return cardName;
	}

	public String getCardNameEnglish() {
		return cardNameEnglish;
	}

	public int getCardId1() {
		return cardId1;
	}

	public int getCardId2() {
		return cardId2;
	}

	public boolean getExclusive() {
		return exclusive;
	}

	public int getRank() {
		return rank;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public int getRecharge_seconds() {
		return recharge_seconds;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public String getIconFilePath() {
		return iconFilePath;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getMultiplicator() {
		return multiplicator;
	}

	
}
