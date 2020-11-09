package gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CheckBox extends BaseActor {
	private Sprite background;
	private Sprite checkMark;
	private Label label;
	private Texture texture;
	private Texture textureOver;
	private Texture textureFocused;
	private Texture textureCheckMark;
	
	private float width;
	private float height;
	
	private boolean isChecked = false;
	
	private final float spaceX = 5.0f;
	
	public CheckBox (String text, BitmapFont bitmapFont) {
		background = new Sprite();
		label = new Label(text, bitmapFont);
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
		label.setPosition(background.getX() + background.getWidth() + spaceX, background.getY() + background.getHeight() / 2 + label.getHeight() / 2);
	}
	
	public void setIconSize (float width, float height) {
		background.setSize(width, height);
		label.setPosition(background.getX() + background.getWidth() + spaceX, background.getY() + background.getHeight() / 2 + label.getHeight() / 2);
		this.width = background.getX() + background.getWidth() + spaceX + label.getWidth();
		this.height = Math.max(background.getHeight(), label.getHeight());
	}
	
	public void setCheckMark (Texture texture, float shift) {
		textureCheckMark = texture;
		checkMark = new Sprite(textureCheckMark);
		checkMark.setSize(15, 15);
		checkMark.setPosition(background.getX() + background.getWidth() / 2 - checkMark.getWidth() / 2, background.getY() + shift);
	}
	
	public void setChecked (boolean value) {
		isChecked = value;
	}
	
	public void show (SpriteBatch batch) {
		super.act(background.getX(), background.getY(), width, height);
		if (super.isPressed) {
			background.setRegion(textureFocused == null ? textureOver : textureFocused);
		} else if (super.isOver) {
			background.setRegion(textureOver == null ? texture : textureOver);
		} else {
			background.setRegion(texture);
		}
		if (super.isReleased) isChecked = isChecked ? false : true;
		background.draw(batch);
		label.draw(batch);
		if (isChecked && checkMark != null) checkMark.draw(batch);
	}
	
	public void setAlpha (float alpha) {
		background.setAlpha(alpha);
		label.setAlphas(alpha);
		if (checkMark != null) checkMark.setAlpha(alpha);
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
	
	public Texture getCheckMark () {
		return textureCheckMark;
	}
	
	public float getX () {
		return background.getX();
	}
	
	public float getY () {
		return background.getY();
	}
	
	public float getWidth () {
		return width;
	}
	
	public float getHeight () {
		return height;
	}
	
	public boolean isChecked () {
		return isChecked;
	}
}