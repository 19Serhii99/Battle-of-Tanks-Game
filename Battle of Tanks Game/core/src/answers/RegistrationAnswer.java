package answers;

import java.io.Serializable;

public class RegistrationAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	
	public RegistrationAnswer (boolean value) {
		super(value);
	}
	
	public RegistrationAnswer (boolean value, int code) {
		super(value);
		this.code = code;
	}

	public int getCode () {
		return code;
	}
}