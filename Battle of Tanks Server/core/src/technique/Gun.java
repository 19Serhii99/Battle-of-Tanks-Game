package technique;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Gun {
	private Array <Vector2> points;
	private Cassette cassette;
	
	private float shellSpeed;
	private float shellWidth;
	private float shellHeight;
	private boolean isCassette = false;
	
	public Gun () {
		points = new Array <Vector2>(1);
	}
	
	public void setShellSpeed (float speed) {
		shellSpeed = speed;
	}
	
	public void setShellWidth (float width) {
		shellWidth = width;
	}
	
	public void setShellHeight (float height) {
		shellHeight = height;
	}
	
	public void setCassette (int maxCount, float timeRecharge, ShootingStyle shootingStyle) {
		isCassette = true;
		cassette = new Cassette();
		cassette.setMaxCount(maxCount);
		cassette.setTimeRecharge(timeRecharge);
		cassette.setShootingStyle(shootingStyle);
	}
	
	public Array <Vector2> getPoints () {
		return points;
	}
	
	public float getShellSpeed () {
		return shellSpeed;
	}
	
	public Cassette getCassette () {
		return cassette;
	}
	
	public boolean isCassette () {
		return isCassette;
	}
	
	public float getShellWidth () {
		return shellWidth;
	}
	
	public float getShellHeight () {
		return shellHeight;
	}
}