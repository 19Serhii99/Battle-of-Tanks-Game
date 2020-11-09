package gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button extends BaseActor {
	protected Sprite background;
	private Texture texture;
	private Texture textureOver;
	private Texture textureFocused;
	
	public Button () {
		background = new Sprite();
	}
	
	public void setTexture (Texture texture) {
		this.texture = texture;
	}
	
	public void setTextureOver (Texture textureOver) {
		this.textureOver = textureOver;
	}
	
	public void setTextureFocused (Texture textureFocused) {
		this.textureFocused = textureFocused;
	}
	
	public void setPosition (float x, float y) {
		background.setPosition(x, y);
	}
	
	public void setSize (float width, float height) {
		background.setSize(width, height);
	}
	
	public void show (SpriteBatch batch) {
		if (super.isPressed) {
			background.setRegion(textureFocused == null ? textureOver == null ? texture : textureOver : textureFocused);
		} else if (super.isOver) {
			background.setRegion(textureOver != null ? textureOver : texture);
		} else {
			background.setRegion(texture);
		}
		background.draw(batch);
		super.act(background.getX(), background.getY(), background.getWidth(), background.getHeight());
	}
	
	public Texture getTexture () {
		return texture;
	}
	
	public Texture getTextureOver () {
		return textureOver;
	}
	
	public Texture getTextureFocused () {
		return textureFocused;
	}
	
	public Sprite getSprite () {
		return background;
	}
	
	public float getX () {
		return background.getX();
	}
	
	public float getY () {
		return background.getY();
	}
	
	public float getWidth () {
		return background.getWidth();
	}
	
	public float getHeight () {
		return background.getHeight();
	}
}