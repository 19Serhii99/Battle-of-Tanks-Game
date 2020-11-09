package queries;

import java.io.Serializable;

public class Registration extends SignIn implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nickname;
	
	public Registration(String login, String password, String nickname) {
		super(login, password);
		this.nickname = nickname;
	}
	
	public String getNickname () {
		return nickname;
	}
}