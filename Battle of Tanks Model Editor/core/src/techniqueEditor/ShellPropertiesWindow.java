package techniqueEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Editor;

import util.Font;
import util.TextureCreator;

public class ShellPropertiesWindow implements Disposable {
	private Sprite background;
	private BitmapFontCache captionLabel;
	private BitmapFontCache textureLabel;
	private BitmapFontCache speedLabel;
	private BitmapFontCache widthLabel;
	private BitmapFontCache heightLabel;
	private TextField textureField;
	private TextField speedField;
	private TextField widthField;
	private TextField heightField;
	private TextButton closeButton;
	
	private boolean isVisible;
	
	public ShellPropertiesWindow (BitmapFont bitmapFont, final Stage stage, TextFieldStyle textFieldStyle, TextButtonStyle textButtonStyle) {
		background = new Sprite(TextureCreator.createTexture(Color.BROWN));
		background.setSize(500, 250);
		background.setPosition(Editor.getCamera().viewportWidth / 2 - background.getWidth() / 2, Editor.getCamera().viewportHeight / 2 - background.getHeight() / 2);
		
		captionLabel = new BitmapFontCache(bitmapFont);
		captionLabel.setText("Shell properties", 0, 0);
		captionLabel.setPosition(background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Shell properties") / 2, background.getVertices()[Batch.Y2] - 5);
		
		textureLabel = new BitmapFontCache(bitmapFont);
		textureLabel.setText("Texture: ", 0, 0);
		textureLabel.setPosition(background.getX() + 5, captionLabel.getY() - 30);
		
		speedLabel = new BitmapFontCache(bitmapFont);
		speedLabel.setText("Speed: ", 0, 0);
		speedLabel.setPosition(background.getX() + 5, textureLabel.getY() - 30);
		
		widthLabel = new BitmapFontCache(bitmapFont);
		widthLabel.setText("Width: ", 0, 0);
		widthLabel.setPosition(background.getX() + 5, speedLabel.getY() - 30);
		
		heightLabel = new BitmapFontCache(bitmapFont);
		heightLabel.setText("Height: ", 0, 0);
		heightLabel.setPosition(background.getX() + 5, widthLabel.getY() - 30);
		
		textureField = new TextField("", textFieldStyle);
		textureField.setSize(400, 25);
		textureField.setPosition(textureLabel.getX() + Font.getFont().getWidth(bitmapFont, "Texture: "), textureLabel.getY() - 20);
		textureField.setAlignment(Align.center);
		
		speedField = new TextField("", textFieldStyle);
		speedField.setSize(400, 25);
		speedField.setPosition(textureField.getX(), speedLabel.getY() - 20);
		speedField.setAlignment(Align.center);
		
		widthField = new TextField("", textFieldStyle);
		widthField.setSize(400, 25);
		widthField.setPosition(textureField.getX(), widthLabel.getY() - 20);
		widthField.setAlignment(Align.center);
		
		heightField = new TextField("", textFieldStyle);
		heightField.setSize(400, 25);
		heightField.setPosition(textureField.getX(), heightLabel.getY() - 20);
		heightField.setAlignment(Align.center);
		
		closeButton = new TextButton("Close", textButtonStyle);
		closeButton.setSize(200, 25);
		closeButton.setPosition(background.getX() + background.getWidth() / 2 - closeButton.getWidth() / 2, background.getY() + 5);
		closeButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= closeButton.getX() && cursor.x <= closeButton.getX() + closeButton.getWidth()
				&& cursor.y >= closeButton.getY() && cursor.y <= closeButton.getY() + closeButton.getHeight()) {
					hide();
				}
			}
		});
		
		stage.addActor(textureField);
		stage.addActor(speedField);
		stage.addActor(widthField);
		stage.addActor(heightField);
		stage.addActor(closeButton);
		
		hide();
	}
	
	public void hide () {
		isVisible = false;
		textureField.setVisible(false);
		speedField.setVisible(false);
		widthField.setVisible(false);
		heightField.setVisible(false);
		closeButton.setVisible(false);
	}
	
	public void resume () {
		isVisible = true;
		textureField.setVisible(true);
		speedField.setVisible(true);
		widthField.setVisible(true);
		heightField.setVisible(true);
		closeButton.setVisible(true);
	}
	
	public void show (SpriteBatch batch) {
		if (isVisible) {
			background.draw(batch);
			captionLabel.draw(batch);
			textureLabel.draw(batch);
			speedLabel.draw(batch);
			widthLabel.draw(batch);
			heightLabel.draw(batch);
		}
	}
	
	public boolean isVisible () {
		return isVisible;
	}
	
	public String getTexture () {
		return textureField.getText();
	}
	
	public float getSpeed () {
		return Float.parseFloat(speedField.getText());
	}
	
	public float getWidth () {
		return Float.parseFloat(widthField.getText());
	}
	
	public float getHeight () {
		return Float.parseFloat(heightField.getText());
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
	}
}