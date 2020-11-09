package queries;

import java.io.Serializable;

public class ConfirmPlayer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	public ConfirmPlayer (int id) {
		this.id = id;
	}
	
	public int getId () {
		return id;
	}
}