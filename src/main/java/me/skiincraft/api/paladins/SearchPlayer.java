package me.skiincraft.api.paladins;

public class SearchPlayer {

	private String name;
	private int userId;
	private String hirezPlayerName;
	private int portalId;
	private boolean privacyFlag;

	public SearchPlayer(String name, int userId, String hirezPlayerName, int portalId, boolean privacyFlag) {
		this.name = name;
		this.userId = userId;
		this.hirezPlayerName = hirezPlayerName;
		this.portalId = portalId;
		this.privacyFlag = privacyFlag;
	}

	public String getName() {
		return name;
	}
	
	public int getUserId() {
		return userId;
	}

	public String getHirezPlayerName() {
		return hirezPlayerName;
	}

	public int getPortalId() {
		return portalId;
	}

	public boolean isPrivacyFlag() {
		return privacyFlag;
	}

}
