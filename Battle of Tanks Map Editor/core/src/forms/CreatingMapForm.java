package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.editor.Objects;

import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;

public class CreatingMapForm implements Disposable {
	private Sprite background;
	
	private Label captionLabel;
	private Label tileAmountXLabel;
	private Label tileAmountYLabel;
	private Label tileWidthLabel;
	private Label tileHeightLabel;
	private Label chunkWidthLabel;
	private Label chunkHeightLabel;
	
	private CustomTextField tileAmountXField;
	private CustomTextField tileAmountYField;
	private CustomTextField tileWidthField;
	private CustomTextField tileHeightField;
	private CustomTextField chunkWidthField;
	private CustomTextField chunkHeightField;
	
	private TextButton createButton;
	private TextButton cancelButton;
	
	private Stage stage;
	
	private int tileAmountX;
	private int tileAmountY;
	private float tileWidth;
	private float tileHeight;
	private float chunkWidth;
	private float chunkHeight;
	private float alpha = 0.0f;
	private boolean isHide = false;
	private boolean isCancel = false;
	private boolean isCreate = false;
	
	public CreatingMapForm (Stage stage) {
		initBackground();
		initLabels();
		initFields(stage);
		initButtons();
		
		this.stage = stage;
	}
	
	private void initBackground () {
		Texture texture = new Texture(Gdx.files.internal("images/form.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(texture);
		background.setSize(450, 600);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2,
				CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
	}
	
	private void initLabels () {
		BitmapFont captionBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		captionBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		BitmapFont labelBitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		labelBitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		captionLabel = new Label("Создание карты", captionBitmapFont);
		captionLabel.setPosition(background.getX() + background.getWidth() / 2 - captionLabel.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		tileAmountXLabel = new Label("Количество тайлов по горизонтали", labelBitmapFont);
		tileAmountXLabel.setPosition(background.getX() + background.getWidth() / 2 - tileAmountXLabel.getWidth() / 2,  captionLabel.getY() - 40);
		
		tileAmountYLabel = new Label("Количество тайлов по вертикали", labelBitmapFont);
		tileAmountYLabel.setPosition(background.getX() + background.getWidth() / 2 - tileAmountYLabel.getWidth() / 2,  captionLabel.getY() - 110);
		
		tileWidthLabel = new Label("Ширина тайла", labelBitmapFont);
		tileWidthLabel.setPosition(background.getX() + background.getWidth() / 2 - tileWidthLabel.getWidth() / 2,  captionLabel.getY() - 180);
		
		tileHeightLabel = new Label("Высота тайла", labelBitmapFont);
		tileHeightLabel.setPosition(background.getX() + background.getWidth() / 2 - tileHeightLabel.getWidth() / 2,  captionLabel.getY() - 250);
		
		chunkWidthLabel = new Label("Ширина чанка", labelBitmapFont);
		chunkWidthLabel.setPosition(background.getX() + background.getWidth() / 2 - chunkWidthLabel.getWidth() / 2,  captionLabel.getY() - 320);
		
		chunkHeightLabel = new Label("Высота чанка", labelBitmapFont);
		chunkHeightLabel.setPosition(background.getX() + background.getWidth() / 2 - chunkHeightLabel.getWidth() / 2,  captionLabel.getY() - 390);
	}
	
	private void initFields (Stage stage) {
		tileAmountXField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		tileAmountXField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		tileAmountXField.getBackground().setSize(300, 50);
		tileAmountXField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - tileAmountXField.getBackground().getWidth() / 2, 560);
		tileAmountXField.getTextField().setSize(282, 50);
		tileAmountXField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - tileAmountXField.getBackground().getWidth() / 2 + 10, 566);
		tileAmountXField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					tileAmountXField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					tileAmountXField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		tileAmountXField.getTextField().setAlignment(Align.center);
		
		tileAmountYField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		tileAmountYField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		tileAmountYField.getBackground().setSize(300, 50);
		tileAmountYField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - tileAmountYField.getBackground().getWidth() / 2, 490);
		tileAmountYField.getTextField().setSize(282, 50);
		tileAmountYField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - tileAmountYField.getBackground().getWidth() / 2 + 10, 496);
		tileAmountYField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					tileAmountYField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					tileAmountYField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		tileAmountYField.getTextField().setAlignment(Align.center);
		
