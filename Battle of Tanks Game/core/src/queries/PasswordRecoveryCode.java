package queries;

import java.io.Serializable;

public class PasswordRecoveryCode extends ConfirmationCodeAcc implements Serializable {
	private static final long serialVersionUID = 1L;

	public PasswordRecoveryCode(String code) {
		super(code);
	}
}