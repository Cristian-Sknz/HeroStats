package me.skiincraft.api.paladins.enums;

public enum Platform {

	PS4(9), Steam(5), HiRez(1), Xbox(10), Switch(22), EpicGames(25);

	private int portalid;
	Platform(int portalid){
		this.portalid = portalid;
	}
	
	public int getPortalId() {
		return portalid;
	}
	
}