		tileWidthField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		tileWidthField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		tileWidthField.getBackground().setSize(300, 50);
		tileWidthField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - tileWidthField.getBackground().getWidth() / 2, 420);
		tileWidthField.getTextField().setSize(282, 50);
		tileWidthField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - tileWidthField.getBackground().getWidth() / 2 + 10, 426);
		tileWidthField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					tileWidthField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					tileWidthField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		tileWidthField.getTextField().setAlignment(Align.center);
		
		tileHeightField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		tileHeightField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		tileHeightField.getBackground().setSize(300, 50);
		tileHeightField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - tileHeightField.getBackground().getWidth() / 2, 350);
		tileHeightField.getTextField().setSize(282, 50);
		tileHeightField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - tileHeightField.getBackground().getWidth() / 2 + 10, 356);
		tileHeightField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					tileHeightField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					tileHeightField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		tileHeightField.getTextField().setAlignment(Align.center);
		
		chunkWidthField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		chunkWidthField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		chunkWidthField.getBackground().setSize(300, 50);
		chunkWidthField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - chunkWidthField.getBackground().getWidth() / 2, 280);
		chunkWidthField.getTextField().setSize(282, 50);
		chunkWidthField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - chunkWidthField.getBackground().getWidth() / 2 + 10, 286);
		chunkWidthField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					chunkWidthField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					chunkWidthField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		chunkWidthField.getTextField().setAlignment(Align.center);
		
		chunkHeightField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		chunkHeightField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		chunkHeightField.getBackground().setSize(300, 50);
		chunkHeightField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - chunkHeightField.getBackground().getWidth() / 2, 210);
		chunkHeightField.getTextField().setSize(282, 50);
		chunkHeightField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - chunkHeightField.getBackground().getWidth() / 2 + 10, 216);
		chunkHeightField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					chunkHeightField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					chunkHeightField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		chunkHeightField.getTextField().setAlignment(Align.center);
		
		stage.addActor(tileAmountXField.getTextField());
		stage.addActor(tileAmountYField.getTextField());
		stage.addActor(tileWidthField.getTextField());
		stage.addActor(tileHeightField.getTextField());
		stage.addActor(chunkWidthField.getTextField());
		stage.addActor(chunkHeightField.getTextField());
	}
	
	private void initButtons () {
		createButton = new TextButton("Создать", Objects.getInstance().getButtonBitmapFont());
		createButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		createButton.setTexture(Objects.getInstance().getButtonTexture());
		createButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		createButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		createButton.setSize(200, 50);
		createButton.setPosition(background.getX() + 20, background.getY() + 30);
		
		cancelButton = new TextButton("Отменить", Objects.getInstance().getButtonBitmapFont());
		cancelButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		cancelButton.setTexture(Objects.getInstance().getButtonTexture());
		cancelButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		cancelButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		cancelButton.setSize(200, 50);
		cancelButton.setPosition(background.getX() + background.getWidth() - cancelButton.getWidth() - 20, background.getY() + 30);
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
		captionLabel.setAlphas(alpha);
		tileAmountXLabel.setAlphas(alpha);
		tileAmountYLabel.setAlphas(alpha);
		tileWidthLabel.setAlphas(alpha);
		tileHeightLabel.setAlphas(alpha);
		chunkWidthLabel.setAlphas(alpha);
		chunkHeightLabel.setAlphas(alpha);
		createButton.setAlpha(alpha);
		cancelButton.setAlpha(alpha);
		tileAmountXField.getBackground().setAlpha(alpha);
		tileAmountYField.getBackground().setAlpha(alpha);
		tileWidthField.getBackground().setAlpha(alpha);
		tileHeightField.getBackground().setAlpha(alpha);
		chunkWidthField.getBackground().setAlpha(alpha);
		chunkHeightField.getBackground().setAlpha(alpha);
		
		background.draw(batch);
		captionLabel.draw(batch);
		tileAmountXLabel.draw(batch);
		tileAmountYLabel.draw(batch);
		tileWidthLabel.draw(batch);
		tileHeightLabel.draw(batch);
		chunkWidthLabel.draw(batch);
		chunkHeightLabel.draw(batch);
		tileAmountXField.getBackground().draw(batch);
		tileAmountYField.getBackground().draw(batch);
		tileWidthField.getBackground().draw(batch);
		tileHeightField.getBackground().draw(batch);
		chunkWidthField.getBackground().draw(batch);
		chunkHeightField.getBackground().draw(batch);
		createButton.show(batch);
		cancelButton.show(batch);
		
		if (createButton.isReleased()) {
			String tileAmountX = tileAmountXField.getTextField().getText().trim();
			String tileAmountY = tileAmountYField.getTextField().getText().trim();
			String tileWidth = tileWidthField.getTextField().getText().trim();
			String tileHeight = tileHeightField.getTextField().getText().trim();
			String chunkWidth = chunkWidthField.getTextField().getText().trim();
			String chunkHeight = chunkHeightField.getTextField().getText().trim();
			
			boolean isTileAmountXOk = false;
			boolean isTileAmountYOk = false;
			boolean isTileWidthOk = false;
			boolean isTileHeightOk = false;
			boolean isChunkWidthOk = false;
			boolean isChunkHeightOk = false;
			
			if (tileAmountX.length() == 0) {
				isTileAmountXOk = false;
			} else {
				try {
					isTileAmountXOk = Integer.parseInt(tileAmountX) <= 0 ? false : true;
				} catch (NumberFormatException e) {
					isTileAmountXOk = false;
				}
			}
			if (tileAmountY.length() == 0) {
				isTileAmountYOk = false;
			} else {
				try {
					isTileAmountYOk = Integer.parseInt(tileAmountY) <= 0 ? false : true;
				} catch (NumberFormatException e) {
					isTileAmountYOk = false;
				}
			}
			if (tileWidth.length() == 0) {
				isTileWidthOk = false;
			} else {
				try {
					isTileWidthOk = Float.parseFloat(tileWidth) <= 0.0f ? false : true;
				} catch (NumberFormatException e) {
					isTileWidthOk = false;
				}
			}
			if (tileHeight.length() == 0) {
				isTileHeightOk = false;
			} else {
				try {
					isTileHeightOk = Float.parseFloat(tileHeight) <= 0.0f ? false : true;
				} catch (NumberFormatException e) {
					isTileHeightOk = false;
				}
			}
			if (chunkWidth.length() == 0) {
				isChunkWidthOk = false;
			} else {
				try {
					isChunkWidthOk = Float.parseFloat(chunkWidth) <= 0.0f ? false : true;
				} catch (NumberFormatException e) {
					isChunkWidthOk = false;
				}
			}
			if (chunkHeight.length() == 0) {
				isChunkHeightOk = false;
			} else {
				try {
					isChunkHeightOk = Float.parseFloat(chunkHeight) <= 0.0f ? false : true;
				} catch (NumberFormatException e) {
					isChunkHeightOk = false;
				}
			}
			if (isTileAmountXOk && isTileAmountYOk && isTileWidthOk && isTileHeightOk && isChunkWidthOk && isChunkHeightOk) {
				this.tileAmountX = Integer.parseInt(tileAmountX);
				this.tileAmountY = Integer.parseInt(tileAmountY);
				this.tileWidth = Float.parseFloat(tileWidth);
				this.tileHeight = Float.parseFloat(tileHeight);
				this.chunkWidth = Float.parseFloat(chunkWidth);
				this.chunkHeight = Float.parseFloat(chunkHeight);
				this.isCreate = true;
			} else {
				if (!isTileAmountXOk) {
					tileAmountXField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
				if (!isTileAmountYOk) {
					tileAmountYField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
				if (!isTileWidthOk) {
					tileWidthField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
				if (!isTileHeightOk) {
					tileHeightField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
				if (!isChunkWidthOk) {
					chunkWidthField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
				if (!isChunkHeightOk) {
					chunkHeightField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
			}
		} else if (cancelButton.isReleased()) {
			isCancel = true;
		}
		
		if (createButton.isPressed() || cancelButton.isPressed()) {
			stage.setKeyboardFocus(null);
		}
	}
	
	public void hide () {
		isHide = true;
	}
	
	public int getTileAmountX () {
		return tileAmountX;
	}
	
	public int getTileAmountY () {
		return tileAmountY;
	}
	
	public float getTileWidth () {
		return tileWidth;
	}
	
	public float getTileHeight () {
		return tileHeight;
	}
	
	public float getChunkWidth () {
		return chunkWidth;
	}
	
	public float getChunkHeight () {
		return chunkHeight;
	}
	
	public boolean isCreate () {
		return isCreate;
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	public float getAlpha () {
		return alpha;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		captionLabel.getFont().dispose();
		tileAmountXLabel.getFont().dispose();
		tileAmountXField.getTextField().remove();
		tileAmountYField.getTextField().remove();
		tileWidthField.getTextField().remove();
		tileHeightField.getTextField().remove();
		chunkWidthField.getTextField().remove();
		chunkHeightField.getTextField().remove();
	}
}