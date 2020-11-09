package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Objects;

import gui.CheckBox;
import gui.Label;
import gui.RadioButtonGroup;
import gui.SelectBox;
import gui.TextButton;
import technique.Corps;
import technique.Tower;
import util.CameraController;
import util.Font;

public class BattleSettingsForm implements Disposable {
	private Sprite background;
	private Label caption;
	private Label battleTypeLabel;
	private RadioButtonGroup battleType;
	private TextButton startButton;
	private SelectBox mapBox;
	private Label mapLabel;
	
	private Texture texture;
	private Texture textureOver;
	private Texture textureFocused;
	private Texture textureChecked;
	private BitmapFont checkBoxFont;
	
	private TechniqueTapeChoosing techniqueTypeChoosing;
	private TowerTapeChoosing towerTapeChoosing;
	
	private boolean isHide = false;
	private float alpha = 0.0f;
	
	private boolean isStart = false;
	private boolean isMap;
	
	public BattleSettingsForm (boolean isMap) {
		initBackground();
		initLabels();
		initRadioButtons();
		initButtons();
		
		this.isMap = isMap;
		
		techniqueTypeChoosing = new TechniqueTapeChoosing();
		
		initSelectBox();
	}
	
	private void initBackground () {
		Texture texture = new Texture(Gdx.files.internal("images/primary forms/signInForm.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(texture);
		background.setSize(1900, 900);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
	}
	
	private void initLabels () {
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 40);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Настройки бою", font);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 15);
		
		font = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		battleTypeLabel = new Label("Режим бою", font);
		battleTypeLabel.setPosition(background.getX() + background.getWidth() / 2 - battleTypeLabel.getWidth() / 2, background.getVertices()[Batch.Y2] - 80);
		
		mapLabel = new Label("Мапа: ", font);
		mapLabel.setPosition(100, CameraController.getInstance().getHeight() - 560);
	}
	
	private void initButtons () {
		startButton = new TextButton("Розпочати бій!", Objects.getInstance().getButtonBitmapFont());
		startButton.setTexture(Objects.getInstance().getButtonTexture());
		startButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		startButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		startButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		startButton.setSize(300, 75);
		startButton.setPosition(background.getX() + background.getWidth() / 2 - startButton.getWidth() / 2, background.getY() + 30);
	}
	
	private void initRadioButtons () {
		checkBoxFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		checkBoxFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		texture = new Texture(Gdx.files.internal("images/gui/radioButton.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureOver = new Texture(Gdx.files.internal("images/gui/radioButtonOver.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureFocused = new Texture(Gdx.files.internal("images/gui/radioButtonFocused.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureChecked = new Texture(Gdx.files.internal("images/gui/radioButtonChecked.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		CheckBox command = new CheckBox("командний", checkBoxFont);
		command.setTexture(texture);
		command.setTextureOver(textureOver);
		command.setTextureFocused(textureFocused);
		command.setIconSize(30, 30);
		command.setPosition(background.getX() + 630, battleTypeLabel.getY() - 70);
		command.setCheckMark(textureChecked, 8);
		
		CheckBox assault = new CheckBox("штурм", checkBoxFont);
		assault.setTexture(texture);
		assault.setTextureOver(textureOver);
		assault.setTextureFocused(textureFocused);
		assault.setIconSize(30, 30);
		assault.setPosition(command.getX() + command.getWidth() + 50, battleTypeLabel.getY() - 70);
		assault.setCheckMark(textureChecked, 8);
		
		CheckBox deathmatch = new CheckBox("бій на смерть", checkBoxFont);
		deathmatch.setTexture(texture);
		deathmatch.setTextureOver(textureOver);
		deathmatch.setTextureFocused(textureFocused);
		deathmatch.setIconSize(30, 30);
		deathmatch.setPosition(assault.getX() + assault.getWidth() + 50, battleTypeLabel.getY() - 70);
		deathmatch.setCheckMark(textureChecked, 8);
		
		battleType = new RadioButtonGroup();
		battleType.addItem(command);
		//battleType.addItem(assault);
		battleType.addItem(deathmatch);
	}
	
	private void initSelectBox () {
		float width = 300;
		float height = 50;
		float x = 200;
		
		mapBox = new SelectBox(x, CameraController.getInstance().getHeight() - 600, width, height);
		mapBox.setPosition(x, CameraController.getInstance().getHeight() - 600);
		mapBox.setSize(width, height);
		
		FileHandle file = Gdx.files.internal("maps");
		for (FileHandle map : file.list()) {
			mapBox.addItem(map.nameWithoutExtension());
		}
		
		mapBox.setTexture(Objects.getInstance().getButtonTexture());
		mapBox.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		mapBox.setTextureOver(Objects.getInstance().getButtonOverTexture());
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		background.setAlpha(alpha);
		caption.setAlphas(alpha);
		battleTypeLabel.setAlphas(alpha);
		startButton.setAlpha(alpha);
		battleType.setAlpha(alpha);
		
		batch.begin();
		background.draw(batch);
		caption.draw(batch);
		startButton.show(batch);
		
		if (isMap) {
			mapLabel.draw(batch);
		}
		
		techniqueTypeChoosing.show(batch);
		
		if (techniqueTypeChoosing.isChecked()) {
			towerTapeChoosing = new TowerTapeChoosing();
			towerTapeChoosing.initTape(techniqueTypeChoosing.getCorps());
		}
		
		if (towerTapeChoosing != null) towerTapeChoosing.show(batch);
		
		batch.end();
		
		if (isMap) {
			mapBox.show(batch);	
		}
		
		if (startButton.isReleased()) {
			if (techniqueTypeChoosing.getCorps() != null && towerTapeChoosing != null) {
				if (towerTapeChoosing.getTower() != null) {
					isStart = true;
				}
			}
		}
	}
	
	public Corps getCorps () {
		return techniqueTypeChoosing.getCorps();
	}
	
	public Tower getTower () {
		return towerTapeChoosing.getTower();
	}
	
	public String getMap () {
		return mapBox.getPopupMenu().getTextItem();
	}
	
	public boolean isStart () {
		return isStart;
	}
	
	public boolean isMap () {
		return isMap;
	}
	
	public RadioButtonGroup getBattleType () {
		return battleType;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		battleTypeLabel.getFont().dispose();
		caption.getFont().dispose();
		texture.dispose();
		textureOver.dispose();
		textureFocused.dispose();
		textureChecked.dispose();
		checkBoxFont.dispose();
		techniqueTypeChoosing.dispose();
		if (towerTapeChoosing != null) towerTapeChoosing.dispose();
		for (Corps corps : Objects.getInstance().getCorpses()) {
			for (Tower tower : corps.getTowers()) {
				if (tower.getWholeTexture() != null) {
					tower.getWholeTexture().dispose();
					tower.setWholeTexture(null);
				}
			}
			if (corps.getWholeTexture() != null) {
				corps.getWholeTexture().dispose();
				corps.setWholeTexture(null);
			}
		}
		mapBox.dispose();
		mapBox.getPopupMenu().dispose();
	}
}