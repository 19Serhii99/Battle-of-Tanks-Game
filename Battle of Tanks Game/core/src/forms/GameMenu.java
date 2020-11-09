package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Events;
import com.mygdx.game.Loading;
import com.mygdx.game.Settings;

import gui.Button;
import gui.PopupMenu;
import gui.TextButton;
import net.Client;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class GameMenu implements Disposable {
	private Sprite background;
	private Sprite menuLine;
	private Loading loading;
	
	private Button homeButton;
	private TextButton startGameButton;
	private TextButton storeButton;
	private TextButton stockButton;
	private TextButton settingsButton;
	private TextButton accountButton;
	private Button offButton;
	private PopupMenu accountPopupMenu;
	
	private HomeForm homeForm;
	private BattleSettingsForm battleSettingsForm;
	private StoreForm storeForm;
	private StockForm stockForm;
	private ExitFromGameForm exitForm;
	private SettingsForm settingsForm;
	
	private boolean isAccount = true;
	private boolean isBattle = false;
	
	private boolean isHome = true;
	private boolean isStartGame = false;
	private boolean isStore = false;
	private boolean isStock = false;
	private boolean isSettings = false;
	
	public GameMenu () {
		initBackgrounds();
		initButtons();
		
		loading = new Loading();
		loading.setText("Загрузка меню...");
		loading.setAlpha(1.0f);
		
		homeForm = new HomeForm();
	}
	
	private void initBackgrounds () {
		Texture texture = new Texture(Gdx.files.internal("images/background.jpg"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(texture);
		background.setSize(CameraController.getInstance().getWidth(), CameraController.getInstance().getHeight());
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		texture = TextureCreator.createTexture(new Color(72f / 255f, 72f / 255f, 72f / 255f, 1.0f));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		menuLine = new Sprite(texture);
		menuLine.setSize(CameraController.getInstance().getWidth(), 50);
		menuLine.setPosition(background.getX(), CameraController.getInstance().getHeight() - menuLine.getHeight());
	}
	
	private void initButtons () {
		Texture texture = new Texture(Gdx.files.internal("images/menu/homeButton.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureFocused = new Texture(Gdx.files.internal("images/menu/homeButtonFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureOver = new Texture(Gdx.files.internal("images/menu/homeButtonOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		homeButton = new Button();
		homeButton.setTexture(texture);
		homeButton.setTextureFocused(textureFocused);
		homeButton.setTextureOver(textureOver);
		homeButton.setSize(40, menuLine.getHeight());
		
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		texture = new Texture(Gdx.files.internal("images/menu/menuBackground.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureOver = new Texture(Gdx.files.internal("images/menu/menuBackgroundOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureFocused = new Texture(Gdx.files.internal("images/menu/menuBackgroundFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		startGameButton = new TextButton("Почати гру", font);
		startGameButton.setTexture(texture);
		startGameButton.setTextureOver(textureOver);
		startGameButton.setTextureFocused(textureFocused);
		startGameButton.setSize(200, menuLine.getHeight());
		
		storeButton = new TextButton("Магазин", font);
		storeButton.setTexture(texture);
		storeButton.setTextureOver(textureOver);
		storeButton.setTextureFocused(textureFocused);
		storeButton.setSize(200, menuLine.getHeight());
		
		stockButton = new TextButton("Склад", font);
		stockButton.setTexture(texture);
		stockButton.setTextureOver(textureOver);
		stockButton.setTextureFocused(textureFocused);
		stockButton.setSize(200, menuLine.getHeight());
		
		settingsButton = new TextButton("Настройки", font);
		settingsButton.setTexture(texture);
		settingsButton.setTextureOver(textureOver);
		settingsButton.setTextureFocused(textureFocused);
		settingsButton.setSize(200, menuLine.getHeight());
		
		texture = new Texture(Gdx.files.internal("images/menu/menuNicknameBackground.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureOver = new Texture(Gdx.files.internal("images/menu/menuNicknameBackgroundOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureFocused = new Texture(Gdx.files.internal("images/menu/menuNicknameBackgroundFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		accountButton = new TextButton(Settings.getInstance().getName(), font);
		accountButton.setTexture(texture);
		accountButton.setTextureOver(textureOver);
		accountButton.setTextureFocused(textureFocused);
		accountButton.setSize(250, menuLine.getHeight());
		
		texture = new Texture(Gdx.files.internal("images/menu/menuOffButton.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureOver = new Texture(Gdx.files.internal("images/menu/menuOffButtonOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureFocused = new Texture(Gdx.files.internal("images/menu/menuOffButtonFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		offButton = new Button();
		offButton.setTexture(texture);
		offButton.setTextureOver(textureOver);
		offButton.setTextureFocused(textureFocused);
		offButton.setSize(50, menuLine.getHeight());
		
		float totalWidth = homeButton.getWidth() + startGameButton.getWidth() + storeButton.getWidth() + stockButton.getWidth() + settingsButton.getWidth() + accountButton.getWidth() + offButton.getWidth();
		float startX = CameraController.getInstance().getWidth() / 2 - totalWidth / 2;
		
		homeButton.setPosition(startX, menuLine.getY());
		startGameButton.setPosition(homeButton.getX() + homeButton.getWidth() + 3, menuLine.getY());
		storeButton.setPosition(startGameButton.getX() + startGameButton.getWidth() - 1, menuLine.getY());
		stockButton.setPosition(storeButton.getX() + storeButton.getWidth() - 1, menuLine.getY());
		settingsButton.setPosition(stockButton.getX() + stockButton.getWidth() - 1, menuLine.getY());
		accountButton.setPosition(settingsButton.getX() + settingsButton.getWidth() - 1 , menuLine.getY());
		offButton.setPosition(accountButton.getX() + accountButton.getWidth() + 3, menuLine.getY());
	}
	
	private void initAccountPopupMenu () {
		Texture texture = TextureCreator.createTexture(new Color(72f / 255f, 72f / 255f, 72f / 255f, 1.0f));
		Texture textureOver = TextureCreator.createTexture(new Color(98f / 255f, 98f / 255f, 98f / 255f, 1.0f));
		Texture textureFocused = TextureCreator.createTexture(new Color(233f / 255f, 134f / 255f, 37f / 255f, 1.0f));
		
		accountPopupMenu = new PopupMenu(Color.WHITE, Color.BLACK, 20, texture, textureOver, textureFocused,
				accountButton.getX() + accountButton.getWidth() / 2 - (accountButton.getWidth() + 50) / 2, accountButton.getY(), accountButton.getWidth() + 50, 50);
		accountPopupMenu.addItem("Вийти з облікового запису");
		accountPopupMenu.setBorder(5, Color.ORANGE);
	}
	
	public void show (SpriteBatch batch) {
		batch.begin();
		
		background.draw(batch);
		menuLine.draw(batch);
		homeButton.show(batch);
		startGameButton.show(batch);
		storeButton.show(batch);
		stockButton.show(batch);
		settingsButton.show(batch);
		accountButton.show(batch);
		offButton.show(batch);
		
		batch.end();
		
		if (isHome) {
			homeForm.show(batch);
		} else if (isStartGame) {
			battleSettingsForm.show(batch);
			if (battleSettingsForm.isStart()) {
				isBattle = true;
			}
		} else if (isStore) {
			storeForm.show(batch);
			if (storeForm.isRestart()) {
				storeForm.dispose();
				storeForm = new StoreForm();
			}
		} else if (isStock) {
			stockForm.show(batch);
			if (stockForm.isRestart()) {
				stockForm.dispose();
				stockForm = new StockForm();
			}
		} else if (isSettings) {
			settingsForm.show(batch);
		}
		
		handleMenuButtons(batch);
		
		if (exitForm != null) {
			exitForm.show(batch);
			if (exitForm.isCancel()) {
				exitForm.hide();
				if (exitForm.getAlpha() == 0.0f) {
					exitForm.dispose();
					exitForm = null;	
				}
			} else if (exitForm.isExit()) {
				exitForm.dispose();
				Gdx.app.exit();
			}
		}
		
		if (loading != null) {
			loading.show(batch);
			if (homeForm.isLoaded()) {
				loading.hide();
				if (loading.getAlpha() == 0.0f) {
					loading.dispose();
					loading = null;
				}
			}
		}
	}
	
	private void handleMenuButtons (SpriteBatch batch) {
		if (accountPopupMenu != null) {
			accountPopupMenu.show(batch);
			if (accountPopupMenu.isSelected()) {
				if (accountPopupMenu.getIdItem() == 0) {
					isAccount = false;
					Client.getInstance().exit(0);
				}
				accountPopupMenu.dispose();
				accountPopupMenu = null;
			} else {
				if (Events.getInstance().isMouseLeftReleased()) {
					accountPopupMenu.dispose();
					accountPopupMenu = null;
				}
			}
		}
		
		if (startGameButton.isReleased()) {
			isStartGame = true;
			if (isHome) {
				isHome = false;
				homeForm.dispose();
				homeForm = null;
			} else if (isStore) {
				storeForm.dispose();
				storeForm = null;
				isStore = false;
			} else if (isStock) {
				stockForm.dispose();
				stockForm = null;
				isStock = false;
			} else if (isSettings) {
				settingsForm.dispose();
				settingsForm = null;
				isSettings = false;
			}
			battleSettingsForm = new BattleSettingsForm(false);
		}
		
		if (accountButton.isReleased()) {
			if (accountPopupMenu != null) {
				accountPopupMenu.dispose();
			}
			initAccountPopupMenu();
		}
		
		if (offButton.isReleased()) {
			if (exitForm == null) exitForm = new ExitFromGameForm();
		}
		
		if (homeButton.isReleased()) {
			isHome = true;
			if (homeForm == null) {
				homeForm = new HomeForm();
				if (isStartGame) {
					battleSettingsForm.dispose();
					battleSettingsForm = null;
					isStartGame = false;
				} else if (isStore) {
					storeForm.dispose();
					storeForm = null;
					isStore = false;
				} else if (isStock) {
					stockForm.dispose();
					stockForm = null;
					isStock = false;
				} else if (isSettings) {
					settingsForm.dispose();
					settingsForm = null;
					isSettings = false;
				}
			}
		} else if (storeButton.isReleased()) {
			isStore = true;
			if (storeForm == null) {
				if (isStartGame) {
					battleSettingsForm.dispose();
					battleSettingsForm = null;
					isStartGame = false;
				} else if (isHome) {
					homeForm.dispose();
					homeForm = null;
					isHome = false;
				} else if (isStock) {
					stockForm.dispose();
					stockForm = null;
					isStock = false;
				} else if (isSettings) {
					settingsForm.dispose();
					settingsForm = null;
					isSettings = false;
				}
				storeForm = new StoreForm();
			}
		} else if (stockButton.isReleased()) {
			isStock = true;
			if (stockForm == null) {
				if (isStartGame) {
					battleSettingsForm.dispose();
					battleSettingsForm = null;
					isStartGame = false;
				} else if (isHome) {
					homeForm.dispose();
					homeForm = null;
					isHome = false;
				} else if (isStore) {
					storeForm.dispose();
					storeForm = null;
					isStore = false;
				} else if (isSettings) {
					settingsForm.dispose();
					settingsForm = null;
					isSettings = false;
				}
				stockForm = new StockForm();
			}
		} else if (settingsButton.isReleased()) {
			isSettings = true;
			if (settingsForm == null) {
				if (isStartGame) {
					battleSettingsForm.dispose();
					battleSettingsForm = null;
					isStartGame = false;
				} else if (isHome) {
					homeForm.dispose();
					homeForm = null;
					isHome = false;
				} else if (isStore) {
					storeForm.dispose();
					storeForm = null;
					isStore = false;
				} else if (isStock) {
					stockForm.dispose();
					stockForm = null;
					isStock = false;
				}
				settingsForm = new SettingsForm();
			}
		}
	}
	
	public boolean isAccount () {
		return isAccount;
	}
	
	public boolean isBattle () {
		return isBattle;
	}
	
	public BattleSettingsForm getBattleSettingsForm () {
		return battleSettingsForm;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		menuLine.getTexture().dispose();
		homeButton.getTexture().dispose();
		homeButton.getTextureFocused().dispose();
		homeButton.getTextureOver().dispose();
		startGameButton.getTexture().dispose();
		startGameButton.getTextureFocused().dispose();
		startGameButton.getTextureOver().dispose();
		if (loading != null) loading.dispose();
		offButton.getTexture().dispose();
		offButton.getTextureFocused().dispose();
		offButton.getTextureOver().dispose();
		if (accountPopupMenu != null) accountPopupMenu.dispose();
		if (homeForm != null) homeForm.dispose();
		if (battleSettingsForm != null) battleSettingsForm.dispose();
		if (storeForm != null) storeForm.dispose();
		if (stockForm != null) stockForm.dispose();
		if (settingsForm != null) settingsForm.dispose();
	}
}