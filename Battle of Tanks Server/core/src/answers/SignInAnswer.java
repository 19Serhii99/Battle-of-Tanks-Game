package answers;

import java.io.Serializable;

public class SignInAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean isConfirmed;
	
	private String name;
	
	public SignInAnswer (boolean value, boolean isConfirmed, String name) {
		super(value);
		this.isConfirmed = isConfirmed;
		this.name = name;
	}
	
	public String getName () {
		return name;
	}
	
	public boolean isConfirmed () {
		return isConfirmed;
	}
}