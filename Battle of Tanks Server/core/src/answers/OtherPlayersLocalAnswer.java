package answers;

import java.io.Serializable;
import java.util.ArrayList;

public class OtherPlayersLocalAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <OtherPlayerLocalAnswer> players;
	
	public OtherPlayersLocalAnswer () {
		players = new ArrayList <OtherPlayerLocalAnswer>();
	}
	
	public ArrayList <OtherPlayerLocalAnswer> getPlayers () {
		return players;
	}
}