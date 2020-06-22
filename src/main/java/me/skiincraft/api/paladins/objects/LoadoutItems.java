package me.skiincraft.api.paladins.objects;

public class LoadoutItems {
	private int itemId;
	private String itemName;
	private int points;

	public LoadoutItems(int itemId, String itemName, int points) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.points = points;
	}

	public int getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public int getPoints() {
		return points;
	}

}
