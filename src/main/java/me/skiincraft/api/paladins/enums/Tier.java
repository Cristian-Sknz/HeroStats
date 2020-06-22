package me.skiincraft.api.paladins.enums;

public enum Tier {

	Unranked(0), Bronze_V(1), Bronze_IV(2), Bronze_III(3), Bronze_II(4), Bronze_I(5),
	Silver_V(6), Silver_IV(7), Silver_III(8), Silver_II(9), Silver_I(10),
	Gold_V(11), Gold_IV(12), Gold_III(13), Gold_II(14), Gold_I(15),    
	Platinum_V(16), Platinum_IV(17), Platinum_III(18), Platinum_II(19), Platinum_I(20),   
	Diamond_V(21), Diamond_IV(22), Diamond_III(23), Diamond_II(24), Diamond_I(25), 
	Master(26), Grandmaster(27);

	private int rankId;
	
	Tier(int rankId) {
		this.rankId = rankId;
	}
	
	public int getRankId() {
		return rankId;
	}
	
	public static Tier getTierById(int id) {
		for (Tier tier:Tier.values()) {
			if (tier.getRankId() == id) {
				return tier;
			}
		}
		return Tier.Bronze_I;
	}
	
	public String getName(Language language) {
		if (language == Language.Portuguese) {
			if (this.name().contains("Silver")) {
				return "Prata " + this.name().substring(7);
			}
			if (this.name().contains("Gold")) {
				return "Ouro " + this.name().substring(5);
			}
			if (this.name().contains("Platinum")) {
				return "Platina " + this.name().substring(9);
			}
			if (this.name().contains("Diamond")) {
				return "Diamante " + this.name().substring(8);
			}
		}
		return this.name().replace("_", " ");
	}
	
}
