package queries;

import java.io.Serializable;

public class ConfirmNewFriend extends AddNewFriend implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public ConfirmNewFriend (int id) {
		super(id);
	}
}