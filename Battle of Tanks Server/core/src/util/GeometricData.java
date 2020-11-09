package util;

public class GeometricData {
	private float area;
	private float leftEdge;
	private float rightEdge;
	private float bottomEdge;
	private float topEdge;
	private float diagonal;
	
	public GeometricData () {
		
	}
	
	public void calculateData (float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		leftEdge = Mathematics.getDistance(x1, y1, x2, y2);
		rightEdge = Mathematics.getDistance(x4, y4, x3, y3);
		bottomEdge = Mathematics.getDistance(x1, y1, x4, y4);
		topEdge = Mathematics.getDistance(x2, y2, x3, y3);
		diagonal = Mathematics.getDistance(x1, y1, x3, y3);
		area = Mathematics.getTriangleArea(leftEdge, topEdge, diagonal);
	}
	
	public float getArea () {
		return area;
	}
	
	public float getLeftEdge () {
		return leftEdge;
	}
	
	public float getRightEdge () {
		return rightEdge;
	}
	
	public float getBottomEdge () {
		return bottomEdge;
	}
	
	public float getTopEdge () {
		return topEdge;
	}
	
	public float getDiagonal () {
		return diagonal;
	}
}