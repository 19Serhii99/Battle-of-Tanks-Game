package com.mygdx.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayDeque;

public class ServerHandler extends Thread {
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	private ArrayDeque <Object> objects;
	
	public ServerHandler () {
		try {
			socket = new Socket("localhost", 4569);
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			objects = new ArrayDeque <Object>();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendObject (Object object) {
		try {
			objectOutputStream.writeObject(object);
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run () {
		while (true) {
			try {
				if (socket.getInputStream().available() > 0) {
					synchronized(objects) {
						objects.add(objectInputStream.readObject());
					}
				}
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	synchronized public ArrayDeque <Object> getObjects () {
		return objects;
	}
}
