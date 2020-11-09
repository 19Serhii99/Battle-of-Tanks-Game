package map;

import com.badlogic.gdx.utils.Array;

public class Chunk {
	private float x;
	private float y;
	
	private boolean isActivated = false;
	
	private Array <Tile> tiles;
	private Array <ChunkNeighbor> neighbors;
	private Array <Vertex> vertices;
	private Array <MapObjectBase> mapObjects;
	
	private BaseManager baseOne;
	private BaseManager baseTwo;
	
	public Chunk (float x, float y) {
		tiles = new Array <Tile>(1);
		neighbors = new Array <ChunkNeighbor>(1);
		vertices = new Array <Vertex>(0);
		mapObjects = new Array <MapObjectBase>(0);
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
		if (baseOne != null) baseOne.getBase().translate(width, height);
		if (baseTwo != null) baseTwo.getBase().translate(width, height);
	}
	
	public void setActivated (boolean activated) {
		this.isActivated = activated;
	}
	
	public void setBaseOne (BaseManager base) {
		this.baseOne = base;
	}
	
	public void setBaseTwo (BaseManager base) {
		this.baseTwo = base;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
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
	
	public BaseManager getBaseOne () {
		return baseOne;
	}
	
	public BaseManager getBaseTwo () {
		return baseTwo;
	}
}