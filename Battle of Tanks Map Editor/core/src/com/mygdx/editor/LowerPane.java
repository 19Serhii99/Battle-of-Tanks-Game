package com.mygdx.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import gui.TextButton;
import util.Font;

public class LowerPane implements Disposable {
	private TextButton spawnOneButton;
	private TextButton spawnTwoButton;
	private TextButton addBaseOneButton;
	private TextButton addBaseTwoButton;
	private TextButton pointsButton;
	private TextButton saveMapButton;
	private TextButton addMapObjectButton;
	
	public LowerPane () {
		initButtons();
	}
	
	private void initButtons () {
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 15);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		BitmapFont fontFocused = Font.getInstance().generateBitmapFont(Color.BLACK, 15);
		fontFocused.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		spawnOneButton = new TextButton("Спавн ком. 1", font);
		spawnOneButton.setBitmapFontFocused(fontFocused);
		spawnOneButton.setTexture(Objects.getInstance().getButtonTexture());
		spawnOneButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		spawnOneButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		spawnOneButton.setSize(100, 25);
		spawnOneButton.setPosition(100, 50);
		
		spawnTwoButton = new TextButton("Спавн ком. 2", font);
		spawnTwoButton.setBitmapFontFocused(fontFocused);
		spawnTwoButton.setTexture(Objects.getInstance().getButtonTexture());
		spawnTwoButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		spawnTwoButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		spawnTwoButton.setSize(100, 25);
		spawnTwoButton.setPosition(spawnOneButton.getX() + spawnOneButton.getWidth() + 5, 50);
		
		addBaseOneButton = new TextButton("База ком. 1", font);
		addBaseOneButton.setBitmapFontFocused(fontFocused);
		addBaseOneButton.setTexture(Objects.getInstance().getButtonTexture());
		addBaseOneButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		addBaseOneButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		addBaseOneButton.setSize(100, 25);
		addBaseOneButton.setPosition(spawnTwoButton.getX() + spawnTwoButton.getWidth() + 5, 50);
		
		addBaseTwoButton = new TextButton("База ком. 2", font);
		addBaseTwoButton.setBitmapFontFocused(fontFocused);
		addBaseTwoButton.setTexture(Objects.getInstance().getButtonTexture());
		addBaseTwoButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		addBaseTwoButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		addBaseTwoButton.setSize(100, 25);
		addBaseTwoButton.setPosition(addBaseOneButton.getX() + addBaseOneButton.getWidth() + 5, 50);
		
		pointsButton = new TextButton("Точки", font);
		pointsButton.setBitmapFontFocused(fontFocused);
		pointsButton.setTexture(Objects.getInstance().getButtonTexture());
		pointsButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		pointsButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		pointsButton.setSize(100, 25);
		pointsButton.setPosition(addBaseTwoButton.getX() + addBaseTwoButton.getWidth() + 5, 50);
		
		saveMapButton = new TextButton("Сохранить", font);
		saveMapButton.setBitmapFontFocused(fontFocused);
		saveMapButton.setTexture(Objects.getInstance().getButtonTexture());
		saveMapButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		saveMapButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		saveMapButton.setSize(100, 25);
		saveMapButton.setPosition(pointsButton.getX() + pointsButton.getWidth() + 5, 50);
		
		addMapObjectButton = new TextButton("Доб. обьект", font);
		addMapObjectButton.setBitmapFontFocused(fontFocused);
		addMapObjectButton.setTexture(Objects.getInstance().getButtonTexture());
		addMapObjectButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		addMapObjectButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		addMapObjectButton.setSize(100, 25);
		addMapObjectButton.setPosition(saveMapButton.getX() + saveMapButton.getWidth() + 5, 50);
	}
	
	public void show (SpriteBatch batch) {
		spawnOneButton.show(batch);
		spawnTwoButton.show(batch);
		addBaseOneButton.show(batch);
		addBaseTwoButton.show(batch);
		pointsButton.show(batch);
		saveMapButton.show(batch);
		addMapObjectButton.show(batch);
	}
	
	public TextButton getPointsButton () {
		return pointsButton;
	}
	
	public TextButton getSpawnOneButton () {
		return spawnOneButton;
	}
	
	public TextButton getSpawnTwoButton () {
		return spawnTwoButton;
	}
	
	public TextButton getSaveMapButton () {
		return saveMapButton;
	}
	
	public TextButton getAddMapObjectButton () {
		return addMapObjectButton;
	}
	
	public TextButton getAddBaseOneButton () {
		return addBaseOneButton;
	}
	
	public TextButton getAddBaseTwoButton () {
		return addBaseTwoButton;
	}
	
	@Override
	public void dispose () {
		spawnOneButton.getLabelBitmapFont().dispose();
		spawnOneButton.getLabelFocusedBitmapFont().dispose();
	}
}