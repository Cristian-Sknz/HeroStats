package me.skiincraft.api.paladins.matches;

import me.skiincraft.api.paladins.common.Champion;
import me.skiincraft.api.paladins.entity.Session;

public class LiveMatchChampion extends Champion {
	
	private int championlevel;
	private String skinName;
	private long skinId;

	public LiveMatchChampion(Champion champion, int level, String skinName, int skinId, Session session) {
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
				session.getRequester());
		
		this.championlevel = level;
		this.skinName = skinName;
		this.skinId = skinId;
	}

	public int getLevel() {
		return championlevel;
	}

	public String getSkinName() {
		return skinName;
	}

	public long getSkinId() {
		return skinId;
	}
}
