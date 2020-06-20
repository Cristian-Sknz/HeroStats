package me.skiincraft.api.paladins.objects;

public class Team {

	private int teamId;
	private String teamName;
	
	public Team(int teamId, String teamName) {
		this.teamId = teamId;
		this.teamName = teamName;
	}

	public int getTeamId() {
		return teamId;
	}

	public String getTeamName() {
		return teamName;
	}
	
	

}
