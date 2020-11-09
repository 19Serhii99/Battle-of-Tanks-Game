package queries;

import java.io.Serializable;

public class AddNewFriend implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	public AddNewFriend (int id) {
		this.id = id;
	}
	
	public int getId () {
		return id;
	}
}