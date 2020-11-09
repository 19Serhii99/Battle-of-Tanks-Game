package answers;

import java.io.Serializable;
import java.util.ArrayList;

public class OtherPlayersTechniqueAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <OtherPlayerTechnique> players;
	
	public OtherPlayersTechniqueAnswer () {
		players = new ArrayList <OtherPlayerTechnique>();
	}
	
	public ArrayList <OtherPlayerTechnique> getPlayers () {
		return players;
	}
}