package answers;

import java.io.Serializable;

public abstract class Verification implements Serializable {
	private static final long serialVersionUID = 1L;
	protected boolean value;
	
	public Verification (boolean value) {
		this.value = value;
	}
	
	public boolean getValue () {
		return value;
	}
}
