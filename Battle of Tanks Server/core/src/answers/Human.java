package answers;

import java.io.Serializable;

public class Human implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nickname;
	
	public Human (int id, String nickname) {
		this.id = id;
		this.nickname = nickname;
	}
	
	public int getId () {
		return id;
	}
	
	public String getNickname () {
		return nickname;
	}
}