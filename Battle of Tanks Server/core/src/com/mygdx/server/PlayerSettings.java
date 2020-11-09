package com.mygdx.server;

public class PlayerSettings {
	private UserHandler userHandler;
	private BattleType battleType;
	
	private int idTechnique;
	private int idTower;
	private int level;
	
	public PlayerSettings (UserHandler userHandler, BattleType battleType, int idTechnique, int idTower, int level) {
		this.userHandler = userHandler;
		this.battleType = battleType;
		this.idTechnique = idTechnique;
		this.idTower = idTower;
		this.level = level;
	}
	
	public UserHandler getUserHandler () {
		return userHandler;
	}
	
	public BattleType getBattleType () {
		return battleType;
	}
	
	public int getIdTechnique () {
		return idTechnique;
	}
	
	public int getIdTower () {
		return idTower;
	}
	
	public int getLevel () {
		return level;
	}
}