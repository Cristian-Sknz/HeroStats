package me.skiincraft.api.paladins.objects;

import me.skiincraft.api.paladins.enums.Tier;

public class TierResults {
	
	private Tier tierConquest;
	private Tier tierRankedController;
	private Tier tierRankedKBM;
	
	public TierResults(int tierConquest, int tierRankedController, int tierRankedKBM) {
		this.tierConquest = Tier.getTierById(tierConquest);
		this.tierRankedController = Tier.getTierById(tierRankedController);
		this.tierRankedKBM = Tier.getTierById(tierRankedKBM);
	}
	
	public TierResults(Tier tierConquest, Tier tierRankedController, Tier tierRankedKBM) {
		this.tierConquest = (tierConquest);
		this.tierRankedController = (tierRankedController);
		this.tierRankedKBM = (tierRankedKBM);
	}
	
	public Tier getTierConquest() {
		return tierConquest;
	}
	public Tier getTierRankedController() {
		return tierRankedController;
	}
	public Tier getTierRankedKBM() {
		return tierRankedKBM;
	}
	
	

}
