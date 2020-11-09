package com.mygdx.server;

public class PlayerItem {
	private Player player;
	
	private int killsAmount;
	private int damageAmount;
	
	public PlayerItem (Player player) {
		this.player = player;
	}
	
	public void addKill () {
		killsAmount++;
	}
	
	public void addDamage (int amount) {
		damageAmount += amount;
	}
	
	public Player getPlayer () {
		return player;
	}
	
	public int getKills () {
		return killsAmount;
	}
	
	public int getDamage () {
		return damageAmount;
	}
}