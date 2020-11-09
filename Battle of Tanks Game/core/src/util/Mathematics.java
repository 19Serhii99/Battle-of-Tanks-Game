package util;

import com.badlogic.gdx.math.Vector2;

public class Mathematics {
	public static float getDistance (float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	public static Vector2 getIntersectionPoint (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float x = 0, y = 0;
		
		float differenceLeftX = x2 - x1;
		float differenceRightX = x4 - x3;
		float differenceLeftY = y2 - y1;
		float differenceRightY = y4 - y3;
		
		if (differenceLeftX == 0.0f || differenceRightX == 0.0f) {
			if (differenceLeftX == 0.0f) {
				x = x1;
				y = (-x * differenceRightY  - (-x3 * differenceRightY + differenceRightX * y3)) / - differenceRightX;
			} else {
				x = x3;
				y = (-x * differenceLeftY  - (-x1 * differenceLeftY + differenceLeftX * y1)) / - differenceLeftX;
			}
		} else if (differenceLeftY == 0.0f || differenceRightY == 0.0f) {
			if (differenceLeftY == 0.0f) {
				y = y1;
				x = (differenceRightX * y - (-x3 * differenceRightY + differenceRightX * y3)) / differenceRightY;
			} else {
				y = y3;
				x = (differenceLeftX * y - (-x1 * differenceLeftY + differenceLeftX * y1)) / differenceLeftY;
			}
		} else {
			float leftC = - x1 * differenceLeftY + differenceLeftX * y1;
			float rightC = -x3 * differenceRightY + differenceRightX * y3;
			float a = differenceLeftX * differenceRightY;
			float b = - leftC * differenceRightY + rightC * differenceLeftY;
			float c = - differenceRightX * differenceLeftY;
			float d = a + c;
			y = - b / d;
			x = (differenceLeftX * y - leftC) / differenceLeftY;
		}
		return new Vector2(x, y);
	}
	
	public static float getSemiperimeter (float a, float b, float c) {
		return (a + b + c) / 2;
	}
	
	public static float getTriangleArea (float a, float b, float c) {
		float p = getSemiperimeter(a, b, c);
		return (float) Math.sqrt(p * (p - a) * (p - b) * (p - c));
	}
	
	public static boolean isCollision (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4, GeometricData data, float pointX, float pointY) {
		float vector1 = getDistance(x1, y1, pointX, pointY);
		float vector2 = getDistance(x2, y2, pointX, pointY);
		float vector3 = getDistance(x3, y3, pointX, pointY);
		float vector4 = getDistance(x4, y4, pointX, pointY);
		float area1 = getTriangleArea(vector1, vector2, data.getLeftEdge());
		float area2 = getTriangleArea(vector2, vector3, data.getTopEdge());
		float area3 = getTriangleArea(vector1, vector3, data.getDiagonal());
		if (area1 + area2 + area3 <= data.getArea() + 1) {
			return true;
		} else {
			float area4 = getTriangleArea(vector1, vector4, data.getBottomEdge());
			float area5 =  getTriangleArea(vector4, vector3, data.getRightEdge());
			return area3 + area4 + area5 <= data.getArea() + 1 ? true : false;
		}
	}
	
	public static boolean pointInSegment (float x1, float y1, float x2, float y2, float pointX, float pointY) {
		return ((pointX >= x1 && pointX <= x2) || (pointX >= x2 && pointX <= x1)) && ((pointY >= y1 && pointY <= y2) || (pointY >= y2 && pointY <= y1));
	}
}