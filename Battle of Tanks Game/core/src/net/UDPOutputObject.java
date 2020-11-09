package net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPOutputObject extends UDPObject {
	
	public UDPOutputObject (InetAddress address, int port, Object object) {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
			packet = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size());
			packet.setAddress(address);
			packet.setPort(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendObject (DatagramSocket socket) {
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}