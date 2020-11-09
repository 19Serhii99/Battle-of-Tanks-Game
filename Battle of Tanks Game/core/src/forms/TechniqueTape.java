package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Events;
import com.mygdx.game.Objects;

import gui.Button;
import technique.Corps;
import technique.Tower;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class TechniqueTape implements Disposable {
	private Sprite background;
	private Sprite border;
	private Array <StoreTechniqueItem> items;
	private BitmapFont bitmapFont;
	private StoreForm storeForm;
	private StockForm stockForm;
	private Button arrowLeft;
	private Button arrowRight;
	
	private Texture storeItemTexture;
	private Texture storeItemOverTexture;
	private Texture storeItemFocusedTexture;
	
	private Rectangle scissors = new Rectangle();
	private Rectangle area;
	
	private boolean isLeft = false;
	private boolean isRight = false;
	private boolean isTapeFocused = false;
	
	private Vector3 cursorFocused;
	
	public TechniqueTape (StoreBaseForm storeForm) {
		if (storeForm instanceof StoreForm) {
			this.storeForm = (StoreForm) storeForm;
		} else if (storeForm instanceof StockForm) {
			stockForm = (StockForm) storeForm;
		}
		
		initBackgrounds();
		initTechniques();
		initTape();
		
		if (this.storeForm != null) {
			for (Corps corps : Objects.getInstance().getCorpses()) {
				if (corps.getPosition() == 0) {
					this.storeForm.initCorpsValues(corps);
					this.storeForm.initTowerValues(corps.getTowers().get(0));
					this.storeForm.getCorpsBackground().setRegion(corps.getWholeTexture());
					this.storeForm.getTowerBackground().setRegion(corps.getTowers().get(0).getWholeTexture());
					this.storeForm.getCorpsBackground().setSize(corps.getWidth(), corps.getHeight());
					this.storeForm.getCorpsBackground().setPosition(this.storeForm.getBackground().getX() + this.storeForm.getBackground().getWidth() / 2 - corps.getWidth() / 2,
							this.storeForm.getBackground().getY() + this.storeForm.getBackground().getHeight() / 2 + 100);
					this.storeForm.getTowerBackground().setSize(corps.getTowers().get(0).getWidth(), corps.getTowers().get(0).getHeight());
					this.storeForm.getTowerBackground().setPosition(this.storeForm.getCorpsBackground().getX() + corps.getTowers().get(0).getX(),
							this.storeForm.getCorpsBackground().getY() + corps.getTowers().get(0).getY());
					this.storeForm.getTowerTape().initTape(corps);
					break;
				}
			}
		} else if (stockForm != null) {
			for (int i = 0; i < Objects.getInstance().getCorpses().size; i++) {
				for (Corps corps : Objects.getInstance().getCorpses()) {
					if (corps.isBought()) {
						if (corps.getPosition() == i) {
							stockForm.initCorpsValues(corps);
							stockForm.initTowerValues(corps.getTowers().get(0));
							stockForm.getCorpsBackground().setRegion(corps.getWholeTexture());
							stockForm.getTowerBackground().setRegion(corps.getTowers().get(0).getWholeTexture());
							stockForm.getCorpsBackground().setSize(corps.getWidth(), corps.getHeight());
							stockForm.getCorpsBackground().setPosition(stockForm.getBackground().getX() + stockForm.getBackground().getWidth() / 2 - corps.getWidth() / 2,
									stockForm.getBackground().getY() + stockForm.getBackground().getHeight() / 2 + 100);
							stockForm.getTowerBackground().setSize(corps.getTowers().get(0).getWidth(), corps.getTowers().get(0).getHeight());
							stockForm.getTowerBackground().setPosition(stockForm.getCorpsBackground().getX() + corps.getTowers().get(0).getX(),
									stockForm.getCorpsBackground().getY() + corps.getTowers().get(0).getY());
							stockForm.getTowerTape().initTape(corps);
							break;
						}
					}
				}
			}	
		}
		
		initButtons();
	}
	
	private void initBackgrounds () {
		Texture backgroundTexture = TextureCreator.createTexture(Color.BLACK);
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture borderTexture = TextureCreator.createTexture(Color.ORANGE);
		borderTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(backgroundTexture);
		background.setSize(1700, 250);
		background.setPosition(100, 30);
		background.setAlpha(0.6f);
		
		border = new Sprite(borderTexture);
	}
	
	private void initTechniques () {
		if (storeForm != null) {
			for (int i = 0; i < Objects.getInstance().getCorpses().size; i++) {
				Corps corps = Objects.getInstance().getCorpses().get(i);
				Texture wholeTexture = new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/corps.png"));
				corps.setWholeTexture(wholeTexture);
				corps.getBackground().setRegion(wholeTexture);
				corps.getBackground().setSize(corps.getWidth(), corps.getHeight());
				for (int j = 0; j < corps.getTowers().size(); j++) {
					Tower tower = corps.getTowers().get(j);
					Texture towerTexture = new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/towers/" + (tower.getPosition() + 1) + "/tower.png"));
					tower.setWholeTexture(towerTexture);
					tower.getBackground().setRegion(towerTexture);
					tower.getBackground().setSize(tower.getWidth(), tower.getHeight());
				}
			}	
		} else {
			for (int i = 0; i < Objects.getInstance().getCorpses().size; i++) {
				Corps corps = Objects.getInstance().getCorpses().get(i);
				if (corps.isBought()) {
					Texture wholeTexture = new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/corps.png"));
					corps.setWholeTexture(wholeTexture);
					corps.getBackground().setRegion(wholeTexture);
					corps.getBackground().setSize(corps.getWidth(), corps.getHeight());
					for (int j = 0; j < corps.getTowers().size(); j++) {
						Tower tower = corps.getTowers().get(j);
						if (tower.isBought()) {
							Texture towerTexture = new Texture(Gdx.files.internal("models/" + corps.getName() + "/images/towers/" + (tower.getPosition() + 1) + "/tower.png"));
							tower.setWholeTexture(towerTexture);
							tower.getBackground().setRegion(towerTexture);
							tower.getBackground().setSize(tower.getWidth(), tower.getHeight());	
						}
					}
				}
			}
		}
	}
	
	private void initTape () {
		storeItemTexture = new Texture(Gdx.files.internal("images/menu/storeItem.png"));
		storeItemTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		storeItemOverTexture = new Texture(Gdx.files.internal("images/menu/storeItemOver.png"));
		storeItemOverTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		storeItemFocusedTexture = new Texture(Gdx.files.internal("images/menu/storeItemFocused.png"));
		storeItemFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		items = new Array <StoreTechniqueItem>();
		
		if (storeForm != null) {
			for (int i = 0; i <= Objects.getInstance().getCorpses().size; i++) {
				for (Corps corps : Objects.getInstance().getCorpses()) {
					if (corps.getPosition() == i) {
						StoreTechniqueItem storeItem = new StoreTechniqueItem(storeItemTexture, storeItemOverTexture, storeItemFocusedTexture, corps, bitmapFont);
						if (items.size == 0) {
							storeItem.setPosition(background.getX() + 70, background.getY() + background.getHeight() / 2 - storeItem.getHeight() / 2);
						} else {
							storeItem.setPosition(items.get(items.size - 1).getX() + items.get(items.size - 1).getWidth()  + 10, items.get(items.size - 1).getY());
						}
						items.add(storeItem);
					}
				}
			}
		} else {
			for (int i = 0; i <= Objects.getInstance().getCorpses().size; i++) {
				for (Corps corps : Objects.getInstance().getCorpses()) {
					if (corps.isBought()) {
						if (corps.getPosition() == i) {
							StoreTechniqueItem storeItem = new StoreTechniqueItem(storeItemTexture, storeItemOverTexture, storeItemFocusedTexture, corps, bitmapFont);
							if (items.size == 0) {
								storeItem.setPosition(background.getX() + 70, background.getY() + background.getHeight() / 2 - storeItem.getHeight() / 2);
							} else {
								storeItem.setPosition(items.get(items.size - 1).getX() + items.get(items.size - 1).getWidth()  + 10, items.get(items.size - 1).getY());
							}
							items.add(storeItem);
						}
					}
				}
			}
		}
	}
	
	private void initButtons () {
		Texture texture = new Texture(Gdx.files.internal("images/menu/arrow.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureOver = new Texture(Gdx.files.internal("images/menu/arrowOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureFocused = new Texture(Gdx.files.internal("images/menu/arrowFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		arrowLeft = new Button();
		arrowLeft.setTexture(texture);
		arrowLeft.setTextureOver(textureOver);
		arrowLeft.setTextureFocused(textureFocused);
		arrowLeft.setSize(60, 240);
		arrowLeft.setPosition(background.getX() + 5, background.getY() + 5);
		
		arrowRight = new Button();
		arrowRight.setTexture(texture);
		arrowRight.setTextureOver(textureOver);
		arrowRight.setTextureFocused(textureFocused);
		arrowRight.setSize(60, 240);
		arrowRight.setPosition(background.getX() + background.getWidth() - arrowRight.getWidth() - 5, background.getY() + 5);
		arrowRight.getSprite().setOriginCenter();
		arrowRight.getSprite().rotate(180);
		
		area = new Rectangle(arrowLeft.getX() + arrowLeft.getWidth(), arrowLeft.getY(), background.getWidth() - 140, 250);
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
		
		border.setSize(background.getWidth(), 5);
		border.setPosition(background.getX(), background.getY());
		border.draw(batch);
		
		border.setSize(background.getWidth(), 5);
		border.setPosition(background.getX(), background.getY() + background.getHeight() - border.getHeight());
		border.draw(batch);
		
		border.setSize(5, background.getHeight());
		border.setPosition(background.getX(), background.getY());
		border.draw(batch);
		
		border.setSize(5, background.getHeight());
		border.setPosition(background.getX() + background.getWidth() - border.getWidth(), background.getY());
		border.draw(batch);
		
		arrowLeft.show(batch);
		arrowRight.show(batch);
		
		if (arrowLeft.isPressed()) {
			isLeft = true;
			for (StoreTechniqueItem item : items) {
				item.setDisable(true);
			}
		} else if (arrowRight.isPressed()) {
			isRight = true;
			for (StoreTechniqueItem item : items) {
				item.setDisable(true);
			}
		} else if (Events.getInstance().isMouseRightPressed()) {
			if (items.size > 6) {
				Vector3 cursor = CameraController.getInstance().unproject();
				if (cursor.x >= arrowLeft.getX() + arrowLeft.getWidth() && cursor.x <= arrowRight.getX() && cursor.y >= arrowLeft.getY() && cursor.y <= arrowLeft.getY() + arrowLeft.getHeight()) {
					isTapeFocused = true;
					cursorFocused = new Vector3(cursor);
				}	
			}
		}
		
		if (isLeft) {
			if (items.get(items.size - 1).getX() > background.getVertices()[Batch.X3] - items.get(items.size - 1).getWidth()  - 75) {
				for (StoreTechniqueItem item : items) {
					item.setPosition(item.getX() - 250 * Gdx.graphics.getDeltaTime(), item.getY());
				}	
			}
		} else if (isRight) {
			if (items.get(0).getX() < background.getX() + 70) {
				for (StoreTechniqueItem item : items) {
					item.setPosition(item.getX() + 250 * Gdx.graphics.getDeltaTime(), item.getY());
				}	
			}
		} else if (Events.getInstance().isScrolled()) {
			Vector3 cursor = CameraController.getInstance().unproject();
			if (cursor.x >= background.getX() && cursor.x <= background.getX() + background.getWidth() && cursor.y >= background.getY() && cursor.y <= background.getY() + background.getHeight()) {
				if (Events.getInstance().getAmountScrolled() == -1) {
					if (items.get(items.size - 1).getX() > background.getVertices()[Batch.X3] - items.get(items.size - 1).getWidth()  - 75) {
						for (StoreTechniqueItem item : items) {
							item.setPosition(item.getX() - 1000 * Gdx.graphics.getDeltaTime(), item.getY());
						}	
					}
				} else {
					if (items.get(0).getX() < background.getX() + 70) {
						for (StoreTechniqueItem item : items) {
							item.setPosition(item.getX() + 1000 * Gdx.graphics.getDeltaTime(), item.getY());
						}	
					}
				}
			}
		} else if (isTapeFocused) {
			Vector3 cursor = CameraController.getInstance().unproject();
			float x = cursor.x - cursorFocused.x;
			StoreTechniqueItem item1 = items.get(items.size - 1);
			StoreTechniqueItem item2 = items.get(0);
			float tempX1 = item1.getX() + x;
			float tempX2 = item2.getX() + x;
			if (tempX1 > background.getVertices()[Batch.X3] - item1.getWidth()  - 75 && tempX2 < background.getX() + 70) {
				for (StoreTechniqueItem item : items) {
					item.setPosition(item.getX() + x, item.getY());
				}
			} else {
				if (tempX1 > background.getVertices()[Batch.X3] - item1.getWidth()  - 75) {
					x = background.getX() + 70 - item2.getX();
				} else if (tempX2 < background.getX() + 70) {
					x = background.getVertices()[Batch.X3] - item1.getWidth()  - 75 - item1.getX();
				}
				for (StoreTechniqueItem item : items) {
					item.setPosition(item.getX() + x, item.getY());
				}
			}
			cursorFocused = new Vector3(cursor);
		}
		
		if (Events.getInstance().isMouseLeftReleased()) {
			isLeft = isRight = false;
			for (StoreTechniqueItem item : items) {
				item.setDisable(false);
			}
		}
		if (Events.getInstance().isMouseRightReleased()) {
			isTapeFocused = false;
		}
		
		batch.flush();
		
		ScissorStack.calculateScissors(CameraController.getInstance().getCamera(), batch.getTransformMatrix(), area, scissors);
		ScissorStack.pushScissors(scissors);
		
		boolean disabled = false;
		
		Vector3 cursor = CameraController.getInstance().unproject();
		if (cursor.x >= arrowLeft.getX() + arrowLeft.getWidth() && cursor.x <= arrowRight.getX() && cursor.y >= arrowLeft.getY() && cursor.y <= arrowLeft.getY() + arrowLeft.getHeight()) {
			if (!isLeft && !isRight && !isTapeFocused) {
				disabled = false;
			}
		} else {
			if (!isLeft && !isRight && !isTapeFocused) {
				disabled = true;
			}
		}
		
		for (StoreTechniqueItem item : items) {
			item.show(batch);
			item.setDisable(disabled);
			if (item.isReleased()) {
				if (storeForm != null) {
					storeForm.initCorpsValues(item.getCorps());
					storeForm.initTowerValues(item.getCorps().getTowers().get(0));
					storeForm.getCorpsBackground().setRegion(item.getCorps().getWholeTexture());
					storeForm.getCorpsBackground().setSize(item.getCorps().getWidth(), item.getCorps().getHeight());
					storeForm.getTowerBackground().setRegion(item.getCorps().getTowers().get(0).getWholeTexture());
					storeForm.getTowerBackground().setSize(item.getCorps().getTowers().get(0).getWidth(), item.getCorps().getTowers().get(0).getHeight());
					storeForm.getCorpsBackground().setPosition(storeForm.getBackground().getX() + storeForm.getBackground().getWidth() / 2 - storeForm.getCorpsBackground().getWidth() / 2,
							storeForm.getBackground().getY() + storeForm.getBackground().getHeight() / 2 + 100);
					storeForm.getTowerBackground().setPosition(storeForm.getCorpsBackground().getX() + item.getCorps().getTowers().get(0).getX(),
							storeForm.getCorpsBackground().getY() + item.getCorps().getTowers().get(0).getY());
					storeForm.getTowerTape().initTape(item.getCorps());	
				} else {
					stockForm.initCorpsValues(item.getCorps());
					Tower currentTower = null;
					block : {
						for (int i = 0; i < item.getCorps().getTowers().size(); i++) {
							for (Tower tower : item.getCorps().getTowers()) {
								if (tower.isBought()) {
									if (tower.getPosition() == i) {
										stockForm.initTowerValues(tower);
										currentTower = tower;
										break block;
									}
								}
							}
						}
					}
					stockForm.getCorpsBackground().setRegion(item.getCorps().getWholeTexture());
					stockForm.getCorpsBackground().setSize(item.getCorps().getWidth(), item.getCorps().getHeight());
					stockForm.getTowerBackground().setRegion(currentTower.getWholeTexture());
					stockForm.getTowerBackground().setSize(currentTower.getWidth(), currentTower.getHeight());
					stockForm.getCorpsBackground().setPosition(stockForm.getBackground().getX() + stockForm.getBackground().getWidth() / 2 - stockForm.getCorpsBackground().getWidth() / 2,
							stockForm.getBackground().getY() + stockForm.getBackground().getHeight() / 2 + 100);
					stockForm.getTowerBackground().setPosition(stockForm.getCorpsBackground().getX() + currentTower.getX(),
							stockForm.getCorpsBackground().getY() + currentTower.getY());
					stockForm.getTowerTape().initTape(item.getCorps());	
				}
			}
		}
		batch.flush();
		ScissorStack.popScissors();
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		border.getTexture().dispose();
		storeItemTexture.dispose();
		storeItemOverTexture.dispose();
		storeItemFocusedTexture.dispose();
		bitmapFont.dispose();
		arrowLeft.getTexture().dispose();
		arrowLeft.getTextureFocused().dispose();
		arrowLeft.getTextureOver().dispose();
	}
}