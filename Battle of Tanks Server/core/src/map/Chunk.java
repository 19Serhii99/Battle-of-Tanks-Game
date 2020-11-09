package map;

import com.badlogic.gdx.utils.Array;

public class Chunk {
	private float x;
	private float y;
	
	private float virtualWidth;
	private float virtualHeight;
	
	private boolean isActivated = false;
	private boolean isRightLast = false;
	private boolean isBottomLast = false;
	
	private Array <Tile> tiles;
	private Array <ChunkNeighbor> neighbors;
	private Array <Vertex> vertices;
	private Array <MapObjectBase> mapObjects;
	private Array <MapObject> tempMapObjects;
	
	public Chunk (float x, float y) {
		tiles = new Array <Tile>(1);
		neighbors = new Array <ChunkNeighbor>(1);
		vertices = new Array <Vertex>(0);
		mapObjects = new Array <MapObjectBase>(1);
		tempMapObjects = new Array <MapObject>(1);
		setPosition(x, y);
	}
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void changePosition (float x, float y) {
		float width = x - this.x;
		float height = y - this.y;
		this.x += width;
		this.y += height;
		for (Tile tile : tiles) {
			tile.setPosition(tile.getX() + width, tile.getY() + height);
		}
		for (Vertex vertex : vertices) {
			vertex.setPosition(vertex.getX() + width, vertex.getY() + height);
		}
		for (MapObjectBase mapObject : mapObjects) {
			mapObject.setPosition(mapObject.getX() + width, mapObject.getY() + height);
		}
	}
	
	public void setActivated (boolean activated) {
		this.isActivated = activated;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public void setRightLast (boolean last) {
		this.isRightLast = last;
	}
	
	public void setBottomLast (boolean last) {
		this.isBottomLast = last;
	}
	
	public void setVirtualWidth (float width) {
		this.virtualWidth = width;
	}
	
	public void setVirtualHeight (float height) {
		this.virtualHeight = height;
	}
	
	public boolean isActivated () {
		return isActivated;
	}
	
	public Array <Tile> getTiles () {
		return tiles;
	}
	
	public Array <ChunkNeighbor> getNeighbors () {
		return neighbors;
	}
	
	public Array <Vertex> getVertices () {
		return vertices;
	}
	
	public Array <MapObjectBase> getMapObjects () {
		return mapObjects;
	}
	
	public Array <MapObject> getTempMapObjects () {
		return tempMapObjects;
	}
	
	public boolean isRightLast () {
		return isRightLast;
	}
	
	public boolean isBottomLast () {
		return isBottomLast;
	}
	
	public float getVirtualWidth () {
		return virtualWidth;
	}
	
	public float getVirtualHeight () {
		return virtualHeight;
	}
}