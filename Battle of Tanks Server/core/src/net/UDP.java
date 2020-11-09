package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayDeque;

import com.badlogic.gdx.utils.Disposable;

public class UDP extends Thread implements Disposable {
	private DatagramSocket socket;
	private ArrayDeque <UDPInputObject> objects;
	
	public UDP (final int port) {
		try {
			System.out.println("Запуск сервера по протоколу UDP...");
			socket = new DatagramSocket(port);
			System.out.println("Сервер успешно запущен по протоколу UDP с портом " + port);
			objects = new ArrayDeque <UDPInputObject>();
			super.setDaemon(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run () {
		while (true) {
			try {
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				objects.add(new UDPInputObject(packet));
				Thread.sleep(1);
			} catch (IOException e) {
				System.out.println("Ошибка прослушивания клиентов UDP...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayDeque <UDPInputObject> getObjects () {
		return objects;
	}
	
	public DatagramSocket getSocket () {
		return socket;
	}
	
	public int getPort () {
		return socket.getLocalPort();
	}
	
	@Override
	public void dispose () {
		socket.close();
	}
}