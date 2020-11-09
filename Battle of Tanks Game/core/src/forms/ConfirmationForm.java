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
import com.mygdx.game.Objects;

import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import util.CameraController;
import util.Font;

public class ConfirmationForm implements Disposable {
	private Sprite background;
	private Label label;
	private CustomTextField codeField;
	private TextButton ok;
	private TextButton cancel;
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	private boolean isOk = false;
	private boolean isCancel = false;
	
	public ConfirmationForm () {
		Texture texture = new Texture(Gdx.files.internal("images/primary forms/message.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		stage = new Stage();
		stage.getViewport().setCamera(CameraController.getInstance().getCamera());
		
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
		
		background = new Sprite(texture);
		background.setSize(500, 200);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
		
		BitmapFont labelFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		labelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		label = new Label("Введіть код підтвердження", labelFont);
		label.setPosition(background.getX() + background.getWidth() / 2 - label.getWidth() / 2, background.getVertices()[Batch.Y2] - 20);
		
		codeField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		codeField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		codeField.getBackground().setSize(400, 50);
		codeField.getBackground().setPosition(background.getX() + background.getWidth() / 2 - codeField.getBackground().getWidth() / 2,
				label.getY() - label.getHeight() - codeField.getBackground().getHeight() - 20);
		codeField.getTextField().setSize(375, 50);
		codeField.getTextField().setPosition(background.getX() + background.getWidth() / 2 - codeField.getBackground().getWidth() / 2 + 10,
				label.getY() - label.getHeight() - codeField.getBackground().getHeight() - 14);
		codeField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					codeField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					codeField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		codeField.getTextField().setAlignment(Align.center);
		
		stage.addActor(codeField.getTextField());
		
		ok = new TextButton("ОК", Objects.getInstance().getButtonBitmapFont());
		ok.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		ok.setTexture(Objects.getInstance().getButtonTexture());
		ok.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		ok.setTextureOver(Objects.getInstance().getButtonOverTexture());
		ok.setSize(100, 50);
		ok.setPosition(background.getX() + background.getWidth() / 2 - ok.getWidth() - 20, background.getY() + 20);
		
		cancel = new TextButton("Назад", Objects.getInstance().getButtonBitmapFont());
		cancel.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		cancel.setTexture(Objects.getInstance().getButtonTexture());
		cancel.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		cancel.setTextureOver(Objects.getInstance().getButtonOverTexture());
		cancel.setSize(100, 50);
		cancel.setPosition(background.getX() + background.getWidth() / 2 + 20, background.getY() + 20);
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
		label.setAlphas(alpha);
		codeField.getBackground().setAlpha(alpha);
		ok.setAlpha(alpha);
		cancel.setAlpha(alpha);
		
		batch.begin();
		
		background.draw(batch);
		label.draw(batch);
		codeField.getBackground().draw(batch);
		ok.show(batch);
		cancel.show(batch);
		
		batch.end();
		
		if (ok.isReleased()) {
			if (codeField.getTextField().getText().trim().length() == 0) {
				setErrorField();
			} else {
				isOk = true;
			}
		} else if (cancel.isReleased()) {
			isCancel = true;
		}
		
		stage.act();
		stage.draw();
	}
	
	public void hide () {
		isHide = true;
	}
	
	public void reset () {
		isOk = false;
		isCancel = false;
	}
	
	public void setErrorField () {
		codeField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
	}
	
	public float getALpha () {
		return alpha;
	}
	
	public boolean isOk () {
		return isOk;
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	public String getCode () {
		return codeField.getTextField().getText().trim();
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		label.getFont().dispose();
		stage.clear();
		inputMultiplexer.removeProcessor(stage);
	}
}