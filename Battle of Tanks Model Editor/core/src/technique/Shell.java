package technique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

import util.Mathematics;

public class Shell extends Sprite implements Disposable {
	private float distanceX;
	private float distanceY;
	private float speed;
	private boolean isLive;
	
	public Shell (Texture texture) {
		super(texture);
		isLive = true;
	}
	
	public void setSpeed (float speed) {
		this.speed = speed;
	}
	
	public void setEnableLive (boolean value) {
		isLive = value;
	}
	
	public void nextPoint (float x, float y, float nextX, float nextY) {
		super.setPosition(x - super.getWidth() / 2, y - super.getHeight() / 2);
		
		float tempX = x + super.getWidth() / 2;
		float tempY = y + super.getHeight() / 2;
		
		float distance = Mathematics.getDistance(tempX, tempY, nextX, nextY);	
		float angle = MathUtils.atan2(nextY - tempY, nextX - tempX) * 180 / MathUtils.PI;
		
		if (angle < 0) angle += 360;
		
		super.setRotation(angle);
		
		distanceX = ((nextX - tempX) / distance) * speed;
		distanceY = ((nextY - tempY) / distance) * speed;
	}
	
	public void moveTo () {
		super.translate(distanceX * Gdx.graphics.getDeltaTime(), distanceY * Gdx.graphics.getDeltaTime());
	}
	
	public float getSpeed () {
		return speed;
	}
	
	public boolean isLive () {
		return isLive;
	}
	
	@Override
	public void dispose () {
		super.getTexture().dispose();
	}
	
}