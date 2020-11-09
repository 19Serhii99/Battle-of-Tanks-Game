package queries;

import java.io.Serializable;
import java.util.ArrayList;

public class BattlePlayerData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList <String> keys;
	
	private float delta;
	private float cursorX;
	private float cursorY;
	private boolean isMouseLeftPressed;
	
	public BattlePlayerData (float delta) {
		this.delta = delta;
		keys = new ArrayList <String>(1);
	}
	public void setCursorPosition (float x, float y) {
		cursorX = x;
		cursorY = y;
	}
	
	public void setMouseLeftPressed (boolean value) {
		this.isMouseLeftPressed = value;
	}
	
	public ArrayList <String> getKeys () {
		return keys;
	}
	
	public float getCursorX () {
		return cursorX;
	}
	
	public float getCursorY () {
		return cursorY;
	}
	
	public boolean isMouseLeftPressed () {
		return isMouseLeftPressed;
	}
	
	public float getDelta () {
		return delta;
	}
}