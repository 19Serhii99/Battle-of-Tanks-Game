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
import util.Font;
import util.TextureCreator;

public class TowerTape implements Disposable {
	private Sprite background;
	private Sprite border;
	private BitmapFont bitmapFont;
	private StoreForm storeForm;
	private StockForm stockForm;
	private Array <StoreTowerItem> items;
	
	private Texture storeItemTexture;
	private Texture storeItemOverTexture;
	private Texture storeItemFocusedTexture;
	
	public TowerTape (StoreBaseForm storeForm) {
		if (storeForm instanceof StoreForm) {
			this.storeForm = (StoreForm) storeForm;
		} else if (storeForm instanceof StockForm) {
			stockForm = (StockForm) storeForm;
		}
		
		initBackgrounds();
	}
	
	private void initBackgrounds () {
		Texture backgroundTexture = TextureCreator.createTexture(Color.BLACK);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture borderTexture = TextureCreator.createTexture(Color.GREEN);
		borderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(backgroundTexture);
		
		if (storeForm != null) {
			background.setSize(storeForm.getBackground().getWidth() - 400, 150);
			background.setPosition(storeForm.getBackground().getX() + storeForm.getBackground().getWidth() / 2 - background.getWidth() / 2, storeForm.getBackground().getY() + 20);
			background.setAlpha(0.6f);
		} else {
			background.setSize(stockForm.getBackground().getWidth() - 400, 150);
			background.setPosition(stockForm.getBackground().getX() + stockForm.getBackground().getWidth() / 2 - background.getWidth() / 2, stockForm.getBackground().getY() + 20);
			background.setAlpha(0.6f);
		}
		
		border = new Sprite(borderTexture);
	}
	
	public void initTape (Corps corps) {
		if (storeItemTexture == null && storeItemOverTexture == null && storeItemFocusedTexture == null && bitmapFont == null) {
			storeItemTexture = new Texture(Gdx.files.internal("images/menu/storeItem.png"));
			storeItemTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			storeItemOverTexture = new Texture(Gdx.files.internal("images/menu/storeItemOver.png"));
			storeItemOverTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			storeItemFocusedTexture = new Texture(Gdx.files.internal("images/menu/storeItemFocused.png"));
			storeItemFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
			bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);	
		}
		
		items = new Array <StoreTowerItem>();
		
		if (storeForm != null) {
			for (int i = 0; i <= corps.getTowers().size(); i++) {
				for (Tower tower : corps.getTowers()) {
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
		} else {
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
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
		
		border.setSize(background.getWidth(), 2);
		border.setPosition(background.getX(), background.getY());
		border.draw(batch);
		
		border.setSize(background.getWidth(), 2);
		border.setPosition(background.getX(), background.getY() + background.getHeight() - border.getHeight());
		border.draw(batch);
		
		border.setSize(2, background.getHeight());
		border.setPosition(background.getX(), background.getY());
		border.draw(batch);
		
		border.setSize(2, background.getHeight());
		border.setPosition(background.getX() + background.getWidth() - border.getWidth(), background.getY());
		border.draw(batch);
		
		for (StoreTowerItem item : items) {
			item.show(batch);
			if (item.isReleased()) {
				if (storeForm != null) {
					storeForm.initTowerValues(item.getTower());
					storeForm.getTowerBackground().setRegion(item.getTower().getWholeTexture());
					storeForm.getTowerBackground().setSize(item.getTower().getWidth(), item.getTower().getHeight());
					storeForm.getTowerBackground().setPosition(storeForm.getCorpsBackground().getX() + item.getTower().getX(), storeForm.getCorpsBackground().getY() + item.getTower().getY());
				} else {
					stockForm.initTowerValues(item.getTower());
					stockForm.getTowerBackground().setRegion(item.getTower().getWholeTexture());
					stockForm.getTowerBackground().setSize(item.getTower().getWidth(), item.getTower().getHeight());
					stockForm.getTowerBackground().setPosition(stockForm.getCorpsBackground().getX() + item.getTower().getX(), stockForm.getCorpsBackground().getY() + item.getTower().getY());
				}
			}
		}
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		border.getTexture().dispose();
		bitmapFont.dispose();
		storeItemTexture.dispose();
		storeItemOverTexture.dispose();
		storeItemFocusedTexture.dispose();
	}
}