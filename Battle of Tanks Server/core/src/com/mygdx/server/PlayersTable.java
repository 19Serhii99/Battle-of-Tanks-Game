package com.mygdx.server;

import com.badlogic.gdx.Gdx;

public abstract class PlayersTable {
	protected float battleTime;
	protected boolean isCommand;
	
	public PlayersTable () {
		//battleTime = 600.0f;
		battleTime = 10.0f;
	}
	
	public void updateTime () {
		battleTime -= Gdx.graphics.getDeltaTime();
		if (battleTime < 0) battleTime = 0;
	}
	
	public boolean isCommand () {
		return isCommand;
	}
	
	public float getBattleTime () {
		return battleTime;
	}
}