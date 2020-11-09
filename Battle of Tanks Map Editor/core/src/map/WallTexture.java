package map;

import com.badlogic.gdx.graphics.Texture;

public class WallTexture extends MapTexture {
	private String path;
	
	public int using = 0;
	
	public WallTexture (int id, Texture texture, String path) {
		super(id, texture);
		this.path = path;
	}
	
	public String getPath () {
		return path;
	}
}