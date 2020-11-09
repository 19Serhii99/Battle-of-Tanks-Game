package net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

public class UDPInputObject extends UDPObject {
	
	public UDPInputObject (DatagramPacket packet) {
		super.packet = packet;
	}
	
	public Object getObject () {
		Object object = null;
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			object =  objectInputStream.readObject();
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}
}