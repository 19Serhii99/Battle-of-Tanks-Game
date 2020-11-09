package net;

import java.net.DatagramPacket;

public abstract class UDPObject {
	protected DatagramPacket packet;
	
	public DatagramPacket getPacket () {
		return packet;
	}
}