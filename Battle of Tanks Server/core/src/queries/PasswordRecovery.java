package queries;

import java.io.Serializable;

public class PasswordRecovery implements Serializable {
	private static final long serialVersionUID = 1L;
	private String login;
	
	public PasswordRecovery (String login) {
		this.login = login;
	}
	
	public String getLogin () {
		return login;
	}
}