package answers;

import java.io.Serializable;

public class AddNewFriendAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public AddNewFriendAnswer(boolean value) {
		super(value);
	}
}