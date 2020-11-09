package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import util.Font;
import util.TextureCreator;

public class PopupMenu implements Disposable {
	private Array <TextButton> buttons;
	private Sprite border;
	private Texture texture;
	private Texture textureOver;
	private Texture textureFocused;
	private BitmapFont bitmapFont;
	private BitmapFont bitmapFontFocused;
	
	private float x;
	private float y;
	private float width;
	private float height;
	private float borderWeight;
	
	private boolean isHide = false;
	private float alpha = 0.0f;
	
	private int idItem;
	private boolean isSelected = false;
	
	public PopupMenu (Color color, Color colorFocused, int fontSize, Texture texture, Texture textureOver, Texture textureFocused, float x, float y, float width, float height) {
		this.texture = texture;
		this.textureOver = textureOver;
		this.textureFocused = textureFocused;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		bitmapFont = Font.getInstance().generateBitmapFont(color, fontSize);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		if (colorFocused != null) {
			bitmapFontFocused = Font.getInstance().generateBitmapFont(colorFocused, fontSize);
			bitmapFontFocused.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		buttons = new Array <TextButton>(1);
	}
	
	public void setBorder (float weight, Color color) {
		border = new Sprite(TextureCreator.createTexture(color));
		this.borderWeight = weight;
	}
	
	public void addItem (String text) {
		TextButton button = new TextButton(text, bitmapFont);
		if (texture != null) button.setTexture(texture);
		if (textureFocused != null) button.setTextureFocused(textureFocused);
		if (textureOver != null) button.setTextureOver(textureOver);
		if (bitmapFontFocused != null) button.setBitmapFontFocused(bitmapFontFocused);
		button.setSize(width, height);
		if (buttons.size == 0) {
			button.setPosition(x, y - button.getHeight());
		} else {
			button.setPosition(x, buttons.get(buttons.size - 1).getY() - button.getHeight());
		}
		buttons.add(button);
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		batch.begin();
		for (int i =  0; i < buttons.size; i++) {
			buttons.get(i).setAlpha(alpha);
			buttons.get(i).show(batch);
			if (buttons.get(i).isReleased()) {
				isSelected = true;
				idItem = i;
			}
		}
		if (border != null) {
			border.setAlpha(alpha);
			
			border.setSize(borderWeight, buttons.size * height);
			border.setPosition(x, buttons.get(buttons.size - 1).getY());
			border.draw(batch);
			
			border.setSize(borderWeight, buttons.size * height);
			border.setPosition(x + width - borderWeight, buttons.get(buttons.size - 1).getY());
			border.draw(batch);
			
			border.setSize(width, borderWeight);
			border.setPosition(x, buttons.get(buttons.size - 1).getY());
			border.draw(batch);
			
			border.setSize(width, borderWeight);
			border.setPosition(x, buttons.get(buttons.size - 1).getY() + height * buttons.size - borderWeight);
			border.draw(batch);
		}
		batch.end();
	}
	
	public boolean isSelected () {
		return isSelected;
	}
	
	public int getIdItem () {
		return idItem;
	}
	
	public void setAlpha (float alpha) {
		this.alpha = alpha;
	}
	
	public void setHidden (boolean hidden) {
		isHide = hidden;
	}
	
	public String getTextItem () {
		return buttons.get(getIdItem()).getText();
	}
	
	public void resetSelected () {
		isSelected = false;
	}
	
	@Override
	public void dispose () {
		if (texture != null) texture.dispose();
		if (textureOver != null) textureOver.dispose();
		if (textureFocused != null) textureFocused.dispose();
		bitmapFont.dispose();
		border.getTexture().dispose();
	}
}