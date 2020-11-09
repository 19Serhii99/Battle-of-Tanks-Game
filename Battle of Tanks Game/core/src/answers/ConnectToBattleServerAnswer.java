package answers;

import java.io.Serializable;

public class ConnectToBattleServerAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String host;
	private String mapName;
	private int portTCP;
	private int portUDP;
	private int id;
	private boolean isLeftSide;
	
	public ConnectToBattleServerAnswer (String host, int portTCP, int portUDP, String mapName, int id, boolean isLeftSide) {
		this.host = host;
		this.portTCP = portTCP;
		this.portUDP = portUDP;
		this.mapName = mapName;
		this.id = id;
		this.isLeftSide = isLeftSide;
	}
	
	public String getHost () {
		return host;
	}
	
	public int getPortTCP () {
		return portTCP;
	}
	
	public int getPortUDP () {
		return portUDP;
	}
	
	public String getMapName () {
		return mapName;
	}
	
	public int getId () {
		return id;
	}
	
	public boolean isLeftSide () {
		return isLeftSide;
	}
}