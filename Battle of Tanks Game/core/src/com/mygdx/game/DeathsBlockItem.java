package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import gui.Label;
import util.CameraController;

public class DeathsBlockItem {
	private Label name1;
	private Label name2;
	private Label destroyed;
	
	private float alpha = 1.0f;
	private float x;
	private float y;
	private float width;
	private long time;
	private boolean isLive = true;
	
	public DeathsBlockItem (String name1, String name2, BitmapFont font) {
		this.name1 = new Label(name1, font);
		this.name2 = new Label(name2, font);
		this.destroyed = new Label("знищив", font);
		
		this.width = this.name1.getWidth() + this.destroyed.getWidth() + this.name2.getWidth() + 20;
	}
	
	public void setPosition (float y) {
		this.x = CameraController.getInstance().getX() + CameraController.getInstance().getWidth() / 2 - width - 10;
		this.y = y;
		
		name1.setPosition(x, y);
		destroyed.setPosition(name1.getX() + name1.getWidth() + 10, y);
		name2.setPosition(destroyed.getX() + destroyed.getWidth() + 10, y);
	}
	
	public void show (SpriteBatch batch) {
		time += Gdx.graphics.getDeltaTime();
		
		if (time >= 5000) {
			alpha -= Gdx.graphics.getDeltaTime() * 2;
			if (alpha <= 0.0f) {
				alpha = 0.0f;
				isLive = false;
			}
		}
		
		name1.setAlphas(alpha);
		name2.setAlphas(alpha);
		destroyed.setAlphas(alpha);
		
		name1.draw(batch);
		name2.draw(batch);
		destroyed.draw(batch);
	}
	
	public Label getName1 () {
		return name1;
	}
	
	public Label getName2 () {
		return name2;
	}
	
	public Label getDestroyed () {
		return destroyed;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public float getWidth () {
		return width;
	}
	
	public boolean isLive () {
		return isLive;
	}
}