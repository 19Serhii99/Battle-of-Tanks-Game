package answers;

import java.io.Serializable;
import java.util.ArrayList;

public class FindFriendAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <Human> people;
	
	public FindFriendAnswer () {
		people = new ArrayList <Human>();
	}
	
	public ArrayList <Human> getPeople () {
		return people;
	}
}