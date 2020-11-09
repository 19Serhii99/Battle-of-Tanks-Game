package answers;

import java.io.Serializable;

public class ConfirmNewFriendAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public ConfirmNewFriendAnswer (boolean value) {
		super(value);
	}
}