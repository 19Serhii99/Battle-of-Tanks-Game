package queries;

import java.io.Serializable;
import java.net.InetAddress;

public class UDPData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private InetAddress inetAddress;
	private int port;
	
	public UDPData (InetAddress inetAddress, int port) {
		this.inetAddress = inetAddress;
		this.port = port;
	}
	
	public InetAddress getInetAddress () {
		return inetAddress;
	}
	
	public int getPort () {
		return port;
	}
}