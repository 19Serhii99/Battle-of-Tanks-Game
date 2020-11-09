package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import technique.Corps;
import technique.Tower;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class TowerTapeChoosing implements Disposable {
	private Sprite background;
	private Sprite border;
	private BitmapFont bitmapFont;
	private Array <StoreTowerItem> items;
	
	private Texture storeItemTexture;
	private Texture storeItemOverTexture;
	private Texture storeItemFocusedTexture;
	private Texture storeItemCheckedTexture;
	
	private StoreTowerItem currentItem;
	private boolean isChecked = false;
	private boolean isHide = false;
	private float alpha = 0.0f;
	
	public TowerTapeChoosing () {
		initBackgrounds();
	}
	
	private void initBackgrounds () {
		Texture backgroundTexture = TextureCreator.createTexture(Color.BLACK);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture borderTexture = TextureCreator.createTexture(Color.GREEN);
		borderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(backgroundTexture);
		background.setSize(800, 150);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() - 700);
		background.setAlpha(0.6f);
		
		border = new Sprite(borderTexture);
	}
	
	public void initTape (Corps corps) {
		if (storeItemTexture == null && storeItemOverTexture == null && storeItemFocusedTexture == null && storeItemCheckedTexture == null && bitmapFont == null) {
			storeItemTexture = new Texture(Gdx.files.internal("images/menu/storeItem.png"));
			storeItemTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			storeItemOverTexture = new Texture(Gdx.files.internal("images/menu/storeItemOver.png"));
			storeItemOverTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			storeItemFocusedTexture = new Texture(Gdx.files.internal("images/menu/storeItemFocused.png"));
			storeItemFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			storeItemCheckedTexture = new Texture(Gdx.files.internal("images/menu/storeItemChecked.png"));
			storeItemCheckedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
			bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);	
		}
		
		items = new Array <StoreTowerItem>();
		
		for (int i = 0; i <= corps.getTowers().size(); i++) {
			for (Tower tower : corps.getTowers()) {
				if (tower.isBought()) {
					if (tower.getPosition() == i) {
						StoreTowerItem storeItem = new StoreTowerItem(storeItemTexture, storeItemOverTexture, storeItemFocusedTexture, tower, bitmapFont);
						if (items.size == 0) {
							storeItem.setPosition(background.getX() + 15, background.getY() + background.getHeight() / 2 - storeItem.getHeight() / 2);
						} else {
							storeItem.setPosition(items.get(items.size - 1).getX() + items.get(items.size - 1).getWidth() + 10, items.get(items.size - 1).getY());
						}
						items.add(storeItem);
					}	
				}
			}
		}
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		isChecked = false;
		
		background.draw(batch);
		background.setAlpha(alpha);
		
		border.setSize(background.getWidth(), 2);
		border.setPosition(background.getX(), background.getY());
		border.setAlpha(alpha);
		border.draw(batch);
		
		border.setSize(background.getWidth(), 2);
		border.setPosition(background.getX(), background.getY() + background.getHeight() - border.getHeight());
		border.setAlpha(alpha);
		border.draw(batch);
		
		border.setSize(2, background.getHeight());
		border.setPosition(background.getX(), background.getY());
		border.setAlpha(alpha);
		border.draw(batch);
		
		border.setSize(2, background.getHeight());
		border.setPosition(background.getX() + background.getWidth() - border.getWidth(), background.getY());
		border.setAlpha(alpha);
		border.draw(batch);
		
		for (StoreTowerItem item : items) {
			item.show(batch);
			if (item.isReleased()) {
				if (currentItem != null) {
					currentItem.setTexture(storeItemTexture);
					currentItem.setTextureOver(storeItemOverTexture);
				}
				item.setTexture(storeItemCheckedTexture);
				item.setTextureOver(storeItemCheckedTexture);
				currentItem = item;
				isChecked = true;
			}
		}
	}
	
	public Tower getTower () {
		return currentItem == null ? null : currentItem.getTower();
	}
	
	public boolean isChecked () {
		return isChecked;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		border.getTexture().dispose();
		bitmapFont.dispose();
		storeItemTexture.dispose();
		storeItemOverTexture.dispose();
		storeItemFocusedTexture.dispose();
		storeItemCheckedTexture.dispose();
	}
}