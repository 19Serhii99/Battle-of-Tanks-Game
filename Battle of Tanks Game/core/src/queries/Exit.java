package queries;

import java.io.Serializable;

public class Exit implements Serializable {
	private static final long serialVersionUID = 1L;
	private int reason;
	
	public Exit (int reason) {
		this.reason = reason;
	}
	
	public int getReason () {
		return reason;
	}
}