package forms;

import java.util.ArrayDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Loading;

import answers.RegistrationAnswer;
import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import net.Client;
import util.CameraController;

public class SignUpForm implements Disposable {
	private Sprite background;
	private Label captionLabel;
	private Label loginLabel;
	private Label nicknameLabel;
	private Label passwordLabel;
	private Label repeatPasswordLabel;
	private CustomTextField loginField;
	private CustomTextField nicknameField;
	private CustomTextField passwordField;
	private CustomTextField repeatPasswordField;
	private TextButton signUpButton;
	private TextButton cancelButton;
	private Stage stage;
	private Loading loading;
	private Texture fieldError;
	private ErrorMessageForm errorMessageForm;
	private InputMultiplexer inputMultiplexer;
	private SuccessfulySignUpForm successfulySignUpForm;
	
	private float maxWidthLabel;
	private float alpha = 0.0f;
	private boolean isCancel = false;
	private boolean isHide = false;
	private boolean isLoading = false;
	private boolean isSignUp = false;
	private boolean isRegistered = false;
	
	private boolean isLoginOk = true;
	private boolean isNicknameOk = true;
	private boolean isPasswordOk = true;
	private boolean isRepeatPasswordOk = true;
	
	public SignUpForm (BitmapFont captionBitmapFont, BitmapFont simpleLablesBitmapFont, TextFieldStyle textFieldStyle, Sprite background, Texture field, Texture fieldFocused, TextButton button, Texture fieldError) {
		this.background = new Sprite(background);
		this.background.setSize(700, 350);
		this.background.setPosition(CameraController.getInstance().getWidth() / 2 - this.background.getWidth() / 2, CameraController.getInstance().getHeight() / 2 - this.background.getHeight() / 2 - 35);
		
		stage = new Stage();
		stage.getViewport().setCamera(CameraController.getInstance().getCamera());
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
		
		initLabels(captionBitmapFont, simpleLablesBitmapFont);
		initFields(textFieldStyle, field, fieldFocused);
		initButtons(button);
		this.fieldError = fieldError;
		
		this.background.setAlpha(alpha);
		captionLabel.setAlphas(alpha);
		loginLabel.setAlphas(alpha);
		nicknameLabel.setAlphas(alpha);
		passwordLabel.setAlphas(alpha);
		repeatPasswordLabel.setAlphas(alpha);
		loginField.getBackground().setAlpha(alpha);
		nicknameField.getBackground().setAlpha(alpha);
		passwordField.getBackground().setAlpha(alpha);
		repeatPasswordField.getBackground().setAlpha(alpha);
		cancelButton.setAlpha(alpha);
		signUpButton.setAlpha(alpha);
		
		loading = new Loading();
		loading.setText("Обробка...");
	}
	
