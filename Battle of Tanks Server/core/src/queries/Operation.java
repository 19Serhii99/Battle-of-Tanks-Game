package queries;

import java.io.Serializable;

public class Operation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int type;
	private int id;
	private boolean isCorps;
	
	public Operation (int type, boolean isCorps, int id) {
		this.type = type;
		this.isCorps = isCorps;
		this.id = id;
	}
	
	public int getType () {
		return type;
	}
	
	public int getId () {
		return id;
	}
	
	public boolean isCorps () {
		return isCorps;
	}
}