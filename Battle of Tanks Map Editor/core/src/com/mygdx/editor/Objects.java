package com.mygdx.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Disposable;

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
	
	private Skin skin;
	private TextFieldStyle textFieldStyle;
	
	private Objects () {
		
	}
	
	public void createObjects () {
		buttonBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		buttonBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		buttonFocusedBitmapFont = Font.getInstance().generateBitmapFont(Color.BLACK, 30);
		buttonFocusedBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		buttonTexture = new Texture(Gdx.files.internal("images/button.png"));
		buttonOverTexture = new Texture(Gdx.files.internal("images/buttonOver.png"));
		buttonFocusedTexture = new Texture(Gdx.files.internal("images/buttonFocused.png"));
		
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
		
		fieldTexture = new Texture(Gdx.files.internal("images/field.png"));
		fieldFocusedTexture = new Texture(Gdx.files.internal("images/fieldFocused.png"));
		fieldErrorTexture = new Texture(Gdx.files.internal("images/fieldError.png"));
		
		fieldTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fieldFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fieldErrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public static Objects getInstance () {
		if (objects == null) objects = new Objects();
		return objects;
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
		Font.getInstance().dispose();
	}
}