package map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class MapTexture implements Disposable {
	private int id;
	
	private Texture texture;
	private String path;
	
	public MapTexture (int id, Texture texture, String path) {
		setId(id);
		setTexture(texture);
		setPath(path);
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public void setTexture (Texture texture) {
		this.texture = texture;
	}
	
	public void setPath (String path) {
		this.path = path;
	}
	
	public int getId () {
		return id;
	}
	
	public Texture getTexture () {
		return texture;
	}
	
	public String getPath () {
		return path;
	}
	
	@Override
	public void dispose () {
		texture.dispose();
	}
}