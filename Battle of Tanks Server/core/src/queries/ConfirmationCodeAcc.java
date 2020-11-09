package queries;

import java.io.Serializable;

public class ConfirmationCodeAcc implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;
	
	public ConfirmationCodeAcc (String code) {
		this.code = code;
	}
	
	public String getCode () {
		return code;
	}
}