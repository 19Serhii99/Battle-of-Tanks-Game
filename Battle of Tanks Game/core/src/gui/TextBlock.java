package gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TextBlock {
	private BitmapFont bitmapFont;
	private Array <Label> text;
	private float x;
	private float y;
	private float width;
	private float height;
	
	public TextBlock (String string, BitmapFont bitmapFont, float x, float y, float width, float height) {
		text = new Array <Label>(1);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bitmapFont = bitmapFont;
		String [] strings = string.split(" ");
		float tempX = x;
		Label label = new Label(strings[0], bitmapFont);
		for (int i = 0; i < strings.length; i++) {
			if (i > 0) {
				Label tempLabel = new Label(strings[i], bitmapFont);
				if (tempX != x) {
					if (label.getWidth() >= tempX - x + width) {
						
					}
				}	
			}
		}
	}
	
	public void show (SpriteBatch batch) {
		for (Label label : text) {
			label.draw(batch);
		}
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
}