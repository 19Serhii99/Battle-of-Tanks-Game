package queries;

import java.io.Serializable;

public class PasswordAfterRecovery implements Serializable {
	private static final long serialVersionUID = 1L;
	private String password;
	
	public PasswordAfterRecovery (String password) {
		this.password = password;
	}
	
	public String getPassword () {
		return password;
	}
}