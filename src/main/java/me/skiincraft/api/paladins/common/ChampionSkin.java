package me.skiincraft.api.paladins.common;

import me.skiincraft.api.paladins.enums.Rarity;

public class ChampionSkin {

	private Champion champion;
	private String championName;
	private Rarity rarity;
	private int skinId1;
	private int skinId2;
	
	private String skinName;
	private String skinNameEnglish;
	
	public ChampionSkin(Champion champion, String championName, Rarity rarity, int skinId1, int skinId2,
			String skinName, String skinNameEnglish) {
		super();
		this.champion = champion;
		this.championName = championName;
		this.rarity = rarity;
		this.skinId1 = skinId1;
		this.skinId2 = skinId2;
		this.skinName = skinName;
		this.skinNameEnglish = skinNameEnglish;
	}
	public Champion getChampion() {
		return champion;
	}
	public String getChampionName() {
		return championName;
	}
	public Rarity getRarity() {
		return rarity;
	}
	public int getSkinId1() {
		return skinId1;
	}
	public int getSkinId2() {
		return skinId2;
	}
	public String getSkinName() {
		return skinName;
	}
	public String getSkinNameEnglish() {
		return skinNameEnglish;
	}
	@Override
	public String toString() {
		return "ChampionSkin [champion=" + champion + ", championName=" + championName + ", rarity=" + rarity
				+ ", skinId1=" + skinId1 + ", skinId2=" + skinId2 + ", skinName=" + skinName + ", skinNameEnglish="
				+ skinNameEnglish + "]";
	}
}
