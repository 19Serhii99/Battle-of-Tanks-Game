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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Loading;
import com.mygdx.game.Objects;
import com.mygdx.game.Settings;

import answers.PasswordRecoveryAnswer;
import answers.PasswordRecoveryCodeAnswer;
import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import net.Client;
import util.CameraController;
import util.Font;

public class PasswordRecoveryForm implements Disposable {
	private Label caption;
	private Label loginLabel;
	private Label enterCodeLabel;
	private Label text1;
	private Label text2;
	private Label text3;
	private CustomTextField loginField;
	private CustomTextField enterCodeField;
	private TextButton sendButton;
	private TextButton okButton;
	private TextButton cancelButton;
	private Sprite background;
	private Stage stage;
	private InputMultiplexer inputMultiplexer;
	private Loading loading;
	private ErrorMessageForm error;
	private ConfirmationFailedForm confirmationFailedForm;
	private PasswordAfterRecoveryForm passwordAfterRecoveryForm;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	private boolean isCancel = false;
	private boolean isLoading = false;
	private boolean isSendingLogin = false;
	private boolean isSendingCode = false;
	private boolean isEnteringPassword = false;
	
	public PasswordRecoveryForm (TextFieldStyle textFieldStyle, Texture fieldTextre, Texture fieldFocusedTexture,
			BitmapFont bitmapFontButton, BitmapFont bitmapFontFocusedButton, Texture buttonTexture, Texture buttonFocusedTexture, Texture buttonOverTexture) {
		stage = new Stage();
		stage.getViewport().setCamera(CameraController.getInstance().getCamera());
		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);
		
		Texture texture = new Texture(Gdx.files.internal("images/primary forms/passwordRecoveryForm.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(texture);
		background.setSize(700, 440);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2,
				CameraController.getInstance().getHeight() / 2 - background.getHeight() / 2 - 50);
		
		initLabels();
		initFields(textFieldStyle, fieldTextre, fieldFocusedTexture);
		initButtons(bitmapFontButton, bitmapFontFocusedButton, buttonTexture, buttonFocusedTexture, buttonOverTexture);
	}
	
	private void initLabels () {
		BitmapFont bitmapFontCaption = Font.getInstance().generateBitmapFont(Color.WHITE, 40);
		bitmapFontCaption.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		caption = new Label("Відновлення паролю", bitmapFontCaption);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		BitmapFont bitmapFontMid = Font.getInstance().generateBitmapFont(Color.WHITE, 30);
		bitmapFontMid.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		loginLabel = new Label("Email:", bitmapFontMid);
		loginLabel.setPosition(background.getX() + 100, caption.getY() - 80);
		
		enterCodeLabel = new Label("Код:", bitmapFontMid);
		enterCodeLabel.setPosition(loginLabel.getX() + loginLabel.getWidth() - enterCodeLabel.getWidth(), background.getVertices()[Batch.Y2] - 270);
		
		BitmapFont bitmapFontSmall = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		bitmapFontSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		text1 = new Label("На вказаний email буде надісланий код для скидання паролю", bitmapFontSmall);
		text1.setPosition(background.getX() + background.getWidth() / 2 - text1.getWidth() / 2, caption.getY() - 120);
		
		text2 =  new Label("Якщо код не прийшов, тоді спробуйте його надіслати ще раз", bitmapFontSmall);
		text2.setPosition(background.getX() + background.getWidth() / 2 - text2.getWidth() / 2, caption.getY() - 350);
		
		text3 =  new Label("Якщо код все одно не приходить, то звертайтесь до тех. підтримки", bitmapFontSmall);
		text3.setPosition(background.getX() + background.getWidth() / 2 - text3.getWidth() / 2, caption.getY() - 380);
	}
	
