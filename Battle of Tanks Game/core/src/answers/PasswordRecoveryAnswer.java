package answers;

import java.io.Serializable;

public class PasswordRecoveryAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;

	public PasswordRecoveryAnswer (boolean value) {
		super(value);
	}
}