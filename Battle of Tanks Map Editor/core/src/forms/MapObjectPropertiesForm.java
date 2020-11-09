package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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

import gui.CheckBox;
import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import map.MapObject;
import util.CameraController;
import util.Font;

public class MapObjectPropertiesForm implements Disposable {
	private Sprite background;
	
	private Label caption;
	private Label x;
	private Label y;
	private Label width;
	private Label height;
	private Label rotation;
	private Label whole;
	private Label destroyed;
	
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	
	private CustomTextField xField;
	private CustomTextField yField;
	private CustomTextField widthField;
	private CustomTextField heightField;
	private CustomTextField rotationField;
	private CustomTextField wholeField;
	private CustomTextField destroyedField;
	
	private TextButton apply;
	
	private CheckBox wallMark;
	private CheckBox destroyedMark;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	
	public MapObjectPropertiesForm (MapObject mapObject) {
		
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/form.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(backgroundTexture);
		background.setSize(1000, 700);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		caption = new Label("Свойства объекта", Font.getInstance().generateBitmapFont(Color.WHITE, 30));
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 15);
		
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		
		x = new Label("X:", font);
		x.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 60);
		
		y = new Label("Y:", font);
		y.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 120);
		
		width = new Label("Ширина:", font);
		width.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 180);
		
		height = new Label("Высота:", font);
		height.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 240);
		
		rotation = new Label("Поворот:", font);
		rotation.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 300);
		
		whole = new Label("Ц.Т.:", font);
		whole.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 360);
		
		destroyed = new Label("У.Т.:", font);
		destroyed.setPosition(background.getX() + 20, background.getVertices()[Batch.Y2] - 420);
		
		stage = new Stage();
		
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
		
		xField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		xField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		xField.getBackground().setSize(300, 50);
		xField.getBackground().setPosition(height.getX() + height.getWidth() + 10, x.getY() - x.getHeight() - xField.getBackground().getHeight() / 2);
		xField.getTextField().setSize(282, 50);
		xField.getTextField().setPosition(height.getX() + height.getWidth() + 20, xField.getBackground().getY() + 5);
		xField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					xField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					xField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		xField.getTextField().setAlignment(Align.center);
		xField.getTextField().setText(String.valueOf(mapObject.getX()));
		
		yField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		yField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		yField.getBackground().setSize(300, 50);
		yField.getBackground().setPosition(height.getX() + height.getWidth() + 10, y.getY() - y.getHeight() - yField.getBackground().getHeight() / 2);
		yField.getTextField().setSize(282, 50);
		yField.getTextField().setPosition(height.getX() + height.getWidth() + 20, yField.getBackground().getY() + 5);
		yField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					yField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					yField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		yField.getTextField().setAlignment(Align.center);
		yField.getTextField().setText(String.valueOf(mapObject.getY()));
		
		widthField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		widthField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		widthField.getBackground().setSize(300, 50);
		widthField.getBackground().setPosition(height.getX() + height.getWidth() + 10, width.getY() - width.getHeight() - widthField.getBackground().getHeight() / 2);
		widthField.getTextField().setSize(282, 50);
		widthField.getTextField().setPosition(height.getX() + height.getWidth() + 20, widthField.getBackground().getY() + 5);
		widthField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					widthField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					widthField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		widthField.getTextField().setAlignment(Align.center);
		widthField.getTextField().setText(String.valueOf(mapObject.getWidth()));
		
		heightField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		heightField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		heightField.getBackground().setSize(300, 50);
		heightField.getBackground().setPosition(height.getX() + height.getWidth() + 10, height.getY() - height.getHeight() - heightField.getBackground().getHeight() / 2);
		heightField.getTextField().setSize(282, 50);
		heightField.getTextField().setPosition(height.getX() + height.getWidth() + 20, heightField.getBackground().getY() + 5);
		heightField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					heightField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					heightField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		heightField.getTextField().setAlignment(Align.center);
		heightField.getTextField().setText(String.valueOf(mapObject.getHeight()));
		
		rotationField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		rotationField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		rotationField.getBackground().setSize(300, 50);
		rotationField.getBackground().setPosition(height.getX() + height.getWidth() + 10, rotation.getY() - rotation.getHeight() - rotationField.getBackground().getHeight() / 2);
		rotationField.getTextField().setSize(282, 50);
		rotationField.getTextField().setPosition(height.getX() + height.getWidth() + 20, rotationField.getBackground().getY() + 5);
		rotationField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					rotationField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					rotationField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		rotationField.getTextField().setAlignment(Align.center);
		rotationField.getTextField().setText(String.valueOf(mapObject.getRotation()));
		
		wholeField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		wholeField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		wholeField.getBackground().setSize(300, 50);
		wholeField.getBackground().setPosition(height.getX() + height.getWidth() + 10, whole.getY() - whole.getHeight() - wholeField.getBackground().getHeight() / 2);
		wholeField.getTextField().setSize(282, 50);
		wholeField.getTextField().setPosition(height.getX() + height.getWidth() + 20, wholeField.getBackground().getY() + 5);
		wholeField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					wholeField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					wholeField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		wholeField.getTextField().setAlignment(Align.center);
		wholeField.getTextField().setText(mapObject.getWholeTexture() == null ? "" : mapObject.getWholeTexture().getPath());
		
		destroyedField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		destroyedField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		destroyedField.getBackground().setSize(300, 50);
		destroyedField.getBackground().setPosition(height.getX() + height.getWidth() + 10, destroyed.getY() - destroyed.getHeight() - destroyedField.getBackground().getHeight() / 2);
		destroyedField.getTextField().setSize(282, 50);
		destroyedField.getTextField().setPosition(height.getX() + height.getWidth() + 20, destroyedField.getBackground().getY() + 5);
		destroyedField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					destroyedField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					destroyedField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		destroyedField.getTextField().setAlignment(Align.center);
		destroyedField.getTextField().setText(mapObject.getDestroyedTexture() == null ? "" : mapObject.getDestroyedTexture().getPath());
		
		stage.addActor(xField.getTextField());
		stage.addActor(yField.getTextField());
		stage.addActor(widthField.getTextField());
		stage.addActor(heightField.getTextField());
		stage.addActor(rotationField.getTextField());
		stage.addActor(wholeField.getTextField());
		stage.addActor(destroyedField.getTextField());
		
		apply = new TextButton("Применить", Objects.getInstance().getButtonBitmapFont());
		apply.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		apply.setTexture(Objects.getInstance().getButtonTexture());
		apply.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		apply.setTextureOver(Objects.getInstance().getButtonOverTexture());
		apply.setSize(200, 50);
		apply.setPosition(background.getX() + background.getWidth() / 2 - apply.getWidth() / 2, background.getY() + 50);
		
		BitmapFont bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		wallMark = new CheckBox("Преграда", bitmapFont);
		
		Texture texture = new Texture(Gdx.files.internal("images/checkBox.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureOver = new Texture(Gdx.files.internal("images/checkBoxOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Texture textureFocused = new Texture(Gdx.files.internal("images/checkBox.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		wallMark.setTexture(texture);
		wallMark.setTextureFocused(textureFocused);
		wallMark.setTextureOver(textureOver);
		wallMark.setIconSize(20, 20);
		wallMark.setPosition(background.getVertices()[Batch.X3] - 200, background.getVertices()[Batch.Y2] - 100);
		
		Texture checkMark = new Texture(Gdx.files.internal("images/checkMark.png"));
		checkMark.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		wallMark.setCheckMark(checkMark, 3);
		wallMark.setChecked(mapObject.isWall());
		
		destroyedMark = new CheckBox("Разрушаемая", bitmapFont);
		destroyedMark.setTexture(texture);
		destroyedMark.setTextureFocused(textureFocused);
		destroyedMark.setTextureOver(textureOver);
		destroyedMark.setIconSize(20, 20);
		destroyedMark.setPosition(background.getVertices()[Batch.X3] - 200, background.getVertices()[Batch.Y2] - 150);
		destroyedMark.setCheckMark(checkMark, 3);
		destroyedMark.setChecked(mapObject.isDestroyedWall());
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
		x.setAlphas(alpha);
		y.setAlphas(alpha);
		width.setAlphas(alpha);
		height.setAlphas(alpha);
		xField.getBackground().setAlpha(alpha);
		yField.getBackground().setAlpha(alpha);
		widthField.getBackground().setAlpha(alpha);
		heightField.getBackground().setAlpha(alpha);
		apply.setAlpha(alpha);
		wallMark.setAlpha(alpha);
		rotation.setAlphas(alpha);
		rotationField.getBackground().setAlpha(alpha);
		whole.setAlphas(alpha);
		wholeField.getBackground().setAlpha(alpha);
		destroyedMark.setAlpha(alpha);
		destroyed.setAlphas(alpha);
		destroyedField.getBackground().setAlpha(alpha);
		
		batch.begin();
		
		background.draw(batch);
		caption.draw(batch);
		x.draw(batch);
		y.draw(batch);
		width.draw(batch);
		height.draw(batch);
		xField.getBackground().draw(batch);
		yField.getBackground().draw(batch);
		widthField.getBackground().draw(batch);
		heightField.getBackground().draw(batch);
		apply.show(batch);
		wallMark.show(batch);
		rotation.draw(batch);
		rotationField.getBackground().draw(batch);
		whole.draw(batch);
		wholeField.getBackground().draw(batch);
		destroyedMark.show(batch);
		destroyed.draw(batch);
		destroyedField.getBackground().draw(batch);
		
		batch.end();
		
		stage.act();
		stage.draw();
	}
	
	public boolean isApply () {
		return apply.isReleased();
	}
	
	public float getX () {
		return Float.parseFloat(xField.getTextField().getText().trim());
	}
	
	public float getY () {
		return Float.parseFloat(yField.getTextField().getText().trim());
	}
	
	public float getWidth () {
		return Float.parseFloat(widthField.getTextField().getText().trim());
	}
	
	public float getHeight () {
		return Float.parseFloat(heightField.getTextField().getText().trim());
	}
	
	public boolean isWall () {
		return wallMark.isChecked();
	}
	
	public float getRotation () {
		return Float.parseFloat(rotationField.getTextField().getText().trim());
	}
	
	public boolean isDestroyed () {
		return destroyedMark.isChecked();
	}
	
	public String getWholeTexturePath () {
		return wholeField.getTextField().getText().trim();
	}
	
	public String getDestroyedTexturePath () {
		return destroyedField.getTextField().getText().trim();
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		x.getFont().dispose();
		stage.dispose();
		stage.clear();
		inputMultiplexer.removeProcessor(stage);
		wallMark.getCheckMark().dispose();
		wallMark.getTexture().dispose();
		wallMark.getTextureFocused().dispose();
		wallMark.getTextureOver().dispose();
	}
}