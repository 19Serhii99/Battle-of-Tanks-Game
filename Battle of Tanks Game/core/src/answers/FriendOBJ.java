package answers;

import java.io.Serializable;

public class FriendOBJ implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int winScore;
	private int failScore;
	private short battleType;
	private short gameType;
	private boolean isOnline;
	private boolean isFights;
	
	private String nickname;
	private String lastSeen;
	
	public FriendOBJ (int id, boolean isOnline, boolean isFights, String nickname) {
		this.id = id;
		this.isOnline = isOnline;
		this.isFights = isFights;
		this.nickname = nickname;
	}
	
	public void setScore (int winScore, int failScore) {
		this.winScore = winScore;
		this.failScore = failScore;
	}
	
	public void setStatus (short gameType, short battleType) {
		this.gameType = gameType;
		this.battleType = battleType;
	}
	
	public void setStatus (String lastSeen) {
		this.lastSeen = lastSeen;
	}
	
	public int getId () {
		return id;
	}
	
	public boolean isOnline () {
		return isOnline;
	}
	
	public boolean isFights () {
		return isFights;
	}
	
	public int getWinScore () {
		return winScore;
	}
	
	public int getFailScore () {
		return failScore;
	}
	
	public short getGameType () {
		return gameType;
	}
	
	public short getBattleType () {
		return battleType;
	}
	
	public String getNickname () {
		return nickname;
	}
	
	public String getLastSeen () {
		return lastSeen;
	}
}