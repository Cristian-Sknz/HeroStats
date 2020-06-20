package me.skiincraft.api.paladins.enums;

public enum DamageType {

	Direct_Damage(0, "Direct"), Area_Damage(1, "AoE"), Complete(2, "True");//Complete for DamageType: True;
	
	private int id;
	private String original;
	
	private DamageType(int id, String original) {
		this.id = id;
		this.original = original;
	}
	
	public static DamageType getByOriginal(String string) {
		for (DamageType damage:DamageType.values()) {
			if (damage.getOriginal().equalsIgnoreCase(string)) {
				return damage;
			}
		}
		return Complete;
	}
	
	public int getId() {
		return id;
	}
	public String getOriginal() {
		return original;
	}
	
	
}
