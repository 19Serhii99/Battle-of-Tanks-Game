package com.mygdx.editor;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;

public class Events extends InputAdapter {
	private static Events instance;
	private boolean isMouseLeftPressed;
	private boolean isMouseRightPressed;
	private boolean isMouseLeftReleased;
	private boolean isMouseRightReleased;
	private boolean isTextInput;
	
	private char inputSymbol;
	
	private Events () {
		clear();
	}
	
	public static Events getInstance () {
		if (instance == null) instance = new Events();
		return instance;
	}
	
	public void clear () {
		isMouseLeftPressed = false;
		isMouseRightPressed = false;
		isMouseLeftReleased = false;
		isMouseRightReleased = false;
		isTextInput = false;
	}
	
	@Override
	public boolean touchDown (int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) isMouseLeftPressed = true;
		else if (button == Buttons.RIGHT) isMouseRightPressed = true;
		return false;
	}
	
	@Override
	public boolean touchUp (int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) isMouseLeftReleased = true;
		else if (button == Buttons.RIGHT) isMouseRightReleased = true;
		return false;
	}
	
	@Override
	public boolean keyTyped (char character) {
		inputSymbol = character;
		isTextInput = true;
		return false;
	}
	
	public boolean isMouseLeftPressed () {
		return isMouseLeftPressed;
	}
	
	public boolean isMouseRightPressed () {
		return isMouseRightPressed;
	}
	
	public boolean isMouseLeftReleased () {
		return isMouseLeftReleased;
	}
	
	public boolean isMouseRightReleased () {
		return isMouseRightReleased;
	}
	
	public char getInputSymbol () {
		return inputSymbol;
	}
	
	public boolean isTextInput () {
		return isTextInput;
	}
}