package gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextButton extends Button {
	private Label label;
	private Label labelFocused;
	
	public TextButton (String text, BitmapFont bitmapFont) {
		label = new Label(text, bitmapFont);
	}
	
	public void setSize (float width, float height) {
		super.setSize(width, height);
		label.setPosition(super.getX() + super.getWidth() / 2 - label.getWidth() / 2, super.getY() + super.getHeight() / 2 + label.getHeight() / 2);
		if (labelFocused != null) labelFocused.setPosition(label.getX(), label.getY());
	}
	
	public void setPosition (float x, float y) {
		super.setPosition(x, y);
		label.setPosition(super.getX() + super.getWidth() / 2 - label.getWidth() / 2, super.getY() + super.getHeight() / 2 + label.getHeight() / 2);
		if (labelFocused != null) labelFocused.setPosition(label.getX(), label.getY());
	}
	
	public void setBitmapFontFocused (BitmapFont bitmapFont) {
		labelFocused = new Label(label.getText(), bitmapFont);
		labelFocused.setPosition(label.getX(), label.getY());
	}
	
	public void setAlpha (float alpha) {
		super.background.setAlpha(alpha);
		label.setAlphas(alpha);
		if (labelFocused != null) labelFocused.setAlphas(alpha);
	}
	
	public void show (SpriteBatch batch) {
		super.show(batch);
		if (super.isPressed && labelFocused != null) {
			labelFocused.draw(batch);
		} else {
			label.draw(batch);
		}
	}
	
	public BitmapFont getLabelBitmapFont () {
		return label.getFont();
	}
	
	public BitmapFont getLabelFocusedBitmapFont () {
		return labelFocused.getFont();
	}
}