	private void initLabels (BitmapFont captionBitmapFont, BitmapFont simpleLablesBitmapFont) {		
		captionLabel = new Label("Реєстрація", captionBitmapFont);
		captionLabel.setPosition(background.getX() + background.getWidth() / 2 - captionLabel.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		loginLabel = new Label("Email:", simpleLablesBitmapFont);
		nicknameLabel = new Label("Ім'я у грі:", simpleLablesBitmapFont);
		passwordLabel = new Label("Пароль:", simpleLablesBitmapFont);
		repeatPasswordLabel = new Label("Повторіть пароль:", simpleLablesBitmapFont);
		
		maxWidthLabel = Math.max(Math.max(loginLabel.getWidth(), nicknameLabel.getWidth()), Math.max(passwordLabel.getWidth(), repeatPasswordLabel.getWidth()));
		
		loginLabel.setPosition(background.getX() + 20 + maxWidthLabel - loginLabel.getWidth(), captionLabel.getY() - 70);
		nicknameLabel.setPosition(background.getX() + 20 + maxWidthLabel - nicknameLabel.getWidth(), captionLabel.getY() - 120);
		passwordLabel.setPosition(background.getX() + 20 + maxWidthLabel - passwordLabel.getWidth(), captionLabel.getY() - 170);
		repeatPasswordLabel.setPosition(background.getX() + 20 + maxWidthLabel - repeatPasswordLabel.getWidth(), captionLabel.getY() - 220);
	}
	
	private void initFields (TextFieldStyle textFieldStyle, final Texture field, final Texture fieldFocused) {
		loginField = new CustomTextField(textFieldStyle, stage);
		loginField.getBackground().setRegion(field);
		loginField.getBackground().setSize(400, 50);
		loginField.getBackground().setPosition(background.getX() + 30 + maxWidthLabel, background.getVertices()[Batch.Y2] - 120);
		loginField.getTextField().setSize(380, 50);
		loginField.getTextField().setPosition(background.getX() + 40 + maxWidthLabel, loginLabel.getY() - loginLabel.getHeight() - 15);
		loginField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					loginField.getBackground().setRegion(fieldFocused);
				} else {
					loginField.getBackground().setRegion(field);
				}
			}
		});
		
		nicknameField = new CustomTextField(textFieldStyle, stage);
		nicknameField.getBackground().setRegion(field);
		nicknameField.getBackground().setSize(400, 50);
		nicknameField.getBackground().setPosition(background.getX() + 30 + maxWidthLabel, background.getVertices()[Batch.Y2] - 170);
		nicknameField.getTextField().setSize(380, 50);
		nicknameField.getTextField().setPosition(background.getX() + 40 + maxWidthLabel, nicknameLabel.getY() - nicknameLabel.getHeight() - 15);
		nicknameField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					nicknameField.getBackground().setRegion(fieldFocused);
				} else {
					nicknameField.getBackground().setRegion(field);
				}
			}
		});
		
		passwordField = new CustomTextField(textFieldStyle, stage);
		passwordField.getBackground().setRegion(field);
		passwordField.getBackground().setSize(400, 50);
		passwordField.getBackground().setPosition(background.getX() + 30 + maxWidthLabel, background.getVertices()[Batch.Y2] - 220);
		passwordField.getTextField().setSize(380, 50);
		passwordField.getTextField().setPosition(background.getX() + 40 + maxWidthLabel, passwordLabel.getY() - passwordLabel.getHeight() - 15);
		passwordField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					passwordField.getBackground().setRegion(fieldFocused);
				} else {
					passwordField.getBackground().setRegion(field);
				}
			}
		});
		passwordField.getTextField().setPasswordCharacter('*');
		passwordField.getTextField().setPasswordMode(true);
		
		repeatPasswordField = new CustomTextField(textFieldStyle, stage);
		repeatPasswordField.getBackground().setRegion(field);
		repeatPasswordField.getBackground().setSize(400, 50);
		repeatPasswordField.getBackground().setPosition(background.getX() + 30 + maxWidthLabel, background.getVertices()[Batch.Y2] - 270);
		repeatPasswordField.getTextField().setSize(380, 50);
		repeatPasswordField.getTextField().setPosition(background.getX() + 40 + maxWidthLabel, repeatPasswordLabel.getY() - repeatPasswordLabel.getHeight() - 15);
		repeatPasswordField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					repeatPasswordField.getBackground().setRegion(fieldFocused);
				} else {
					repeatPasswordField.getBackground().setRegion(field);
				}
			}
		});
		repeatPasswordField.getTextField().setPasswordCharacter('*');
		repeatPasswordField.getTextField().setPasswordMode(true);
	}
	
	private void initButtons (TextButton background) {
		signUpButton = new TextButton("Зареєструватися", background.getLabelBitmapFont());
		signUpButton.setBitmapFontFocused(background.getLabelFocusedBitmapFont());
		signUpButton.setTexture(background.getTexture());
		signUpButton.setTextureFocused(background.getTextureFocused());
		signUpButton.setTextureOver(background.getTextureOver());
		signUpButton.setSize(250, 50);
		signUpButton.setPosition(this.background.getX() + this.background.getWidth() - signUpButton.getWidth() - 50, this.background.getVertices()[Batch.Y1] + 20);
		
		cancelButton = new TextButton("Скасувати", background.getLabelBitmapFont());
		cancelButton.setBitmapFontFocused(background.getLabelFocusedBitmapFont());
		cancelButton.setTexture(background.getTexture());
		cancelButton.setTextureFocused(background.getTextureFocused());
		cancelButton.setTextureOver(background.getTextureOver());
		cancelButton.setSize(200, 50);
		cancelButton.setPosition(this.background.getX() + 50, this.background.getVertices()[Batch.Y1] + 20);
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	private void signUp () {
		String login = loginField.getTextField().getText().trim();
		String nickname = nicknameField.getTextField().getText().trim();
		String password = passwordField.getTextField().getText().trim();
		String repeatPassword = repeatPasswordField.getTextField().getText().trim();
		
		isLoginOk = false;
		isNicknameOk = false;
		isPasswordOk = false;
		isRepeatPasswordOk = false;
		
		if (login.length() == 0) {
			isLoginOk = false;
			loginField.getBackground().setRegion(fieldError);
		} else {
			Pattern pattern = Pattern.compile("[A-Za-z0-9._]+@[A-Za-z0-9._]+");
			Matcher matcher = pattern.matcher(login);
			isLoginOk = matcher.matches();
			if (!isLoginOk) loginField.getBackground().setRegion(fieldError);
		}
		
		if (nickname.length() == 0) {
			isNicknameOk = false;
			nicknameField.getBackground().setRegion(fieldError);
		} else {
			Pattern pattern = Pattern.compile("[A-Za-z0-9_]{4,50}");
			Matcher matcher = pattern.matcher(nickname);
			isNicknameOk = matcher.matches();
			if (!isNicknameOk) nicknameField.getBackground().setRegion(fieldError);
		}
		
		if (password.length() == 0) {
			isPasswordOk = false;
			passwordField.getBackground().setRegion(fieldError);
		} else {
			Pattern pattern = Pattern.compile("[A-Za-z0-9_]{5,50}");
			Matcher matcher = pattern.matcher(password);
			isPasswordOk = matcher.matches();
			if (!isPasswordOk) passwordField.getBackground().setRegion(fieldError);
		}
		
		if (repeatPassword.length() == 0) {
			isRepeatPasswordOk = false;
			repeatPasswordField.getBackground().setRegion(fieldError);
		} else {
			isRepeatPasswordOk = true;
		}
		
		if (isPasswordOk && isRepeatPasswordOk) {
			if (!password.equals(repeatPassword)) {
				isPasswordOk = false;
				isRepeatPasswordOk = false;
				passwordField.getBackground().setRegion(fieldError);
				repeatPasswordField.getBackground().setRegion(fieldError);
			}
		}
		
		if (isLoginOk && isNicknameOk && isPasswordOk && isRepeatPasswordOk) {
			Client.getInstance().registration(login, nickname, repeatPassword);
			isLoading = true;
			isSignUp = true;
			loginField.getTextField().setDisabled(true);
			nicknameField.getTextField().setDisabled(true);
			passwordField.getTextField().setDisabled(true);
			repeatPasswordField.getTextField().setDisabled(true);
			cancelButton.setDisable(true);
			signUpButton.setDisable(true);
		}
	}
	
	public void show (SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		
		batch.begin();
		
		background.setAlpha(alpha);
		captionLabel.setAlphas(alpha);
		loginLabel.setAlphas(alpha);
		nicknameLabel.setAlphas(alpha);
		passwordLabel.setAlphas(alpha);
		repeatPasswordLabel.setAlphas(alpha);
		loginField.getBackground().setAlpha(alpha);
		nicknameField.getBackground().setAlpha(alpha);
		passwordField.getBackground().setAlpha(alpha);
		repeatPasswordField.getBackground().setAlpha(alpha);
		cancelButton.setAlpha(alpha);
		signUpButton.setAlpha(alpha);
		
		if (errorMessageForm != null) {
			errorMessageForm.show(batch);
			if (errorMessageForm.isOk()) {
				errorMessageForm.hide();
				if (errorMessageForm.getAlpha() == 0.0f) {
					loginField.getTextField().setDisabled(false);
					nicknameField.getTextField().setDisabled(false);
					passwordField.getTextField().setDisabled(false);
					repeatPasswordField.getTextField().setDisabled(false);
					cancelButton.setDisable(false);
					signUpButton.setDisable(false);
					errorMessageForm.dispose();
					errorMessageForm = null;
					alpha = 0.0f;
				}
			}
		} else if (successfulySignUpForm != null) {
			successfulySignUpForm.show(batch);
			if (successfulySignUpForm.isOk()) {
				successfulySignUpForm.hide();
				if (successfulySignUpForm.getAlpha() == 0.0f) {
					successfulySignUpForm.dispose();
					successfulySignUpForm = null;
					isRegistered = true;
				}
			}
		} else {
			if (!isRegistered) {
				background.draw(batch);
				captionLabel.draw(batch);
				loginLabel.draw(batch);
				nicknameLabel.draw(batch);
				passwordLabel.draw(batch);
				repeatPasswordLabel.draw(batch);
				loginField.getBackground().draw(batch);
				nicknameField.getBackground().draw(batch);
				passwordField.getBackground().draw(batch);
				repeatPasswordField.getBackground().draw(batch);
				cancelButton.show(batch);
				signUpButton.show(batch);
				if (alpha == 1.0f) {
					if (!loginField.getTextField().isVisible()) {
						loginField.getTextField().setVisible(true);
						nicknameField.getTextField().setVisible(true);
						passwordField.getTextField().setVisible(true);
						repeatPasswordField.getTextField().setVisible(true);
					}
				}	
			}
		}
		
		batch.end();
		
		stage.act();
		stage.draw();
		
		if (isLoading) {
			loading.show(batch);
		}
		
		if (!isHide) {
			if (cancelButton.isReleased()) {
				isCancel = true;
			} else if (signUpButton.isReleased()) {
				signUp();
			}
			if (cancelButton.isPressed() || signUpButton.isPressed()) {
				stage.setKeyboardFocus(null);
			}
		}
		
		if (isSignUp) {
			ArrayDeque <Object> objects = Client.getInstance().getServerListener().getObjects();
			if (objects.size() > 0) {
				Object object = objects.pop();
				if (object.getClass() == RegistrationAnswer.class) {
					RegistrationAnswer answer = (RegistrationAnswer) object;
					if (answer.getValue()) {
						successfulySignUpForm = new SuccessfulySignUpForm();
						loginField.getTextField().setVisible(false);
						nicknameField.getTextField().setVisible(false);
						passwordField.getTextField().setVisible(false);
						repeatPasswordField.getTextField().setVisible(false);
						loginField.getTextField().setDisabled(true);
						nicknameField.getTextField().setDisabled(true);
						passwordField.getTextField().setDisabled(true);
						repeatPasswordField.getTextField().setDisabled(true);
					} else {
						isRegistered = false;
						if (answer.getCode() == 1) {
							errorMessageForm = new ErrorMessageForm(500, 200, "Таке ім'я вже існує! Вигадайте інше");
							nicknameField.getBackground().setTexture(fieldError);
						} else if (answer.getCode() == 2) {
							errorMessageForm = new ErrorMessageForm(500, 200, "Користувач з таким email вже існує");
							loginField.getBackground().setTexture(fieldError);
						}
						loginField.getTextField().setDisabled(true);
						nicknameField.getTextField().setDisabled(true);
						passwordField.getTextField().setDisabled(true);
						repeatPasswordField.getTextField().setDisabled(true);
						loginField.getTextField().setVisible(false);
						nicknameField.getTextField().setVisible(false);
						passwordField.getTextField().setVisible(false);
						repeatPasswordField.getTextField().setVisible(false);
					}
					isSignUp = false;
					isLoading = false;
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

	public boolean isRegistered () {
		return isRegistered;
	}
	
	@Override
	public void dispose () {
		loginField.getTextField().remove();
		nicknameField.getTextField().remove();
		passwordField.getTextField().remove();
		repeatPasswordField.getTextField().remove();
		inputMultiplexer.removeProcessor(stage);
	}
}