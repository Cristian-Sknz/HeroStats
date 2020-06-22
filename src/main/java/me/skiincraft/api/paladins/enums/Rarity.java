package me.skiincraft.api.paladins.enums;

public enum Rarity {
	
	Common(0), Uncommom(1), Rare(2), Epic(3), Legendary(4), Limited(5), Ilimited(6);
	
	private int id;
	
	Rarity(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public static Rarity getRarityByName(String name) {
		for (Rarity rarity : Rarity.values()) {
			if (rarity.name().equalsIgnoreCase(name)) {
				return rarity;
			}
		}
		return null;
	}

}
