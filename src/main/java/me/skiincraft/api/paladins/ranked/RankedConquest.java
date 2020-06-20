package me.skiincraft.api.paladins.ranked;

import me.skiincraft.api.paladins.objects.LeagueSeason;

public class RankedConquest extends LeagueSeason {

	private int leaves;
	private int prevrank;
	private int rank;
	private int season;
	private int trend;
	private int playerId;
	
	public RankedConquest(LeagueSeason l, int leaves, int prevrank, int rank, int season,
			int trend, int playerId, String ret_msg) {
		super(l.getWins(), l.getLosses(), l.getPoints(), l.getTier());
		this.leaves = leaves;
		this.prevrank = prevrank;
		this.rank = rank;
		this.season = season;
		this.trend = trend;
		this.playerId = playerId;
		this.ret_msg = ret_msg;
	}

	private String ret_msg;

	public int getLeaves() {
		return leaves;
	}

	public int getPrevrank() {
		return prevrank;
	}

	public int getRank() {
		return rank;
	}

	public int getSeason() {
		return season;
	}

	public int getTrend() {
		return trend;
	}

	public int getPlayerId() {
		return playerId;
	}

	public String getRet_msg() {
		return ret_msg;
	}
	
	
	

}
