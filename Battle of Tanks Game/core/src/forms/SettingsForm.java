package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Objects;
import com.mygdx.game.Settings;

import gui.CheckBox;
import gui.Label;
import gui.Scale;
import gui.SelectBox;
import gui.TextButton;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class SettingsForm implements Disposable {
	private Sprite background;
	private Sprite border1;
	private Sprite border2;
	
	private Label caption;
	private Label resolutionLabel;
	private Label screenFormatLabel;
	private Label generalVolumeLabel;
	private Label interfaceVolumeLabel;
	private Label effectsVolumeLabel;
	private Label techniqueVolumeLabel;
	private Label environmentVolumeLabel;
	
	private TextButton video;
	private TextButton sound;
	private TextButton management;
	private TextButton apply;
	private TextButton reset;
	
	private CheckBox vSync;
	private CheckBox frames;
	private CheckBox fullscreen;
	private CheckBox turnOnSound;
	private CheckBox turnOffWhenHidden;
	
	private SelectBox resolutionBox;
	private SelectBox screenFormatBox;
	
	private Scale generalVolume;
	private Scale interfaceVolume;
	private Scale effectsVolume;
	private Scale techniqueVolume;
	private Scale environmentVolume;
	
	private boolean isHide;
	private boolean isVideo = true;
	private boolean isSound;
	private float alpha = 0.0f;
	
	private Sprite managementTable;
	
	public SettingsForm () {
		initBackgrounds();
		initLabels();
		initButtons();
		initCheckBoxes();
		initSelectBoxes();
		initScales();
	}
	
	private void initBackgrounds () {
		background = new Sprite(TextureCreator.createTexture(Color.BLACK));
		background.setSize(1600, 900);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		border1 = new Sprite(TextureCreator.createTexture(Color.GRAY));
		
		border2 = new Sprite(TextureCreator.createTexture(Color.YELLOW));
		
		managementTable = new Sprite(new Texture(Gdx.files.internal("images/management.png")));
		managementTable.setSize(774, 325);
		managementTable.setPosition(background.getX() + 200, background.getVertices()[Batch.Y2] - 550);
	}
	
	private void initLabels () {
		caption = new Label("Настройки", Font.getInstance().generateBitmapFont(Color.WHITE, 30));
		caption.setPosition(background.getX() + 10, background.getVertices()[Batch.Y2] - 10);
		
		screenFormatLabel = new Label("Формат екрану", Font.getInstance().generateBitmapFont(Color.WHITE, 20));
		screenFormatLabel.setPosition(background.getX() + 700, background.getVertices()[Batch.Y2] - 300);
		
		resolutionLabel = new Label("Роздільна здатність екрану", screenFormatLabel.getFont());
		resolutionLabel.setPosition(background.getX() + 700, background.getVertices()[Batch.Y2] - 400);
		
		generalVolumeLabel = new Label("Загальна гучність", resolutionLabel.getFont());
		generalVolumeLabel.setPosition(background.getX() + 650, background.getVertices()[Batch.Y2] - 300 + generalVolumeLabel.getHeight());
		
		interfaceVolumeLabel = new Label("Інтерфейс", resolutionLabel.getFont());
		interfaceVolumeLabel.setPosition(background.getX() + 650, background.getVertices()[Batch.Y2] - 400 + interfaceVolumeLabel.getHeight());
		
		effectsVolumeLabel = new Label("Ефекти", resolutionLabel.getFont());
		effectsVolumeLabel.setPosition(background.getX() + 650, background.getVertices()[Batch.Y2] - 500 + effectsVolumeLabel.getHeight());
		
		techniqueVolumeLabel = new Label("Техніка", resolutionLabel.getFont());
		techniqueVolumeLabel.setPosition(background.getX() + 650, background.getVertices()[Batch.Y2] - 600 + techniqueVolumeLabel.getHeight());
		
		environmentVolumeLabel = new Label("Довкілля", resolutionLabel.getFont());
		environmentVolumeLabel.setPosition(background.getX() + 650, background.getVertices()[Batch.Y2] - 700 + environmentVolumeLabel.getHeight());
	}
	
	private void initButtons () {
		Texture texture = new Texture(Gdx.files.internal("images/menu/menuBackground.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureOver = new Texture(Gdx.files.internal("images/menu/menuBackgroundOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureFocused = new Texture(Gdx.files.internal("images/menu/menuBackgroundFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		video = new TextButton("Відео", Objects.getInstance().getButtonBitmapFont());
		video.setTexture(texture);
		video.setTextureFocused(textureFocused);
		video.setTextureOver(textureOver);
		video.setBitmapFontFocused(Objects.getInstance().getButtonBitmapFont());
		video.setSize(200, 50);
		video.setPosition(background.getX() + 100, background.getVertices()[Batch.Y2] - 150);
		
		sound = new TextButton("Звук", Objects.getInstance().getButtonBitmapFont());
		sound.setTexture(texture);
		sound.setTextureFocused(textureFocused);
		sound.setTextureOver(textureOver);
		sound.setBitmapFontFocused(Objects.getInstance().getButtonBitmapFont());
		sound.setSize(200, 50);
		sound.setPosition(video.getX() + video.getWidth() + 1, video.getY());
		
		management = new TextButton("Керування", Objects.getInstance().getButtonBitmapFont());
		management.setTexture(texture);
		management.setTextureFocused(textureFocused);
		management.setTextureOver(textureOver);
		management.setBitmapFontFocused(Objects.getInstance().getButtonBitmapFont());
		management.setSize(200, 50);
		management.setPosition(sound.getX() + sound.getWidth() + 1, video.getY());
		
		reset = new TextButton("Скинути", Objects.getInstance().getButtonBitmapFont());
		reset.setTexture(Objects.getInstance().getButtonTexture());
		reset.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		reset.setTextureOver(Objects.getInstance().getButtonOverTexture());
		reset.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		reset.setSize(200, 50);
		reset.setPosition(background.getX() + 500, background.getY() + 100);
		
		apply = new TextButton("Застосувати", Objects.getInstance().getButtonBitmapFont());
		apply.setTexture(Objects.getInstance().getButtonTexture());
		apply.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		apply.setTextureOver(Objects.getInstance().getButtonOverTexture());
		apply.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		apply.setSize(200, 50);
		apply.setPosition(background.getX() + 750, background.getY() + 100);
	}
	
	private void initCheckBoxes () {
		BitmapFont bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture texture = new Texture(Gdx.files.internal("images/gui/checkBox.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureOver = new Texture(Gdx.files.internal("images/gui/checkBoxOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureFocused = new Texture(Gdx.files.internal("images/gui/checkBox.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture checkMark = new Texture(Gdx.files.internal("images/gui/checkMark.png"));
		checkMark.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		vSync = new CheckBox("Вертикальна синхронізація", bitmapFont);
		vSync.setTexture(texture);
		vSync.setTextureFocused(textureFocused);
		vSync.setTextureOver(textureOver);
		vSync.setIconSize(20, 20);
		vSync.setPosition(background.getX() + 200, background.getVertices()[Batch.Y2] - 300);
		vSync.setCheckMark(checkMark, 3);
		vSync.setChecked(Settings.getInstance().isVSync());
		
		frames = new CheckBox("Обмеження кадрів", bitmapFont);
		frames.setTexture(texture);
		frames.setTextureFocused(textureFocused);
		frames.setTextureOver(textureOver);
		frames.setIconSize(20, 20);
		frames.setPosition(background.getX() + 200, background.getVertices()[Batch.Y2] - 400);
		frames.setCheckMark(checkMark, 3);
		frames.setChecked(Settings.getInstance().isFrameLimitation());
		
		fullscreen = new CheckBox("Повноекранний режим", bitmapFont);
		fullscreen.setTexture(texture);
		fullscreen.setTextureFocused(textureFocused);
		fullscreen.setTextureOver(textureOver);
		fullscreen.setIconSize(20, 20);
		fullscreen.setPosition(background.getX() + 200, background.getVertices()[Batch.Y2] - 500);
		fullscreen.setCheckMark(checkMark, 3);
		fullscreen.setChecked(Settings.getInstance().isFullscreen());
		
		turnOnSound = new CheckBox("Включити звук", bitmapFont);
		turnOnSound.setTexture(texture);
		turnOnSound.setTextureFocused(textureFocused);
		turnOnSound.setTextureOver(textureOver);
		turnOnSound.setIconSize(20, 20);
		turnOnSound.setPosition(background.getX() + 200, background.getVertices()[Batch.Y2] - 300);
		turnOnSound.setCheckMark(checkMark, 3);
		turnOnSound.setChecked(!Settings.getInstance().isMute());
		
		turnOffWhenHidden = new CheckBox("Заглушати, коли гра згорнута", bitmapFont);
		turnOffWhenHidden.setTexture(texture);
		turnOffWhenHidden.setTextureFocused(textureFocused);
		turnOffWhenHidden.setTextureOver(textureOver);
		turnOffWhenHidden.setIconSize(20, 20);
		turnOffWhenHidden.setPosition(background.getX() + 200, background.getVertices()[Batch.Y2] - 400);
		turnOffWhenHidden.setCheckMark(checkMark, 3);
		turnOffWhenHidden.setChecked(Settings.getInstance().isMuteWhenHidden());
	}
	
	private void initSelectBoxes () {
		float width = 300;
		float height = 50;
		float x = resolutionLabel.getX() + resolutionLabel.getWidth() + 20;
		
		resolutionBox = new SelectBox(x, resolutionLabel.getY() - 25, width, height);
		resolutionBox.setPosition(x, resolutionLabel.getY() - 25);
		resolutionBox.setSize(width, height);
		resolutionBox.addItem("1920 x 1080");
		resolutionBox.setTexture(Objects.getInstance().getButtonTexture());
		resolutionBox.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		resolutionBox.setTextureOver(Objects.getInstance().getButtonOverTexture());
		
		screenFormatBox = new SelectBox(x, screenFormatLabel.getY() - 25, width, height);
		screenFormatBox.setPosition(x, screenFormatLabel.getY() - 25);
		screenFormatBox.setSize(width, height);
		screenFormatBox.addItem("16:9");
		screenFormatBox.setTexture(Objects.getInstance().getButtonTexture());
		screenFormatBox.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		screenFormatBox.setTextureOver(Objects.getInstance().getButtonOverTexture());
	}
	
	private void initScales () {
		generalVolume = new Scale(100, Settings.getInstance().getGeneralVolume(), background.getX() + 850, background.getVertices()[Batch.Y2] - 300, 300, 20, true);
		interfaceVolume = new Scale(100, Settings.getInstance().getInterfaceVolume(), background.getX() + 850, background.getVertices()[Batch.Y2] - 400, 300, 20, true);
		effectsVolume = new Scale(100, Settings.getInstance().getEffectsVolume(), background.getX() + 850, background.getVertices()[Batch.Y2] - 500, 300, 20, true);
		techniqueVolume = new Scale(100, Settings.getInstance().getTechniqueVolume(), background.getX() + 850, background.getVertices()[Batch.Y2] - 600, 300, 20, true);
		environmentVolume = new Scale(100, Settings.getInstance().getEnvironmentVolume(), background.getX() + 850, background.getVertices()[Batch.Y2] - 700, 300, 20, true);
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		if (alpha > 0.7f) {
			background.setAlpha(0.7f);
		} else {
			background.setAlpha(alpha);
		}
		
		border1.setAlpha(alpha);
		border2.setAlpha(alpha);
		caption.setAlphas(alpha);
		vSync.setAlpha(alpha);
		frames.setAlpha(alpha);
		fullscreen.setAlpha(alpha);
		video.setAlpha(alpha);
		sound.setAlpha(alpha);
		management.setAlpha(alpha);
		apply.setAlpha(alpha);
		reset.setAlpha(alpha);
		screenFormatLabel.setAlphas(alpha);
		resolutionLabel.setAlphas(alpha);
		
		batch.begin();
		
		background.draw(batch);
		caption.draw(batch);
		
		border1.setSize(background.getWidth(), 2);
		border1.setPosition(background.getX(), background.getY());
		border1.draw(batch);
		
		border1.setPosition(background.getX(), background.getY() + background.getHeight() - border1.getHeight());
		border1.draw(batch);
		
		border1.setPosition(background.getX(), background.getY() + background.getHeight() - border1.getHeight() - 20 - caption.getHeight());
		border1.draw(batch);
		
		border1.setSize(2, background.getHeight());
		border1.setPosition(background.getX(), background.getY());
		border1.draw(batch);
		
		border1.setPosition(background.getX() + background.getWidth() - border1.getWidth(), background.getY());
		border1.draw(batch);
		
		video.show(batch);
		sound.show(batch);
		management.show(batch);
		
		border2.setSize(video.getWidth() + sound.getWidth() + management.getWidth() + 2, 2);
		border2.setPosition(video.getX(), video.getY() + video.getHeight() - border2.getHeight());
		border2.draw(batch);
		
		border2.setSize(2, 750);
		border2.setPosition(video.getX(), video.getY() - border2.getHeight() + video.getHeight());
		border2.draw(batch);
		
		border2.setSize(1300, 2);
		border2.setPosition(video.getX(), border2.getY());
		border2.draw(batch);
		
		border2.setSize(2, 750 - video.getHeight());
		border2.setPosition(border2.getX() + 1300 - border2.getWidth(), border2.getY());
		border2.draw(batch);
		
		if (isVideo) {
			border2.setSize(1100, 2);
			border2.setPosition(border2.getX() - border2.getWidth() + 2, video.getY());
			border2.draw(batch);
			vSync.show(batch);
			frames.show(batch);
			fullscreen.show(batch);
			screenFormatLabel.draw(batch);
			resolutionLabel.draw(batch);
		} else if (isSound) {
			border2.setSize(899, 2);
			border2.setPosition(border2.getX() - border2.getWidth() + 2, video.getY());
			border2.draw(batch);
			border2.setSize(video.getWidth(), 2);
			border2.setPosition(video.getX(), video.getY());
			border2.draw(batch);
			turnOnSound.show(batch);
			turnOffWhenHidden.show(batch);
			generalVolumeLabel.draw(batch);
			interfaceVolumeLabel.draw(batch);
			effectsVolumeLabel.draw(batch);
			techniqueVolumeLabel.draw(batch);
			environmentVolumeLabel.draw(batch);
			generalVolume.show(batch);
			interfaceVolume.show(batch);
			effectsVolume.show(batch);
			techniqueVolume.show(batch);
			environmentVolume.show(batch);
		} else {
			border2.setSize(698, 2);
			border2.setPosition(border2.getX() - border2.getWidth() + 2, video.getY());
			border2.draw(batch);
			border2.setSize(video.getWidth() + sound.getWidth() + 2, 2);
			border2.setPosition(video.getX(), video.getY());
			border2.draw(batch);
			managementTable.draw(batch);
		}
		
		if (video.isReleased()) {
			isVideo = true;
			isSound = false;
		} else if (sound.isReleased()) {
			isSound = true;
			isVideo = false;
		} else if (management.isReleased()) {
			isSound = false;
			isVideo = false;
		}
		
		border2.setSize(2, video.getHeight());
		border2.setPosition(video.getX() + video.getWidth(), video.getY());
		border2.draw(batch);
		
		border2.setSize(2, video.getHeight());
		border2.setPosition(sound.getX() + sound.getWidth(), video.getY());
		border2.draw(batch);
		
		border2.setSize(2, video.getHeight());
		border2.setPosition(management.getX() + management.getWidth(), video.getY());
		border2.draw(batch);
		
		reset.show(batch);
		apply.show(batch);
		
		if (reset.isReleased()) {
			if (isVideo) {
				vSync.setChecked(false);
				frames.setChecked(false);
				fullscreen.setChecked(true);
				Settings.getInstance().setFullscreenMode(true);
				Settings.getInstance().setVSync(false);
				Settings.getInstance().setFrameLimitation(false);
			} else if (isSound) {
				turnOnSound.setChecked(true);
				turnOffWhenHidden.setChecked(true);
				generalVolume.setValue(100);
				interfaceVolume.setValue(100);
				effectsVolume.setValue(100);
				techniqueVolume.setValue(100);
				environmentVolume.setValue(100);
				Settings.getInstance().setMute(false);
				Settings.getInstance().setMuteWhenHidden(true);
				Settings.getInstance().setGeneralVolume(100);
				Settings.getInstance().setInterfaceVolume(100);
				Settings.getInstance().setEffectsVolume(100);
				Settings.getInstance().setTechniqueVolume(100);
				Settings.getInstance().setEnvironmentVolume(100);
			}
		} else if (apply.isReleased()) {
			if (isVideo) {
				Settings.getInstance().setFullscreenMode(fullscreen.isChecked());
				Settings.getInstance().setVSync(vSync.isChecked());
				Settings.getInstance().setFrameLimitation(frames.isChecked());
			} else if (isSound) {
				Settings.getInstance().setMute(!turnOnSound.isChecked());
				Settings.getInstance().setMuteWhenHidden(turnOffWhenHidden.isChecked());
				Settings.getInstance().setGeneralVolume(generalVolume.getCurrentValue());
				Settings.getInstance().setInterfaceVolume(interfaceVolume.getCurrentValue());
				Settings.getInstance().setEffectsVolume(effectsVolume.getCurrentValue());
				Settings.getInstance().setTechniqueVolume(techniqueVolume.getCurrentValue());
				Settings.getInstance().setEnvironmentVolume(environmentVolume.getCurrentValue());
			}
		}
		
		batch.end();
		
		if (isVideo) {
			screenFormatBox.show(batch);
			resolutionBox.show(batch);
		}
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		border1.getTexture().dispose();
		border2.getTexture().dispose();
		caption.getFont().dispose();
		video.getTexture().dispose();
		video.getTextureFocused().dispose();
		video.getTextureOver().dispose();
		vSync.getTexture().dispose();
		vSync.getTextureFocused().dispose();
		vSync.getTextureOver().dispose();
		vSync.getCheckMark().dispose();
		vSync.getFont().dispose();
		generalVolume.dispose();
		interfaceVolume.dispose();
		effectsVolume.dispose();
		techniqueVolume.dispose();
		environmentVolume.dispose();
		managementTable.getTexture().dispose();
	}
}