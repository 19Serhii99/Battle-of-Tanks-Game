package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.TreeSet;

import com.badlogic.gdx.utils.Disposable;
import com.mygdx.server.BattleCreator;
import com.mygdx.server.UserHandler;

public class TCP extends Thread implements Disposable {
	private ServerSocket socket;
	private ArrayList <UserHandler> users;
	private TreeSet <UserHandler> players;
	private Connection mysql;
	private BattleCreator battleCreator;
	
	public TCP (final int port) {
		System.out.println("Запуск сервера по протоколу TCP...");
		try {
			this.socket = new ServerSocket(port);
			this.users = new ArrayList <UserHandler>();
			this.players = new TreeSet <UserHandler>();
			System.out.println("Сервер успешно запущен по протоколу TCP");
			super.setDaemon(true);
			battleCreator = new BattleCreator();
			battleCreator.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setConnection (Connection mysql) {
		this.mysql = mysql;
	}
	
	@Override
	public void run () {
		while (true) {
			try {
				synchronized (users) {
					users.add(new UserHandler(socket.accept(), mysql));
				}
				System.out.println("Новое подключение...");
			} catch (IOException e) {
				System.out.println("Ошибка прослушивания клиентов TCP...");
			}
		}
	}
	
	synchronized public ArrayList <UserHandler> getUsers () {
		return users;
	}
	
	synchronized public TreeSet <UserHandler> getPlayers () {
		return players;
	}
	
	synchronized public BattleCreator getBattleCreator () {
		return battleCreator;
	}
	
	@Override
	public void dispose () {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}