package com.mygdx.game;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import util.CameraController;
import util.Font;

public class DeathsBlock implements Disposable {
	private LinkedList <DeathsBlockItem> items;
	private BitmapFont font;
	
	public DeathsBlock () {
		items = new LinkedList <DeathsBlockItem>();
		font = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
	}
	
	public void show (SpriteBatch batch) {
		for (int i = 0; i < items.size(); i++) {
			final DeathsBlockItem item = items.get(i);
			item.show(batch);
			if (!item.isLive()) {
				items.remove(i);
				for (int j = i; j < items.size(); j++) {
					items.get(j).setPosition(items.get(j).getY() + 20);
				}
				i--;
			}
		}
	}
	
	public void addKilling (String killer, String victim) {
		final DeathsBlockItem item = new DeathsBlockItem(killer, victim, font);
		if (items.size() > 0) {
			item.setPosition(items.getLast().getY() - 20);
		} else {
			item.setPosition(CameraController.getInstance().getY() + CameraController.getInstance().getHeight() / 2 - 5);
		}
		items.add(item);
	}
	
	public void translate (float y) {
		for (DeathsBlockItem item : items) {
			item.setPosition(y);
		}
	}
	
	public LinkedList <DeathsBlockItem> getItems () {
		return items;
	}
	
	@Override
	public void dispose () {
		font.dispose();
	}
}