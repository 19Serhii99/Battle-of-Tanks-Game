package queries;

import java.io.Serializable;

public class AddToBlackList extends AddNewFriend implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public AddToBlackList (int id) {
		super (id);
	}
}