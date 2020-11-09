package gui;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Events;

import util.CameraController;

public abstract class BaseActor {	
	protected boolean isPressed;
	protected boolean isReleased;
	protected boolean isOver;
	protected boolean isDisabled;
	
	public BaseActor () {
		
	}
	
	public void act (float x, float y, float width, float height) {
		isReleased = false;
		if (!isDisabled) {
			Vector3 cursor = CameraController.getInstance().unproject();
			if (cursor.x >= x && cursor.x <= x + width && cursor.y >= y && cursor.y <= y + height) {
				isOver = true;
				if (Events.getInstance().isMouseLeftPressed()) {
					isPressed = true;
				} else {
					if (isPressed) {
						if (Events.getInstance().isMouseLeftReleased()) {
							isReleased = true;
							isPressed = false;
						}
					}
				}
			} else {
				isOver = false;
				if (Events.getInstance().isMouseLeftReleased()) {
					isReleased = false;
					isPressed = false;
				}
			}	
		} else {
			isOver = isPressed = isReleased = false;
		}
	}
	
	public void reset () {
		isPressed = false;
		isReleased = false;
		isOver = false;
	}
	
	public void setDisable (boolean value) {
		isDisabled = value;
	}
	
	public boolean isPressed () {
		return isPressed;
	}
	
	public boolean isReleased () {
		return isReleased;
	}
	
	public boolean isOver () {
		return isOver;
	}
	
	public boolean isDisabled () {
		return isDisabled;
	}
}