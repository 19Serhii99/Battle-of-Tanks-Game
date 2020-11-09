package util;

import com.badlogic.gdx.math.Vector2;

public class Axis {
	private Vector2 axisX;
	private Vector2 axisY;
	
	public Axis () {
		axisX = new Vector2(1.0f, 0.0f);
		axisY = new Vector2(0.0f, 1.0f);
	}
	
	public void turnAxis (float angle) {
		axisX.x = 1.0f;
		axisX.y = 0.0f;
		axisY.x = 0.0f;
		axisY.y = 1.0f;
		
		final float sin = (float)Math.sin(Math.abs(angle) * Math.PI / 180);
		final float cos = (float)Math.cos(Math.abs(angle) * Math.PI / 180);
		
		final Vector2 newAxisX = new Vector2(1.0f, 0.0f);
		final Vector2 newAxisY = new Vector2(0.0f, 1.0f);
		
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
		final float tempX = x - x0;
		final float tempY = y - y0;
		
		final float newX = tempX * axisX.x + tempY * axisY.x;
		final float newY = tempX * axisX.y + tempY * axisY.y;
		
		return new Vector2(newX + x0, newY + y0);
	}
}