package com.mygdx.server;

import java.net.Socket;

public class PlayerBattle {
	private Socket socket;
	private Player player;
	
	public PlayerBattle (Socket socket, Player player) {
		this.socket = socket;
		this.player = player;
	}
	
	public Socket getSocket () {
		return socket;
	}
	
	public Player getPlayer () {
		return player;
	}
}