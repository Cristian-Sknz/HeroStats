package me.skiincraft.api.paladins.objects;

import me.skiincraft.api.paladins.enums.DamageType;

public class Ability {
	
	private String name;
	private String description;
	private int id;
	private String avatarUrl;
	private DamageType damagetype;
	private int cooldown;
	private boolean hasCooldown;
	
	public Ability(String name, String description, int id, String avatarUrl,
			DamageType damagetype, int cooldown, boolean hasCooldown) {
		this.name = name;
		this.description = description;
		this.id = id;
		this.avatarUrl = avatarUrl;
		this.damagetype = damagetype;
		this.cooldown = cooldown;
		this.hasCooldown = hasCooldown;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public DamageType getDamagetype() {
		return damagetype;
	}

	public int getCooldown() {
		return cooldown;
	}

	public boolean isHasCooldown() {
		return hasCooldown;
	}
	
}
