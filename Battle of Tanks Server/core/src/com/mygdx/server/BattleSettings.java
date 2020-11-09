package com.mygdx.server;

import java.util.ArrayDeque;

public class BattleSettings {
	private BattleType battleType;
	private ArrayDeque <PlayerSettings> players;
	
	private float averageLevel;
	private long startTime;
	
	public BattleSettings (BattleType battleType, float averageLevel) {
		this.battleType = battleType;
		this.averageLevel = averageLevel;
		this.players = new ArrayDeque <PlayerSettings>();
		startTime = System.currentTimeMillis();
	}
	
	public void setBattleType (BattleType battleType) {
		this.battleType = battleType;
	}
	
	public void setAverageLevel (float averageLevel) {
		this.averageLevel = averageLevel;
	}
	
	public BattleType getBattleType () {
		return battleType;
	}
	
	public long getStartTime () {
		return startTime;
	}
	
	public float getAverageLevel () {
		return averageLevel;
	}
	
	public ArrayDeque <PlayerSettings> getPlayers () {
		return players;
	}
}