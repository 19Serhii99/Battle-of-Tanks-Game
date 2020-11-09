package technique;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Body extends Sprite implements Disposable {
	private float acceleration;
	private float maxSpeed;
	private float speed;
	private float speedRotation;
	
	private Vector2 centerPoint;
	
	public Body (Texture texture) {
		super(texture);
		
		centerPoint = new Vector2(0.0f, 0.0f);
	}
	
	public void setSpeed (float speed) {
		this.speed = speed;
	}
	
	public void setAcceleration (float acceleration) {
		this.acceleration = acceleration;
	}
	
	public void setMaxSpeed (float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public void setSpeedRotation (float speedRotation) {
		this.speedRotation = speedRotation;
	}
	
	public float getAcceleration () {
		return acceleration;
	}
	public float getMaxSpeed () {
		return maxSpeed;
	}

	public float getSpeed () {
		return speed;
	}
	
	public float getSpeedRotation () {
		return speedRotation;
	}
	
	public Vector2 getCenterPoint () {
		return centerPoint;
	}
	
	@Override
	public void dispose () {
		super.getTexture().dispose();
	}
}