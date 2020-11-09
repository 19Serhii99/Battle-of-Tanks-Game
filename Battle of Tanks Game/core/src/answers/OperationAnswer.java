package answers;

import java.io.Serializable;

public class OperationAnswer extends Verification implements Serializable {
	private static final long serialVersionUID = 1L;

	public OperationAnswer (boolean value) {
		super(value);
	}
}