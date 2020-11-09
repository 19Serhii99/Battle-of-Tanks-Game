package gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import util.Font;
import util.TextureCreator;

public class SelectBox extends Button implements Disposable {
	private Label label;
	private PopupMenu menu;
	
	private boolean isSelecting = false;
	
	public SelectBox (float x, float y, float width, float height) {
		Texture texture = TextureCreator.createTexture(new Color(72f / 255f, 72f / 255f, 72f / 255f, 1.0f));
		Texture textureOver = TextureCreator.createTexture(new Color(98f / 255f, 98f / 255f, 98f / 255f, 1.0f));
		Texture textureFocused = TextureCreator.createTexture(new Color(233f / 255f, 134f / 255f, 37f / 255f, 1.0f));
		
		menu = new PopupMenu(Color.WHITE, Color.BLACK, 20, texture, textureOver, textureFocused, x, y, width, height);
		menu.setBorder(1, Color.ORANGE);
		
		label = new Label("", Font.getInstance().generateBitmapFont(Color.WHITE, 20));
	}
	
	private void setLabel (String text) {
		label.setText(text);
		label.setPosition(super.getX() + super.getWidth() / 2 - label.getWidth() / 2, super.getY() + super.getHeight() / 2 + label.getHeight() / 2);
	}
	
	public void show (SpriteBatch batch) {
		batch.begin();
		super.show(batch);
		label.draw(batch);
		batch.end();
		
		if (super.isReleased()) {
			isSelecting = isSelecting ? false : true;
		}
		
		if (isSelecting) {
			menu.show(batch);
			if (menu.isSelected()) {
				isSelecting = false;
				setLabel(menu.getTextItem());
				menu.resetSelected();
			}
		} else {
			menu.setAlpha(0.0f);
		}
	}
	
	public void addItem (String item) {
		menu.addItem(item);
		setLabel(item);
	}
	
	public PopupMenu getPopupMenu () {
		return menu;
	}
	
	public boolean isSelecting () {
		return isSelecting;
	}

	@Override
	public void dispose () {
		label.getFont().dispose();
	}
}