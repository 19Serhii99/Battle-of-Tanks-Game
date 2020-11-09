package map;

public class MapObjectBase implements Wrapper {
	private float x;
	private float y;
	private float width;
	private float height;
	
	public MapObjectBase () {
		setPosition(x, y);
	}
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize (float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public float getWidth () {
		return width;
	}
	
	public float getHeight () {
		return height;
	}
	
	@Override
	public boolean isWrapper () {
		return false;
	}
}