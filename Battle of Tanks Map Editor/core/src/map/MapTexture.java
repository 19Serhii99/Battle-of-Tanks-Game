package map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class MapTexture implements Disposable {
	private int id;
	private Texture texture;
	
	public MapTexture (int id, Texture texture) {
		setId(id);
		setTexture(texture);
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public void setTexture (Texture texture) {
		this.texture = texture;
	}
	
	public int getId () {
		return id;
	}
	
	public Texture getTexture () {
		return texture;
	}
	
	@Override
	public void dispose () {
		if (texture.getTextureData().isPrepared()) texture.getTextureData().consumePixmap().dispose();
		texture.dispose();
	}
}