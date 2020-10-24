package me.skiincraft.discord.herostats.enums;

public enum PaladinsClass {

	Damage(0), Support(2), Front_Line(3), Flanker(1);

	private final int id;

	PaladinsClass(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name().replace("_", " ");
	}
}
