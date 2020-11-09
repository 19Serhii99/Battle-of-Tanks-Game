package util;

import com.badlogic.gdx.math.Vector2;

public class Axis {
	private static Axis instance;
	private Vector2 axisX;
	private Vector2 axisY;
	
	private Axis () {
		axisX = new Vector2(1.0f, 0.0f);
		axisY = new Vector2(0.0f, 1.0f);
	}
	
	public static Axis getInstance () {
		if (instance == null) instance = new Axis();
		return instance;
	}
	
	public void turnAxis (float angle) {
		axisX.x = 1.0f;
		axisX.y = 0.0f;
		axisY.x = 0.0f;
		axisY.y = 1.0f;
		
		final float sin = (float)Math.sin(Math.abs(angle) * Math.PI / 180);
		final float cos = (float)Math.cos(Math.abs(angle) * Math.PI / 180);
		
		Vector2 newAxisX = new Vector2(1.0f, 0.0f);
		Vector2 newAxisY = new Vector2(0.0f, 1.0f);
		
		if (angle > 0) {
			newAxisX.x = axisX.x * cos - axisX.y * sin;
			newAxisX.y = axisX.x * sin + axisX.y * cos;
			newAxisY.x = axisY.x * cos - axisY.y * sin;
			newAxisY.y = axisY.x * sin + axisY.y * cos;
		} else {
			newAxisX.x = axisX.x * cos + axisX.y * sin;
			newAxisX.y = -axisX.x * sin + axisX.y * cos;
			newAxisY.x = axisY.x * cos + axisY.y * sin;
			newAxisY.y = -axisY.x * sin + axisY.y * cos;
		}
		
		axisX = newAxisX;
		axisY = newAxisY;
	}
	
	public Vector2 turnPoint (float x0, float y0, float x, float y) {
		float tempX = x - x0;
		float tempY = y - y0;
		
		float newX = tempX * axisX.x + tempY * axisY.x;
		float newY = tempX * axisX.y + tempY * axisY.y;
		
		return new Vector2(newX + x0, newY + y0);
	}
}