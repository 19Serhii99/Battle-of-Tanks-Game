package queries;

import java.io.Serializable;

public class SignIn implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String login;
	protected String password;
	
	public SignIn (String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public String getLogin () {
		return login;
	}
	
	public String getPassword () {
		return password;
	}
}