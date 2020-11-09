package forms;

import java.util.regex.Pattern;

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
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Loading;
import com.mygdx.game.Objects;

import answers.PasswordAfterRecoveryAnswer;
import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import net.Client;
import util.CameraController;
import util.Font;

public class PasswordAfterRecoveryForm implements Disposable {
	private Sprite background;
	private Label caption;
	private Label passwordLabel;
	private Label repeatPasswordLabel;
	private CustomTextField passwordField;
	private CustomTextField repeatPasswordField;
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	private TextButton applyButton;
	private TextButton cancelButton;
	private Loading loading;
	private SuccessfulyRecoveryForm successfulyRecoveryForm;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	private boolean isCancel = false;
	private boolean isOk = false;
	private boolean isSending = false;
	private boolean isLoading = false;
	
	public PasswordAfterRecoveryForm () {
		initBackground();
		initLabels();
		initFields();
		initButtons();
		
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
	}
	
	private void initBackground () {
		Texture texture = new Texture(Gdx.files.internal("images/primary forms/signInForm.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(texture);
		background.setSize(550, 250);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2);
	}
	
	private void initLabels () {
		BitmapFont captionFont = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		captionFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Новий пароль", captionFont);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 25);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		passwordLabel = new Label("Пароль:", font);
		repeatPasswordLabel = new Label("Повторіть пароль:", font);
		
		repeatPasswordLabel.setPosition(background.getX() + 20, caption.getY() - 100);
		passwordLabel.setPosition(repeatPasswordLabel.getX() + repeatPasswordLabel.getWidth() - passwordLabel.getWidth(), caption.getY() - 50);
	}
	
	private void initFields () {
		stage = new Stage();
		
		passwordField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		passwordField.getBackground().setSize(250, 50);
		passwordField.getBackground().setPosition(repeatPasswordLabel.getX() + repeatPasswordLabel.getWidth() + 20, caption.getY() - 90);
		passwordField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		passwordField.getTextField().setSize(230, 50);
		passwordField.getTextField().setPosition(passwordField.getBackground().getX() + 10, passwordField.getBackground().getY() + 7);
		passwordField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					passwordField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					passwordField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		passwordField.getTextField().setPasswordCharacter('*');
		passwordField.getTextField().setPasswordMode(true);
		
		repeatPasswordField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		repeatPasswordField.getBackground().setSize(250, 50);
		repeatPasswordField.getBackground().setPosition(repeatPasswordLabel.getX() + repeatPasswordLabel.getWidth() + 20, caption.getY() - 145);
		repeatPasswordField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
		repeatPasswordField.getTextField().setSize(230, 50);
		repeatPasswordField.getTextField().setPosition(passwordField.getBackground().getX() + 10, repeatPasswordField.getBackground().getY() + 7);
		repeatPasswordField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					repeatPasswordField.getBackground().setRegion(Objects.getInstance().getFieldFocusedTexture());
				} else {
					repeatPasswordField.getBackground().setRegion(Objects.getInstance().getFieldTexture());
				}
			}
		});
		repeatPasswordField.getTextField().setPasswordCharacter('*');
		repeatPasswordField.getTextField().setPasswordMode(true);
		
		stage.addActor(passwordField.getTextField());
		stage.addActor(repeatPasswordField.getTextField());
	}
	
	private void initButtons () {
		applyButton = new TextButton("Зберегти", Objects.getInstance().getButtonBitmapFont());
		applyButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		applyButton.setTexture(Objects.getInstance().getButtonTexture());
		applyButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		applyButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		applyButton.setSize(200, 50);
		applyButton.setPosition(background.getX() + background.getWidth() / 2 - applyButton.getWidth(), background.getY() + 20);
		
		cancelButton = new TextButton("Скасувати", Objects.getInstance().getButtonBitmapFont());
		cancelButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		cancelButton.setTexture(Objects.getInstance().getButtonTexture());
		cancelButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		cancelButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		cancelButton.setSize(200, 50);
		cancelButton.setPosition(background.getX() + background.getWidth() / 2 + 20, applyButton.getY());
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
		passwordLabel.setAlphas(alpha);
		repeatPasswordLabel.setAlphas(alpha);
		passwordField.getBackground().setAlpha(alpha);
		repeatPasswordField.getBackground().setAlpha(alpha);
		applyButton.setAlpha(alpha);
		cancelButton.setAlpha(alpha);
		
		batch.begin();
		background.draw(batch);
		caption.draw(batch);
		passwordLabel.draw(batch);
		repeatPasswordLabel.draw(batch);
		passwordField.getBackground().draw(batch);
		repeatPasswordField.getBackground().draw(batch);
		applyButton.show(batch);
		cancelButton.show(batch);
		batch.end();
		
		if (cancelButton.isReleased()) {
			isCancel = true;
		} else if (applyButton.isReleased()) {
			String password = passwordField.getTextField().getText().trim();
			String repeatPassword = repeatPasswordField.getTextField().getText().trim();
			Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+");
			boolean isPasswordOk = false;
			boolean isRepeatPasswordOk = false;
			if (password.length() == 0) {
				passwordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
			} else {
				if (password.length() >= 5 && password.length() <= 20) {
					if (pattern.matcher(password).matches()) {
						isPasswordOk = true;
					} else {
						passwordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
					}
				} else {
					passwordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
			}
			if (repeatPassword.length() == 0) {
				repeatPasswordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
			} else {
				isRepeatPasswordOk = true;
			}
			if (isPasswordOk && isRepeatPasswordOk) {
				if (password.equals(repeatPassword)) {
					Client.getInstance().passwordAfterRecovery(password);
					isSending = true;
					passwordField.getTextField().setDisabled(true);
					repeatPasswordField.getTextField().setDisabled(true);
					isLoading = true;
					loading = new Loading();
					loading.setText("Обробка...");
					applyButton.setDisable(true);
					cancelButton.setDisable(true);
				} else {
					passwordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
					repeatPasswordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				}
			}
		}
		
		stage.act();
		stage.draw();
		
		if (successfulyRecoveryForm != null) {
			successfulyRecoveryForm.show(batch);
			if (successfulyRecoveryForm.isOk()) {
				successfulyRecoveryForm.hide();
				if (successfulyRecoveryForm.getAlpha() == 0.0f) {
					successfulyRecoveryForm.dispose();
					successfulyRecoveryForm = null;
					isOk = true;
				}
			}
		}
		
		if (isLoading) {
			loading.show(batch);
		}
		
		if (isSending) {
			if (Client.getInstance().getServerListener() != null) {
				if (Client.getInstance().getServerListener().getObjects() != null) {
					if (Client.getInstance().getServerListener().getObjects().size() > 0) {
						Object object = Client.getInstance().getServerListener().getObjects().pop();
						if (object.getClass() == PasswordAfterRecoveryAnswer.class) {
							successfulyRecoveryForm = new SuccessfulyRecoveryForm();
						} else {
							passwordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
							repeatPasswordField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
							passwordField.getTextField().setDisabled(false);
							repeatPasswordField.getTextField().setDisabled(false);
						}
						isLoading = false;
						loading.dispose();
						loading = null;
						isSending = false;
					}
				}
			}
		}
	}
	
	public void hide () {
		isHide = true;
	}
	
	public float getAlpha () {
		return alpha;
	}
	
	public boolean isOk () {
		return isOk;
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
		passwordLabel.getFont().dispose();
		inputMultiplexer.removeProcessor(stage);
		stage.clear();
		stage.dispose();
	}
}