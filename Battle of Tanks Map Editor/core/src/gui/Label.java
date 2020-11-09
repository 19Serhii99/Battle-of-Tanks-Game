package gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;

import util.Font;

public class Label extends BitmapFontCache {
	private float width;
	private float height;
	
	private String text;
	
	public Label(String text, BitmapFont bitmapFont) {
		super(bitmapFont);
		this.setText(text);
	}
	
	public void setText (String text) {
		this.text = text;
		super.setText(text, 0, 0);
		
		width = Font.getInstance().getWidth(super.getFont(), text);
		height = Font.getInstance().getHeight(super.getFont(), text);
	}
	
	public float getWidth () {
		return width;
	}
	
	public float getHeight () {
		return height;
	}
	
	public String getText () {
		return text;
	}
}