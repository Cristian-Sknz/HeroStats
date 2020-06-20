package me.skiincraft.api.paladins.enums;

public enum PlayerStatus {
	
	Offline(0), In_Lobby(1), Menu_Selection(2), In_Game(3), Online(4), Unknown(5);

	private int id;
	
	PlayerStatus(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
