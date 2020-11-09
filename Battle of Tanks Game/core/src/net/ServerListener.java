package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayDeque;

import answers.IsOnlinePlayerAnswer;

public class ServerListener extends Thread {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ArrayDeque <Object> objects;
	
	public ServerListener (Socket socket) {
		this.socket = socket;
		this.objects = new ArrayDeque <Object>(1);
		try {
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			Client.getInstance().setServerConnection(true);
		} catch (IOException e) {
			Client.getInstance().setServerConnection(false);
		}
	}
	
	@Override
	public void run () {
		while (true) {
			if (!socket.isClosed()) {
				try {
					if (socket.getInputStream().available() > 0) {
						Object object = objectInputStream.readObject();
						if (object.getClass() != IsOnlinePlayerAnswer.class) {
							objects.push(object);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayDeque <Object> getObjects () {
		return objects;
	}
}