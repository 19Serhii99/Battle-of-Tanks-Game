package map;

import com.badlogic.gdx.utils.Array;

public class Vertex {
	private float x;
	private float y;
	
	private boolean isSpawnOne = false;
	private boolean isSpawnTwo = false;
	
	private Array <Vertex> neighbors;
	private Array <Vertex> tempNeighbors;
	
	public Vertex (float x, float y) {
		setPosition(x, y);
		neighbors = new Array <Vertex>(1);
		tempNeighbors = new Array <Vertex>(0);
	}
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSpawnOne (boolean value) {
		this.isSpawnOne = value;
	}
	
	public void setSpawnTwo (boolean value) {
		this.isSpawnTwo = value;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public Array <Vertex> getNeighbors () {
		return neighbors;
	}
	
	public Array <Vertex> getTempNeighbors () {
		return tempNeighbors;
	}
	
	public boolean isSpawnOne () {
		return isSpawnOne;
	}
	
	public boolean isSpawnTwo () {
		return isSpawnTwo;
	}
}