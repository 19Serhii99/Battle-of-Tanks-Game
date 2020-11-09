package answers;

import java.io.Serializable;

public class AddToBlackListAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;

	public AddToBlackListAnswer (boolean value) {
		super(value);
	}
}