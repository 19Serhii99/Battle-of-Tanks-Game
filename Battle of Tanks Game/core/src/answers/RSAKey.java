package answers;

import java.io.Serializable;

public class RSAKey implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte[] data;

	public RSAKey(byte[] data) {
		this.data = data;
	}
	
	public byte[] getData() {
		return data;
	}
}