	private void initFields (TextFieldStyle textFieldStyle, final Texture fieldTexture, final Texture fieldFocusedTexture) {
		loginField = new CustomTextField(textFieldStyle, stage);
		loginField.getBackground().setSize(400, 50);
		loginField.getBackground().setPosition(background.getX() + 210, caption.getY() - 120);
		loginField.getBackground().setRegion(fieldTexture);
		loginField.getTextField().setSize(380, 50);
		loginField.getTextField().setPosition(loginField.getBackground().getX() + 10, loginField.getBackground().getY() + 7);
		loginField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					loginField.getBackground().setRegion(fieldFocusedTexture);
				} else {
					loginField.getBackground().setRegion(fieldTexture);
				}
			}
		});
		
		enterCodeField = new CustomTextField(textFieldStyle, stage);
		enterCodeField.getBackground().setSize(350, 50);
		enterCodeField.getBackground().setPosition(background.getX() + 210, caption.getY() - 305);
		enterCodeField.getBackground().setRegion(fieldTexture);
		enterCodeField.getTextField().setSize(330, 50);
		enterCodeField.getTextField().setPosition(enterCodeField.getBackground().getX() + 10, enterCodeField.getBackground().getY() + 7);
		enterCodeField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					enterCodeField.getBackground().setRegion(fieldFocusedTexture);
				} else {
					enterCodeField.getBackground().setRegion(fieldTexture);
				}
			}
		});
		enterCodeField.getTextField().setAlignment(Align.center);
		enterCodeField.getTextField().setDisabled(false);
		
		stage.addActor(loginField.getTextField());
		stage.addActor(enterCodeField.getTextField());
	}
	
	private void initButtons (BitmapFont bitmapFont, BitmapFont bitmapFontFocused, Texture buttonTexture, Texture buttonFocusedTexture, Texture buttonOverTexture) {
		sendButton = new TextButton("Надіслати", bitmapFont);
		sendButton.setTexture(buttonTexture);
		sendButton.setTextureFocused(buttonFocusedTexture);
		sendButton.setTextureOver(buttonOverTexture);
		sendButton.setBitmapFontFocused(bitmapFontFocused);
		sendButton.setSize(200, 50);
		sendButton.setPosition(background.getX() + background.getWidth() / 2 - sendButton.getWidth() / 2, caption.getY() - 220);
		
		okButton = new TextButton("ОК", bitmapFont);
		okButton.setTexture(buttonTexture);
		okButton.setTextureFocused(buttonFocusedTexture);
		okButton.setTextureOver(buttonOverTexture);
		okButton.setBitmapFontFocused(bitmapFontFocused);
		okButton.setSize(75, 40);
		okButton.setPosition(enterCodeField.getBackground().getX() + enterCodeField.getTextField().getWidth() + 45, caption.getY() - 293);
		
		cancelButton = new TextButton("Назад", bitmapFont);
		cancelButton.setTexture(buttonTexture);
		cancelButton.setTextureFocused(buttonFocusedTexture);
		cancelButton.setTextureOver(buttonOverTexture);
		cancelButton.setBitmapFontFocused(bitmapFontFocused);
		cancelButton.setSize(200, 50);
		cancelButton.setPosition(background.getX() + background.getWidth() + 5, background.getVertices()[Batch.Y2] - cancelButton.getHeight());
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
		loginLabel.setAlphas(alpha);
		loginField.getBackground().setAlpha(alpha);
		text1.setAlphas(alpha);
		sendButton.setAlpha(alpha);
		enterCodeLabel.setAlphas(alpha);
		enterCodeField.getBackground().setAlpha(alpha);
		okButton.setAlpha(alpha);
		text2.setAlphas(alpha);
		text3.setAlphas(alpha);
		cancelButton.setAlpha(alpha);
		
		batch.begin();
		if (error == null) {
			background.draw(batch);
			caption.draw(batch);
			loginLabel.draw(batch);
			loginField.getBackground().draw(batch);
			text1.draw(batch);
			cancelButton.show(batch);
			
			if (alpha == 1.0f) {
				loginField.getTextField().setVisible(true);
				enterCodeField.getTextField().setVisible(true);
			}
			
			sendButton.show(batch);
			enterCodeLabel.draw(batch);
			enterCodeField.getBackground().draw(batch);
			okButton.show(batch);
			text2.draw(batch);
			text3.draw(batch);
		} else {
			error.show(batch);
			if (error.isOk()) {
				error.hide();
				if (error.getAlpha() == 0.0f) {
					error.dispose();
					error = null;
					loginField.getTextField().setDisabled(false);
					sendButton.setDisable(false);
					cancelButton.setDisable(false);
					okButton.setDisable(false);
					enterCodeField.getTextField().setDisabled(false);
					isHide = false;
				}
			}
		}
		
		batch.end();
		
		stage.act();
		stage.draw();
		
		if (isLoading) {
			loading.show(batch);
		}
		
		if (confirmationFailedForm != null) {
			confirmationFailedForm.show(batch);
		}
		
		if (isEnteringPassword) {
			if (alpha == 0.0f) {
				passwordAfterRecoveryForm.show(batch);
				if (passwordAfterRecoveryForm.isCancel()) {
					passwordAfterRecoveryForm.hide();
					if (passwordAfterRecoveryForm.getAlpha() == 0.0f) {
						passwordAfterRecoveryForm.dispose();
						passwordAfterRecoveryForm = null;
						isEnteringPassword = false;
						isHide = false;
						loginField.getTextField().setDisabled(false);
						enterCodeField.getTextField().setDisabled(false);
						loginField.getTextField().setVisible(true);
						enterCodeField.getTextField().setVisible(true);
						sendButton.setDisable(false);
						cancelButton.setDisable(false);
						okButton.setDisable(false);
					}
				} else if (passwordAfterRecoveryForm.isOk()) {
					isCancel = true;
				}
			}
		}
		
		if (!isHide) {
			if (sendButton.isPressed() || okButton.isPressed() || cancelButton.isPressed()) {
				stage.setKeyboardFocus(null);
			}
			if (sendButton.isReleased()) {
				if (confirmationFailedForm != null) {
					confirmationFailedForm.dispose();
					confirmationFailedForm = null;
				}
				String login = loginField.getTextField().getText().trim();
				if (login.length() == 0) {
					loginField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				} else {
					loading = new Loading();
					loading.setText("Надіслання...");
					isLoading = true;
					loginField.getTextField().setDisabled(true);
					enterCodeField.getTextField().setDisabled(true);
					sendButton.setDisable(true);
					cancelButton.setDisable(true);
					okButton.setDisable(true);
					isSendingLogin = true;
					Client.getInstance().passwordRecovery(login);
				}
			} else if (cancelButton.isReleased()) {
				if (confirmationFailedForm != null) {
					confirmationFailedForm.dispose();
					confirmationFailedForm = null;
				}
				isCancel = true;
			} else if (okButton.isReleased()) {
				if (confirmationFailedForm != null) {
					confirmationFailedForm.dispose();
					confirmationFailedForm = null;
				}
				String code = enterCodeField.getTextField().getText().trim();
				if (code.length() == 0) {
					enterCodeField.getBackground().setRegion(Objects.getInstance().getFieldErrorTexture());
				} else {
					loading = new Loading();
					loading.setText("Перевірка...");
					isLoading = true;
					loginField.getTextField().setDisabled(true);
					enterCodeField.getTextField().setDisabled(true);
					sendButton.setDisable(true);
					cancelButton.setDisable(true);
					okButton.setDisable(true);
					isSendingCode = true;
					Client.getInstance().passwordRecoveryCode(code);
				}
			}
		}
		
		if (isSendingLogin) {
			if (Client.getInstance().getServerListener() != null) {
				if (Client.getInstance().getServerListener().getObjects() != null) {
					if (Client.getInstance().getServerListener().getObjects().size() > 0) {
						Object object = Client.getInstance().getServerListener().getObjects().pop();
						if (object.getClass() == PasswordRecoveryAnswer.class) {
							PasswordRecoveryAnswer passwordRecoveryAnswer = (PasswordRecoveryAnswer) object;
							if (passwordRecoveryAnswer.getValue()) {
								loginField.getTextField().setDisabled(false);
								enterCodeField.getTextField().setDisabled(false);
								sendButton.setDisable(false);
								cancelButton.setDisable(false);
								okButton.setDisable(false);
								isSendingLogin = true;
							} else {
								error = new ErrorMessageForm(500, 200, "Користувача з таким email не знайдено");
								loginField.getTextField().setVisible(false);
								isHide = true;
							}
							loading.dispose();
							loading = null;
							isLoading = false;
							isSendingLogin = false;
						}
					}
				}
			}
		} else if (isSendingCode) {
			if (Client.getInstance().getServerListener() != null) {
				if (Client.getInstance().getServerListener().getObjects() != null) {
					if (Client.getInstance().getServerListener().getObjects().size() > 0) {
						Object object = Client.getInstance().getServerListener().getObjects().pop();
						if (object.getClass() == PasswordRecoveryCodeAnswer.class) {
							PasswordRecoveryCodeAnswer passwordRecoveryCodeAnswer = (PasswordRecoveryCodeAnswer) object;
							if (passwordRecoveryCodeAnswer.getValue()) {
								enterCodeField.getTextField().setVisible(false);
								loginField.getTextField().setVisible(false);
								passwordAfterRecoveryForm = new PasswordAfterRecoveryForm();
								isHide = true;
								isEnteringPassword = true;
							} else {
								confirmationFailedForm = new ConfirmationFailedForm();
								enterCodeField.getTextField().setDisabled(false);
								loginField.getTextField().setDisabled(false);
								sendButton.setDisable(false);
								cancelButton.setDisable(false);
								okButton.setDisable(false);
							}
							loading.dispose();
							loading = null;
							isLoading = false;
							isSendingCode = false;
						}
					}
				}
			}
		}
	}
	
	public float getAlpha () {
		return alpha;
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	public void hide () {
		isHide = true;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		loginField.getTextField().remove();
		enterCodeField.getTextField().remove();
		caption.getFont().dispose();
		loginLabel.getFont().dispose();
		text1.getFont().dispose();
		inputMultiplexer.removeProcessor(stage);
		if (loading != null) loading.dispose();
	}
}