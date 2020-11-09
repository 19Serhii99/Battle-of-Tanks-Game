package com.mygdx.game;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;

public class Events implements InputProcessor {
	private static Events events;
	private boolean isLeftMousePressed;
	private boolean isRightMousePressed;
	private boolean isLeftMouseReleased;
	private boolean isRightMouseReleased;
	private boolean isMouseMoved;
	
	private Events () {
		clear();
	}
	
	public void clear () {
		isLeftMousePressed = false;
		isRightMousePressed = false;
		isLeftMouseReleased = false;
		isRightMouseReleased = false;
		isMouseMoved = false;
	}
	
	public static Events getEvents () {
		if (events == null) {
			events = new Events();
		}
		return events;
	}
	
	public boolean isLeftMousePressed () {
		return isLeftMousePressed;
	}
	
	public boolean isRightMousePressed () {
		return isRightMousePressed;
	}
	
	public boolean isLeftMouseReleased () {
		return isLeftMouseReleased;
	}
	
	public boolean isRightMouseReleased () {
		return isRightMouseReleased;
	}
	
	public boolean isMouseMoved () {
		return isMouseMoved;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			isLeftMousePressed = true;
		} else if (button == Buttons.RIGHT) {
			isRightMousePressed = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {
			isLeftMouseReleased = true;
		} else if (button == Buttons.RIGHT) {
			isRightMouseReleased = true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		isMouseMoved = true;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}