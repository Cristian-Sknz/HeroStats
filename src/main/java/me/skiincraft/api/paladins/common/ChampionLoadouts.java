package me.skiincraft.api.paladins.common;

import java.util.List;

import me.skiincraft.api.paladins.objects.LoadoutItems;

public class ChampionLoadouts {

	private Champion champion;
	private String deckname;
	private int deckId;
	private List<LoadoutItems> itens;
	private int championId;
	private String championName;
	
	
	public ChampionLoadouts(Champion champion, String deckname, int deckId, List<LoadoutItems> itens, int championId,
			String championName) {
		this.champion = champion;
		this.deckname = deckname;
		this.deckId = deckId;
		this.itens = itens;
		this.championId = championId;
		this.championName = championName;
	}

	public Champion getChampion() {
		return champion;
	}



	public String getDeckname() {
		return deckname;
	}



	public int getDeckId() {
		return deckId;
	}



	public List<LoadoutItems> getItens() {
		return itens;
	}



	public int getChampionId() {
		return championId;
	}



	public String getChampionName() {
		return championName;
	}	
}
