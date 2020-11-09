package technique;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Shooting {
	private ShootingStyle shootingStyle;
	private Array <Shell> shells;
	private Array <Vector2> gunPoints;
	private Sound sound;
	private Texture shellTexture;
	private Cassette cassette;
	
	private float speed;
	private float timeRecharge;
	private long timeStartRecharge;
	private int index;
	private int totalShells;
	private boolean isCassette;
	private boolean isRecharge;
	
	public Shooting (int totalShells, Array <Vector2> gunPoints) {
		this.gunPoints = gunPoints;
		this.totalShells = totalShells;
		this.shells = new Array <Shell>(totalShells);
		this.isCassette = false;
		this.isRecharge = false;
	}
	
	public void setCassette (int maxCount, float timeRecharge) {
		cassette = Cassette.getInstance();
		cassette.setMaxCount(maxCount);
		cassette.setTimeRecharge(timeRecharge);
		isCassette = true;
	}
	
	public void setSpeed (float speed) {
		this.speed = speed;
	}
	
	public void setSound (Sound sound) {
		this.sound = sound;
	}
	
	public void setShootingStyle (ShootingStyle shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public void setTimeRecharge (float timeRecharge) {
		this.timeRecharge = timeRecharge;
	}
	
	public void setTexture (Texture texture) {
		shellTexture = texture;
	}
	
	public void createShell (float nextX, float nextY) {
		if (isRecharge) {
			if (isCassette) {
				if (cassette.getCurrentCount() == 0) {
					if ((float) (System.currentTimeMillis() - timeStartRecharge) / 1000 >= timeRecharge) {
						isRecharge = false;
						if (totalShells - cassette.getMaxCount() < 0) {
							cassette.setCount(totalShells);
							totalShells = 0;
						} else {
							cassette.setCount(cassette.getMaxCount());
							totalShells -= cassette.getMaxCount();
						}
					}
				} else {
					if ((float) (System.currentTimeMillis() - timeStartRecharge) / 1000 >= cassette.getTimeRecharge()) {
						isRecharge = false;
					}
				}
			} else {
				if ((float) (System.currentTimeMillis() - timeStartRecharge) / 1000 >= timeRecharge) {
					isRecharge = false;
				}
			}
		} else {
			if (isCassette) {
				if (shootingStyle == ShootingStyle.IN_TURN) {
					creatingShell(gunPoints.get(index).x, gunPoints.get(index).y, nextX, nextY);
					index++;
					cassette.setCount(cassette.getCurrentCount() - 1);
					if (index > gunPoints.size - 1) index = 0;
				} else if (shootingStyle == ShootingStyle.TOGETHER) {
					for (Vector2 point : gunPoints) {
						creatingShell(point.x, point.y, nextX, nextY);
						cassette.setCount(cassette.getCurrentCount() - 1);
						if (cassette.getCurrentCount() == 0) break;
					}
				} else if (shootingStyle == ShootingStyle.RANDOM) {
					index = MathUtils.random(0, gunPoints.size - 1);
					creatingShell(gunPoints.get(index).x, gunPoints.get(index).y, nextX, nextY);
					cassette.setCount(cassette.getCurrentCount() - 1);
				}
				sound.play();
				isRecharge = true;
				timeStartRecharge = System.currentTimeMillis();
			} else {
				if (totalShells > 0) {
					creatingShell(gunPoints.get(0).x, gunPoints.get(0).y, nextX, nextY);
					sound.play();
					isRecharge = true;
					timeStartRecharge = System.currentTimeMillis();
				}
			}
		}
	}
	
	private void creatingShell (float x, float y, float nextX, float nextY) {
		Shell shell = new Shell(shellTexture);
		shell.setSpeed(speed);
		shell.setSize(50, 25);
		shell.setOriginCenter();
		shell.nextPoint(x, y, nextX, nextY);
		shells.add(shell);
	}
	
	public Array <Shell> getShells () {
		return shells;
	}
	
	public float getSpeed () {
		return speed;
	}
	
	public float getTimeRecharge () {
		return timeRecharge;
	}
	
	public Sound getSound () {
		return sound;
	}
	
	public ShootingStyle getShootingStyle() {
		return shootingStyle;
	}
	
	public Array <Vector2> getGunPoints () {
		return gunPoints;
	}
	
	public Texture getTexture () {
		return shellTexture;
	}
}