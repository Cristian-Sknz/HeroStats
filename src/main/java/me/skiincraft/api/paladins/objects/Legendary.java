package me.skiincraft.api.paladins.objects;

public class Legendary extends Card {
	
	public Legendary(Card card) {
		super(card.getCardName(), card.getCardNameEnglish(),
				card.getCardId1(), card.getCardId2(),
				card.getDescription(), card.getExclusive(),
				card.getRank(), card.getRarity(),
				card.getRecharge_seconds(), card.getMultiplicator(),
				card.getIconUrl(), card.getIconFilePath(),
				card.getRet_msg());
	}

}
