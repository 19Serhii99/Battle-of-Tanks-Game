package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import technique.Corps;
import util.Font;
import util.TextureCreator;

public class Objects implements Disposable {
	private static Objects objects;
	
	private BitmapFont buttonBitmapFont;
	private BitmapFont buttonFocusedBitmapFont;
	
	private Texture buttonTexture;
	private Texture buttonOverTexture;
	private Texture buttonFocusedTexture;
	
	private Texture fieldTexture;
	private Texture fieldFocusedTexture;
	private Texture fieldErrorTexture;
	
	private Texture areaTexture;
	private Texture areaFocusedTexture;
	private Texture areaErrorTexture;
	
	private Skin skin;
	private TextFieldStyle textFieldStyle;
	private TextFieldStyle textAreaStyle;
	
	private Array <Corps> corpses;
	
	private Objects () {
		
	}
	
	public void createObjects () {
		buttonBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		buttonBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		buttonFocusedBitmapFont = Font.getInstance().generateBitmapFont(Color.BLACK, 30);
		buttonFocusedBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		buttonTexture = new Texture(Gdx.files.internal("images/gui/button.png"));
		buttonOverTexture = new Texture(Gdx.files.internal("images/gui/buttonOver.png"));
		buttonFocusedTexture = new Texture(Gdx.files.internal("images/gui/buttonFocused.png"));
		
		buttonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonOverTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		buttonFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		skin = new Skin();
		skin.add("selection", TextureCreator.createTexture(Color.GRAY));
		skin.add("cursor", TextureCreator.createTexture(Color.BLACK));
		
		BitmapFont fieldBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 25);
		fieldBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textFieldStyle = new TextFieldStyle();
		textFieldStyle.selection = skin.getDrawable("selection");
		textFieldStyle.cursor = skin.getDrawable("cursor");
		textFieldStyle.font = fieldBitmapFont;
		textFieldStyle.fontColor = Color.BLACK;
		
		fieldTexture = new Texture(Gdx.files.internal("images/gui/field.png"));
		fieldFocusedTexture = new Texture(Gdx.files.internal("images/gui/fieldFocused.png"));
		fieldErrorTexture = new Texture(Gdx.files.internal("images/gui/fieldError.png"));
		
		fieldTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fieldFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fieldErrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		fieldBitmapFont = Font.getInstance().generateBitmapFont(Color.BLACK, 15);
		fieldBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		areaTexture = new Texture(Gdx.files.internal("images/gui/textArea.png"));
		areaFocusedTexture = new Texture(Gdx.files.internal("images/gui/textAreaFocused.png"));
		areaErrorTexture = new Texture(Gdx.files.internal("images/gui/textAreaError.png"));
		
		areaTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		areaFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		areaErrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		skin.add("area", areaTexture);
		skin.add("areaFocused", areaFocusedTexture);
		skin.add("areaError", areaErrorTexture);
		
		textAreaStyle = new TextFieldStyle();
		textAreaStyle.selection = skin.getDrawable("selection");
		textAreaStyle.cursor = skin.getDrawable("cursor");
		textAreaStyle.background = skin.getDrawable("area");
		textAreaStyle.focusedBackground = skin.getDrawable("areaFocused");
		textAreaStyle.font = fieldBitmapFont;
		textAreaStyle.fontColor = Color.BLACK;
	}
	
	public static Objects getInstance () {
		if (objects == null) objects = new Objects();
		return objects;
	}
	
	public void setCorpses (Array <Corps> corpses) {
		this.corpses = corpses;
	}
	
	public BitmapFont getButtonBitmapFont () {
		return buttonBitmapFont;
	}
	
	public BitmapFont getButtonFocusedBitmapFont () {
		return buttonFocusedBitmapFont;
	}
	
	public Texture getButtonTexture () {
		return buttonTexture;
	}
	
	public Texture getButtonOverTexture () {
		return buttonOverTexture;
	}
	
	public Texture getButtonFocusedTexture () {
		return buttonFocusedTexture;
	}
	
	public TextFieldStyle getTextFieldStyle () {
		return textFieldStyle;
	}
	
	public Texture getFieldTexture () {
		return fieldTexture;
	}
	
	public Texture getFieldFocusedTexture () {
		return fieldFocusedTexture;
	}
	
	public Texture getFieldErrorTexture () {
		return fieldErrorTexture;
	}
	
	public Texture getAreaTexture () {
		return areaTexture;
	}
	
	public Texture getAreaFocusedTexture () {
		return areaFocusedTexture;
	}
	
	public Texture getAreaErrorTexture () {
		return areaErrorTexture;
	}
	
	public TextFieldStyle getTextAreaStyle () {
		return textAreaStyle;
	}
	
	public Array <Corps> getCorpses () {
		return corpses;
	}

	@Override
	public void dispose () {
		buttonBitmapFont.dispose();
		buttonFocusedBitmapFont.dispose();
		buttonTexture.dispose();
		buttonOverTexture.dispose();
		buttonFocusedTexture.dispose();
		fieldTexture.dispose();
		fieldFocusedTexture.dispose();
		fieldErrorTexture.dispose();
		skin.dispose();
	}
}