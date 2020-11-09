package map;

public class Tile {
	private float x;
	private float y;
	
	private MapTexture mapTexture;
	
	public Tile (MapTexture mapTexture) {
		this.mapTexture = mapTexture;
	}
	
	public void setMapTexture (MapTexture mapTexture) {
		this.mapTexture = mapTexture;
	}
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public MapTexture getMapTexture () {
		return mapTexture;
	}
}