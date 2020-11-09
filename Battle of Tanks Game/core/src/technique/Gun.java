package technique;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Gun implements Disposable {
	private Array <Vector2> points;
	private Texture shellTexture;
	private Sound shotSound;
	private Sound hitSound;
	private Cassette cassette;
	
	private float shellSpeed;
	private float shellWidth;
	private float shellHeight;
	private boolean isCassette = false;
	
	public Gun () {
		points = new Array <Vector2>(1);
	}
	
	public void setShellTexture (Texture texture) {
		shellTexture = texture;
	}
	
	public void setShellSpeed (float speed) {
		shellSpeed = speed;
	}
	
	public void setShotSound (Sound sound) {
		shotSound = sound;
	}
	
	public void setHitSound (Sound sound) {
		hitSound = sound;
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
	
	public Texture getShellTexture () {
		return shellTexture;
	}
	
	public float getShellSpeed () {
		return shellSpeed;
	}
	
	public Sound getShotSound () {
		return shotSound;
	}
	
	public Sound getHitSound () {
		return hitSound;
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
	
	@Override
	public void dispose () {
		if (shellTexture != null) shellTexture.dispose();
		if (shotSound != null) shotSound.dispose();
		if (hitSound != null) hitSound.dispose();
	}
}