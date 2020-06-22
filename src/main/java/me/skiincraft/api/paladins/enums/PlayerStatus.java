package me.skiincraft.api.paladins.enums;

public class PlayerStatus {

	public enum Status {
		Offline(0), In_Lobby(1), God_Selection(2), In_Game(3), Online(4), Unknown(5);

		private int id;

		Status(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
		public static Status getStatusById(int id) {
			for (Status status : values()) {
				if (status.getId() == id) {
					return status;
				}
			}
			return Unknown;
		}
	}

	private int matchId;
	private PaladinsQueue queue;
	private Status status;
	
	public PlayerStatus(int matchId, Status status, PaladinsQueue queue) {
		this.matchId = matchId;
		this.status = status;
		this.queue = queue;
	}

	public int getMatchId() {
		return matchId;
	}
	
	public PaladinsQueue getMatchQueue() {
		return queue;
	}
	
	public boolean isLiveMatch() {
		return (getMatchId() != 0);
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "PlayerStatus [matchId=" + matchId + ", queue=" + getMatchQueue() + ", status=" + status + "]";
	}
}
