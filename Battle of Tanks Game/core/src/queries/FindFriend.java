package queries;

import java.io.Serializable;

public class FindFriend implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int lastPlayer;
	private boolean isUp;
	private String search;
	
	public FindFriend (String search, int lastPlayer, boolean isUp) {
		this.search = search;
		this.lastPlayer = lastPlayer;
		this.isUp = isUp;
	}
	
	public String getSearch () {
		return search;
	}
	
	public int getLastPlayer () {
		return lastPlayer;
	}
	
	public boolean isUp () {
		return isUp;
	}
}