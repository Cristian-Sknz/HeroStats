package me.skiincraft.api.paladins.entity;

import me.skiincraft.api.paladins.Queue;

public class PaladinsFriend {
	  /*{
		    "account_id":"1115541929",
		    "friend_flags":"1",
		    "name":"Maintalus",
		    "player_id":"707419748",
		    "ret_msg":null,
		    "status":"Friend"
		  },*/
	
	private int accoundId;
	private int friendFlags;
	private String name;
	private int userId;
	private String status;
	private Queue queue;
	private PaladinsPlayer player;
	public PaladinsFriend(int accoundId, int friendFlags, String name, int userId, String status, PaladinsPlayer player, Queue queue) {
		this.accoundId = accoundId;
		this.friendFlags = friendFlags;
		this.name = name;
		this.userId = userId;
		this.status = status;
		this.player = player;
	}
	
	public PaladinsPlayer getUser() {
		return player;
	}
	
	public Queue getQueue() {
		return queue;
	}
	
	public int getAccoundId() {
		return accoundId;
	}
	public int getFriendFlags() {
		return friendFlags;
	}
	public String getName() {
		return name;
	}
	public int getUserId() {
		return userId;
	}
	public String getStatus() {
		return status;
	}
	
	
}
