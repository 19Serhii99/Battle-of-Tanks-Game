package answers;

import java.io.Serializable;

public class MyLocalAnswer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private float x;
	private float y;
	private float rotation;
	private float rotationTower;
	private float xTower;
	private float yTower;
	
	public MyLocalAnswer (float x, float y, float rotation, float rotationTower, float xTower, float yTower) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.rotationTower = rotationTower;
		this.xTower = xTower;
		this.yTower = yTower;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public float getRotation () {
		return rotation;
	}
	
	public float getRotationTower () {
		return rotationTower;
	}
	
	public float getXTower () {
		return xTower;
	}
	
	public float getYTower () {
		return yTower;
	}
}