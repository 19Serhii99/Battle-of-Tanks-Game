package com.mygdx.server;

public class Status {
	private boolean isAuthorizated;
	private boolean isPlaying;
	private boolean isReady;
	private boolean isWantsPlay;
	private boolean isConfirmedAcc;
	
	public Status () {
		this.isAuthorizated = false;
		this.isPlaying = false;
		this.isReady = false;
		this.isWantsPlay = false;
		this.isConfirmedAcc = false;
	}
	
	public void setAuthorizated (boolean value) {
		isAuthorizated = value;
	}
	
	public void setPlaying (boolean value) {
		isPlaying = value;
	}
	
	public void setReady (boolean value) {
		isReady = value;
	}
	
	public void setWantsPlay (boolean value) {
		isWantsPlay = value;
	}
	
	public void setConfirmedAcc (boolean value) {
		isConfirmedAcc = value;
	}
	
	public boolean isAuthorizated () {
		return isAuthorizated;
	}
	
	public boolean isPlaying () {
		return isPlaying;
	}
	
	public boolean isReady () {
		return isReady;
	}
	
	public boolean isWantsPlay () {
		return isWantsPlay;
	}
	
	public boolean isConfirmedAcc () {
		return isConfirmedAcc;
	}
}