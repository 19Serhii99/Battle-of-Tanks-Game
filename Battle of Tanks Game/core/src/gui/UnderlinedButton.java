package gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import util.TextureCreator;

public class UnderlinedButton extends BaseActor implements Disposable {
	private Label label;
	private Label labelOver;
	private Label labelFocused;
	private Texture textureLabel;
	private Texture textureLabelOver;
	private Texture textureLabelFocused;
	private Sprite underline;
	
	private float height;
	private final float spaceY = 10.0f;
	
	public UnderlinedButton (String text, BitmapFont bitmapFont, Color color) {
		label = new Label(text, bitmapFont);
		textureLabel = TextureCreator.createTexture(color);
		underline = new Sprite(textureLabel);
		underline.setSize(label.getWidth(), 2);
		underline.setPosition(label.getX(), label.getY() - label.getHeight() - underline.getHeight() - spaceY);
		height = label.getHeight() + spaceY + underline.getHeight();
	}
	
	public void setPosition (float x, float y) {
		label.setPosition(x, y);
		underline.setPosition(label.getX(), label.getY() - label.getHeight() - spaceY);
		height = label.getHeight() + spaceY + underline.getHeight();
	}
	
	public void show (SpriteBatch batch) {
		super.act(label.getX(), underline.getY(), underline.getWidth(), height);
		if (super.isOver) {
			if (labelOver != null) labelOver.draw(batch);
			else label.draw(batch);
			underline.setRegion(textureLabelOver == null ? textureLabel : textureLabelOver);
		} else if (super.isPressed) {
			if (labelFocused != null) labelFocused.draw(batch);
			else label.draw(batch);
			underline.setRegion(textureLabelFocused == null ? textureLabel : textureLabelFocused);
		} else {
			label.draw(batch);
			underline.setRegion(textureLabel);
		}
		underline.draw(batch);
	}
	
	public void setBitmapFontOver (BitmapFont bitmapFont, Color color) {
		labelOver = new Label(label.getText(), bitmapFont);
		labelOver.setPosition(label.getX(), label.getY());
		textureLabelOver = TextureCreator.createTexture(color);
	}
	
	public void setBitmapFontFocused (BitmapFont bitmapFont, Color color) {
		labelFocused = new Label(label.getText(), bitmapFont);
		labelFocused.setPosition(label.getX(), label.getY());
		textureLabelFocused = TextureCreator.createTexture(color);
	}
	
	public void setAlpha (float alpha) {
		label.setAlphas(alpha);
		if (labelOver != null) labelOver.setAlphas(alpha);
		if (labelFocused != null) labelFocused.setAlphas(alpha);
		underline.setAlpha(alpha);
	}

	@Override
	public void dispose() {
		textureLabel.dispose();
		if (textureLabelOver != null) textureLabelOver.dispose();
		if (textureLabelFocused != null) textureLabelFocused.dispose();
	}
}