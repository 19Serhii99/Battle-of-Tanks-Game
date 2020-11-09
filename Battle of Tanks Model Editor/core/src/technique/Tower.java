package technique;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

public class Tower extends Sprite implements Disposable {
	private ShootingStyle shootingStyle;
	private Vector2 centerPoint;
	
	private float speedRotation;
	
	public Tower (Texture texture) {
		super(texture);
		
		centerPoint = new Vector2(0.0f, 0.0f);
	}
	
	public void setShootingStyle (ShootingStyle shootingStyle) {
		this.shootingStyle = shootingStyle;
	}
	
	public void setSpeedRotation (float speedRotation) {
		this.speedRotation = speedRotation;
	}
	
	public void movePoints (float xAmount, float yAmount) {		
		centerPoint.x += xAmount;
		centerPoint.y += yAmount;
	}
	
	public ShootingStyle getShootingStyle () {
		return shootingStyle;
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