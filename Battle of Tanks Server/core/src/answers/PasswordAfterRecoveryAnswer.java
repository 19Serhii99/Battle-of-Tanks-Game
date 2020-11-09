package answers;

import java.io.Serializable;

public class PasswordAfterRecoveryAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;

	public PasswordAfterRecoveryAnswer (boolean value) {
		super(value);
	}
}
