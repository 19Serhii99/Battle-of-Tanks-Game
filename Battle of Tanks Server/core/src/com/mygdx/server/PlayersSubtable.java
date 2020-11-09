package com.mygdx.server;

import java.util.ArrayList;

public class PlayersSubtable {
	private int maxPlayers;
	private int currentPlayers;
	private boolean isLeftSide;
	
	private ArrayList <PlayerItem> items;
	
	public PlayersSubtable (ArrayList <Player> players, boolean isLeftSide) {
		maxPlayers = players.size();
		currentPlayers = maxPlayers;
		
		initItems(players);
		
		this.isLeftSide = isLeftSide;
	}
	
	private void initItems (ArrayList <Player> players) {
		items = new ArrayList <PlayerItem> (players.size());
		
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			PlayerItem item = new PlayerItem(player);
			items.add(item);
		}
	}
	
	public void addDead () {
		currentPlayers--;
	}
	
	public ArrayList <PlayerItem> getItems () {
		return items;
	}
	
	public int getCurrentPlayers () {
		return currentPlayers;
	}
	
	public int getMaxPlayers () {
		return maxPlayers;
	}
	
	public boolean isLeftSide () {
		return isLeftSide;
	}
}