package answers;

import java.io.Serializable;

public class PasswordRecoveryCodeAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;

	public PasswordRecoveryCodeAnswer (boolean value) {
		super(value);
	}
}