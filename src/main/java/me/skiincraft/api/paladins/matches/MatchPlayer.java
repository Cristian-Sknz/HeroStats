package me.skiincraft.api.paladins.matches;

import java.util.List;

import me.skiincraft.api.paladins.entity.PaladinsMatch;
import me.skiincraft.api.paladins.objects.Item;
import me.skiincraft.api.paladins.objects.Kills;
import me.skiincraft.api.paladins.objects.LeagueSeason;

public class MatchPlayer {

	private int level;
	private int assists;
	private MatchChampion champion;
	private long damageBot;
	private long damageUsingWeapon;
	private long damageUsingMagical;
	private long damageUsingPhysical;
	private long damageMitigated;
	private long damage;
	private long damageTaken;
	private long damageTakenUsingMagical;
	private long damageTakenUsingPhysical;
	
	private Kills killsDetails;
	private int killstreak;
	private int kills;
	private int deaths;
	private int goldEarned;
	private int goldPerMinute;
	
	private long healing;
	private long selfHealing;
	
	private PaladinsMatch match;
	
	private long playerId;
	private String playerRegion;
	private String playername;
	private LeagueSeason playerLeague;
	private int playerPortalId;
	private long playerPortalUserId;
	private List<Item> itemactive;
	
	private boolean hasWin;
	private String json;

	public MatchPlayer(int level, int assists, MatchChampion champion, long damageBot,
			long damageUsingWeapon, long damageUsingMagical, long damageUsingPhysical, long damageMitigated,
			long damage, long damageTaken, long damageTakenUsingMagical, long damageTakenUsingPhysical,
			Kills killsDetails, int killstreak, int kills, int deaths, int goldEarned, int goldPerMinute, long healing,
			long selfHealing, PaladinsMatch match, long playerId, String playerRegion, String playername,
			LeagueSeason playerLeague, int playerPortalId, long playerPortalUserId, List<Item> itemactive, boolean hasWin, String json) {
		this.level = level;
		this.assists = assists;
		this.champion = champion;
		this.damageBot = damageBot;
		this.damageUsingWeapon = damageUsingWeapon;
		this.damageUsingMagical = damageUsingMagical;
		this.damageUsingPhysical = damageUsingPhysical;
		this.damageMitigated = damageMitigated;
		this.damage = damage;
		this.damageTaken = damageTaken;
		this.damageTakenUsingMagical = damageTakenUsingMagical;
		this.damageTakenUsingPhysical = damageTakenUsingPhysical;
		this.killsDetails = killsDetails;
		this.killstreak = killstreak;
		this.kills = kills;
		this.deaths = deaths;
		this.goldEarned = goldEarned;
		this.goldPerMinute = goldPerMinute;
		this.healing = healing;
		this.selfHealing = selfHealing;
		this.match = match;
		this.playerId = playerId;
		this.playerRegion = playerRegion;
		this.playername = playername;
		this.playerLeague = playerLeague;
		this.playerPortalId = playerPortalId;
		this.playerPortalUserId = playerPortalUserId;
		this.itemactive = itemactive;
		this.hasWin = hasWin;
		this.json = json;
	}

	public int getLevel() {
		return level;
	}
	
	public int getAssists() {
		return assists;
	}

	public MatchChampion getChampion() {
		return champion;
	}

	public long getDamageBot() {
		return damageBot;
	}

	public long getDamageUsingWeapon() {
		return damageUsingWeapon;
	}

	public long getDamageUsingMagical() {
		return damageUsingMagical;
	}

	public long getDamageUsingPhysical() {
		return damageUsingPhysical;
	}

	public long getDamageMitigated() {
		return damageMitigated;
	}

	public long getDamage() {
		return damage;
	}

	public long getDamageTaken() {
		return damageTaken;
	}

	public long getDamageTakenUsingMagical() {
		return damageTakenUsingMagical;
	}

	public long getDamageTakenUsingPhysical() {
		return damageTakenUsingPhysical;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getGoldEarned() {
		return goldEarned;
	}

	public int getGoldPerMinute() {
		return goldPerMinute;
	}

	public long getHealing() {
		return healing;
	}

	public long getSelfHealing() {
		return selfHealing;
	}

	public PaladinsMatch getMatch() {
		return match;
	}

	public long getPlayerId() {
		return playerId;
	}

	public String getPlayername() {
		return playername;
	}

	public int getPlayerPortalId() {
		return playerPortalId;
	}

	public long getPlayerPortalUserId() {
		return playerPortalUserId;
	}

	public List<Item> getItemactive() {
		return itemactive;
	}

	public LeagueSeason getPlayerLeague() {
		return playerLeague;
	}

	public Kills getKillsDetails() {
		return killsDetails;
	}

	public String getPlayerRegion() {
		return playerRegion;
	}

	public boolean HasWin() {
		return hasWin;
	}
	
	public String json() {
		return json;
	}

	@Override
	public String toString() {
		return "MatchPlayer [level=" + level + ", assists=" + assists + ", champion=" + champion.getChampionName() + ", damage=" + damage
				+ ", damageTaken=" + damageTaken + ", killstreak=" + killstreak + ", kills=" + kills + ", deaths="
				+ deaths + ", goldEarned=" + goldEarned + ", healing=" + healing + ", selfHealing=" + selfHealing
				+ ", match=" + match + ", playerId=" + playerId + ", playerRegion=" + playerRegion + ", playername="
				+ playername + ", playerLeague=" + playerLeague + ", hasWin=" + hasWin + "]";
	}
	
	
}
