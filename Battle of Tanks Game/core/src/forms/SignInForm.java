package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Loading;
import com.mygdx.game.Objects;
import com.mygdx.game.RSA;
import com.mygdx.game.Settings;

import answers.ConfirmationCodeAccAnswer;
import answers.ModulesAnswer;
import answers.RSAKey;
import answers.SignInAnswer;
import gui.Button;
import gui.CheckBox;
import gui.CustomTextField;
import gui.Label;
import gui.TextButton;
import gui.UnderlinedButton;
import net.Client;
import technique.Gun;
import technique.ShootingStyle;
import technique.TechniqueType;
import util.CameraController;
import util.Font;

public class SignInForm implements Disposable {
	private Stage stage;
	private Settings settings;
	private Sprite background;
	private Sprite backgroundSignIn;
	private Sprite logo;
	private CameraController camera;
	private Font font;
	private BitmapFont bitmapFont;
	private Label signInLabel;
	private Label loginLabel;
	private Label passwordLabel;
	private CustomTextField loginField;
	private CustomTextField passwordField;
	private TextButton signInButton;
	private Texture fieldTexture;
	private Texture fieldFocusedTexture;
	private Texture fieldErrorTexture;
	private CheckBox rememberMeCheckBox;
	private UnderlinedButton forgotPasswordButton;
	private UnderlinedButton createNewAccButton;
	private SignUpForm signUpForm;
	private ExitFromGameForm exitFromGameForm;
	private PasswordRecoveryForm passwordRecoveryForm;
	private Button exitButton;
	private Loading loading;
	private SignInFailForm signInFailForm;
	private InputMultiplexer inputMultiplexer;
	private ConfirmationForm confirmationForm;
	private ConfirmationFailedForm confirmationFailedForm;
	private ErrorMessageForm errorConnecting;

	private boolean isSignIn = false; // Нажал ли войти
	private boolean isSignUp = false;
	private boolean isExit = false;
	private boolean isHide = false;
	private boolean isLoading = false;
	private boolean isPasswordRecovery = false;
	private boolean isConfirmationCodeAcc = false;
	private boolean isModules = false;
	private float alpha = 0.0f;

	private boolean isLogin = false; // Вошел ли в учетку

	public SignInForm() {
		camera = CameraController.getInstance();
		font = Font.getInstance();
		settings = Settings.getInstance();
		stage = new Stage();
		stage.getViewport().setCamera(CameraController.getInstance().getCamera());
		logo = new Sprite(new Texture(Gdx.files.internal("images/primary forms/logo.png")));
		logo.setPosition(camera.getWidth() / 2 - logo.getWidth() / 2, camera.getHeight() - 175);

		initBackground();
		initLabels();
		initFields();
		initButtons();
		initCheckBox();

		inputMultiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
		inputMultiplexer.addProcessor(stage);

		loading = new Loading();
		loading.setText("Аутентифікація...");

		if (Settings.getInstance().isRememberAccount()) {
			loginField.getTextField().setText(Settings.getInstance().getLogin());
			passwordField.getTextField().setText(Settings.getInstance().getPassword());
			rememberMeCheckBox.setChecked(true);
		}
	}

	private void initBackground() {
		background = new Sprite(new Texture(Gdx.files.internal("images/background.jpg")));
		background.setSize(camera.getWidth(), camera.getHeight());

		backgroundSignIn = new Sprite(new Texture(Gdx.files.internal("images/primary forms/signInForm.png")));
		backgroundSignIn.setSize(600, 320);
		backgroundSignIn.setPosition(camera.getWidth() / 2 - backgroundSignIn.getWidth() / 2,
				camera.getHeight() / 2 - backgroundSignIn.getHeight() / 2 - 35);
	}

	private void initLabels() {
		bitmapFont = font.generateBitmapFont(Color.WHITE, 40);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		signInLabel = new Label(settings.getLanguage().getSignIn(), bitmapFont);
		signInLabel.setPosition(backgroundSignIn.getX() + backgroundSignIn.getWidth() / 2 - signInLabel.getWidth() / 2,
				backgroundSignIn.getVertices()[Batch.Y2] - 10);

		bitmapFont = font.generateBitmapFont(Color.WHITE, 25);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		loginLabel = new Label("Email:", bitmapFont);
		loginLabel.setPosition(backgroundSignIn.getX() + 50, signInLabel.getY() - 60);

		passwordLabel = new Label("Пароль:", bitmapFont);
		passwordLabel.setPosition(backgroundSignIn.getX() + 30, loginLabel.getY() - 70);
	}

	private void initFields() {
		fieldTexture = new Texture(Gdx.files.internal("images/gui/field.png"));
		fieldFocusedTexture = new Texture(Gdx.files.internal("images/gui/fieldFocused.png"));
		fieldErrorTexture = new Texture(Gdx.files.internal("images/gui/fieldError.png"));

		fieldTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fieldFocusedTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fieldErrorTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		loginField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		loginField.getBackground().setRegion(fieldTexture);
		loginField.getBackground().setSize(400, 50);
		loginField.getBackground().setPosition(backgroundSignIn.getX() + 127,
				backgroundSignIn.getVertices()[Batch.Y2] - 110);
		loginField.getTextField().setSize(375, 50);
		loginField.getTextField().setPosition(loginLabel.getX() + loginLabel.getWidth() + 20,
				loginLabel.getY() - loginLabel.getHeight() - 15);
		loginField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					loginField.getBackground().setRegion(fieldFocusedTexture);
				} else {
					loginField.getBackground().setRegion(fieldTexture);
				}
			}
		});
		loginField.getTextField().setVisible(false);

		passwordField = new CustomTextField(Objects.getInstance().getTextFieldStyle(), stage);
		passwordField.getBackground().setRegion(fieldTexture);
		passwordField.getBackground().setSize(400, 50);
		passwordField.getBackground().setPosition(loginField.getBackground().getX(),
				loginField.getBackground().getY() - 70);
		passwordField.getTextField().setSize(375, 50);
		passwordField.getTextField().setPosition(loginField.getTextField().getX(),
				passwordLabel.getY() - passwordLabel.getHeight() - 15);
		passwordField.getTextField().setPasswordCharacter('*');
		passwordField.getTextField().setPasswordMode(true);
		passwordField.getTextField().addListener(new FocusListener() {
			@Override
			public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
				super.keyboardFocusChanged(event, actor, focused);
				if (focused) {
					passwordField.getBackground().setRegion(fieldFocusedTexture);
				} else {
					passwordField.getBackground().setRegion(fieldTexture);
				}
			}
		});
		passwordField.getTextField().setVisible(false);
	}

	private void initButtons() {
		Texture texture = new Texture(Gdx.files.internal("images/gui/button.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture textureOver = new Texture(Gdx.files.internal("images/gui/buttonOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture textureFocused = new Texture(Gdx.files.internal("images/gui/buttonFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 25);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		BitmapFont bitmapFontFocused = Font.getInstance().generateBitmapFont(Color.BLACK, 25);
		bitmapFontFocused.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		signInButton = new TextButton("Увійти", bitmapFont);
		signInButton.setSize(200, 50);
		signInButton.setPosition(backgroundSignIn.getX() + backgroundSignIn.getWidth() - signInButton.getWidth() - 80,
				backgroundSignIn.getVertices()[Batch.Y2] - 250);
		signInButton.setTexture(texture);
		signInButton.setTextureOver(textureOver);
		signInButton.setTextureFocused(textureFocused);
		signInButton.setBitmapFontFocused(bitmapFontFocused);

		bitmapFont = Font.getInstance().generateBitmapFont(Color.GREEN, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		BitmapFont bitmapFontOver = Font.getInstance().generateBitmapFont(Color.ORANGE, 20);

		forgotPasswordButton = new UnderlinedButton("Забули пароль?", bitmapFont, Color.GREEN);
		forgotPasswordButton.setPosition(backgroundSignIn.getX() + 40, backgroundSignIn.getVertices()[Batch.Y1] + 40);
		forgotPasswordButton.setBitmapFontOver(bitmapFontOver, Color.ORANGE);

		createNewAccButton = new UnderlinedButton("Створити новий обліковий запис", bitmapFont, Color.GREEN);
		createNewAccButton.setPosition(backgroundSignIn.getX() + backgroundSignIn.getWidth() - 330,
				backgroundSignIn.getVertices()[Batch.Y1] + 40);
		createNewAccButton.setBitmapFontOver(bitmapFontOver, Color.ORANGE);

		texture = new Texture(Gdx.files.internal("images/primary forms/exitButton.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		textureOver = new Texture(Gdx.files.internal("images/primary forms/exitButtonOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		textureFocused = new Texture(Gdx.files.internal("images/primary forms/exitButtonFocused.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		exitButton = new Button();
		exitButton.setTexture(texture);
		exitButton.setTextureOver(textureOver);
		exitButton.setTextureFocused(textureFocused);
		exitButton.setSize(75, 75);
		exitButton.setPosition(background.getX() + background.getWidth() - exitButton.getWidth() - 10, 10);
	}

	private void initCheckBox() {
		bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		rememberMeCheckBox = new CheckBox("Запам'ятати мене", bitmapFont);

		Texture texture = new Texture(Gdx.files.internal("images/gui/checkBox.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture textureOver = new Texture(Gdx.files.internal("images/gui/checkBoxOver.png"));
		textureOver.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Texture textureFocused = new Texture(Gdx.files.internal("images/gui/checkBox.png"));
		textureFocused.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		rememberMeCheckBox.setTexture(texture);
		rememberMeCheckBox.setTextureFocused(textureFocused);
		rememberMeCheckBox.setTextureOver(textureOver);
		rememberMeCheckBox.setIconSize(20, 20);
		rememberMeCheckBox.setPosition(backgroundSignIn.getX() + 50, backgroundSignIn.getVertices()[Batch.Y2] - 230);

		Texture checkMark = new Texture(Gdx.files.internal("images/gui/checkMark.png"));
		checkMark.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		rememberMeCheckBox.setCheckMark(checkMark, 3);
	}

	private void signIn() {
		if (!isSignIn) {
			String login = loginField.getTextField().getText().trim();
			String password = passwordField.getTextField().getText().trim();
			boolean isLoginOk = false;
			boolean isPasswordOk = false;
			if (login.length() == 0) {
				isLoginOk = false;
				loginField.getBackground().setRegion(fieldErrorTexture);
			} else {
				isLoginOk = true;
			}
			if (password.length() == 0) {
				isPasswordOk = false;
				passwordField.getBackground().setRegion(fieldErrorTexture);
			} else {
				isPasswordOk = true;
			}
			if (isLoginOk && isPasswordOk) {
				isLoading = true;
				setObjectsDisable(true);
				try {
					final byte[] loginBytes = RSA.getInstance().encrypt(login);
					final byte[] passwordBytes = RSA.getInstance().encrypt(password);
					Client.getInstance().signIn(new String(loginBytes, "ISO-8859-1"), new String(passwordBytes, "ISO-8859-1"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				isSignIn = true;
			}
		}
	}

	public void show(SpriteBatch batch) {
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f)
				alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) {
				alpha = 1.0f;
				loginField.getTextField().setVisible(true);
				passwordField.getTextField().setVisible(true);
			}
		}
		backgroundSignIn.setAlpha(alpha);
		signInLabel.setAlphas(alpha);
		loginLabel.setAlphas(alpha);
		passwordLabel.setAlphas(alpha);
		signInButton.setAlpha(alpha);
		loginField.getBackground().setAlpha(alpha);
		passwordField.getBackground().setAlpha(alpha);
		rememberMeCheckBox.setAlpha(alpha);
		forgotPasswordButton.setAlpha(alpha);
		createNewAccButton.setAlpha(alpha);
		exitButton.getSprite().setAlpha(alpha);
		batch.begin();
		background.draw(batch);
		logo.draw(batch);
		batch.end();
		if (signUpForm == null && exitFromGameForm == null && passwordRecoveryForm == null && confirmationForm == null
				&& errorConnecting == null) {
			batch.begin();
			backgroundSignIn.draw(batch);
			signInLabel.draw(batch);
			loginLabel.draw(batch);
			passwordLabel.draw(batch);
			signInButton.show(batch);
			loginField.getBackground().draw(batch);
			passwordField.getBackground().draw(batch);
			rememberMeCheckBox.show(batch);
			forgotPasswordButton.show(batch);
			createNewAccButton.show(batch);
			exitButton.show(batch);
			if (alpha == 1.0f) {
				loginField.getTextField().setVisible(true);
				passwordField.getTextField().setVisible(true);
			}
			batch.end();
			if (isSignUp) {
				if (alpha == 0.0f) {
					signUpForm = new SignUpForm(signInLabel.getFont(), loginLabel.getFont(),
							loginField.getTextField().getStyle(), backgroundSignIn, fieldTexture, fieldFocusedTexture,
							signInButton, fieldErrorTexture);
					isSignUp = false;
				}
			} else if (isExit) {
				if (alpha == 0.0f) {
					if (exitFromGameForm == null)
						exitFromGameForm = new ExitFromGameForm();
					isExit = false;
				}
			} else if (isPasswordRecovery) {
				if (alpha == 0.0f) {
					if (passwordRecoveryForm == null)
						passwordRecoveryForm = new PasswordRecoveryForm(loginField.getTextField().getStyle(),
								fieldTexture, fieldFocusedTexture, signInButton.getLabelBitmapFont(),
								signInButton.getLabelFocusedBitmapFont(), signInButton.getTexture(),
								signInButton.getTextureFocused(), signInButton.getTextureOver());
					isPasswordRecovery = false;
				}
			}
			if (!isHide && !isLoading) {
				if (signInButton.isReleased()) {
					signIn();
					if (signInFailForm != null) {
						signInFailForm.dispose();
						signInFailForm = null;
					}
				} else if (createNewAccButton.isReleased()) {
					setObjectsDisable(true);
					loginField.getTextField().setVisible(false);
					passwordField.getTextField().setVisible(false);
					if (signUpForm == null) {
						isHide = true;
						isSignUp = true;
					}
					if (signInFailForm != null) {
						signInFailForm.dispose();
						signInFailForm = null;
					}
				} else if (exitButton.isReleased()) {
					setObjectsDisable(true);
					loginField.getTextField().setVisible(false);
					passwordField.getTextField().setVisible(false);
					isHide = true;
					isExit = true;
					if (signInFailForm != null) {
						signInFailForm.dispose();
						signInFailForm = null;
					}
				} else if (forgotPasswordButton.isReleased()) {
					setObjectsDisable(true);
					loginField.getTextField().setVisible(false);
					passwordField.getTextField().setVisible(false);
					isPasswordRecovery = true;
					isHide = true;
					if (signInFailForm != null) {
						signInFailForm.dispose();
						signInFailForm = null;
					}
				} else if (Gdx.input.isKeyPressed(Keys.ENTER)) {
					signIn();
					if (signInFailForm != null) {
						signInFailForm.dispose();
						signInFailForm = null;
					}
				}
			}
		} else if (signUpForm != null) {
			signUpForm.show(batch);
			if (signUpForm.isCancel() || signUpForm.isRegistered()) {
				signUpForm.hide();
				if (signUpForm.getAlpha() == 0.0f) {
					signUpForm.dispose();
					signUpForm = null;
					isHide = false;
					setObjectsDisable(false);
				}
			}
		} else if (exitFromGameForm != null) {
			exitFromGameForm.show(batch);
			if (exitFromGameForm.isCancel()) {
				exitFromGameForm.hide();
				if (exitFromGameForm.getAlpha() == 0.0f) {
					exitFromGameForm.dispose();
					exitFromGameForm = null;
					exitButton.reset();
					isHide = false;
					setObjectsDisable(false);
				}
			} else if (exitFromGameForm.isExit()) {
				exitFromGameForm.dispose();
				exitFromGameForm = null;
				Gdx.app.exit();
			}
		} else if (passwordRecoveryForm != null) {
			passwordRecoveryForm.show(batch);
			if (passwordRecoveryForm.isCancel()) {
				passwordRecoveryForm.hide();
				if (passwordRecoveryForm.getAlpha() == 0.0f) {
					passwordRecoveryForm.dispose();
					passwordRecoveryForm = null;
					forgotPasswordButton.reset();
					isHide = false;
					setObjectsDisable(false);
				}
			}
		} else if (confirmationForm != null) {
			confirmationForm.show(batch);
			if (confirmationForm.isOk()) {
				Client.getInstance().confirmCodeAcc(confirmationForm.getCode());
				confirmationForm.reset();
				isConfirmationCodeAcc = true;
				isLoading = true;
				loading.setText("Перевірка...");
				if (confirmationFailedForm != null) {
					confirmationFailedForm.dispose();
					confirmationFailedForm = null;
				}
			} else if (confirmationForm.isCancel()) {
				confirmationForm.dispose();
				confirmationForm = null;
				setObjectsDisable(false);
				loginField.getTextField().setVisible(false);
				passwordField.getTextField().setVisible(false);
				alpha = 0.0f;
				if (confirmationFailedForm != null) {
					confirmationFailedForm.dispose();
					confirmationFailedForm = null;
				}
			}
			if (confirmationFailedForm != null) {
				confirmationFailedForm.show(batch);
			}
		}
		if (signInFailForm != null)
			signInFailForm.show(batch);
		if (signInButton.isPressed() || exitButton.isPressed() || forgotPasswordButton.isPressed()
				|| createNewAccButton.isPressed() || rememberMeCheckBox.isPressed()) {
			stage.setKeyboardFocus(null);
		}
		if (signUpForm == null && exitFromGameForm == null && confirmationForm == null
				&& passwordRecoveryForm == null) {
			stage.act();
			stage.draw();
		}
		if (isLoading) {
			loading.show(batch);
		}
		if (isSignIn) {
			if (Client.getInstance().getServerListener() != null) {
				if (Client.getInstance().getServerListener().getObjects().size() > 0) {
					Object object = Client.getInstance().getServerListener().getObjects().pop();
					if (object.getClass() == SignInAnswer.class) {
						SignInAnswer answer = (SignInAnswer) object;
						if (answer.getValue() == false) {
							signInFailForm = new SignInFailForm();
							loginField.getBackground().setRegion(fieldErrorTexture);
							passwordField.getBackground().setRegion(fieldErrorTexture);
							setObjectsDisable(false);
							isLoading = false;
						} else {
							Settings.getInstance().setName(answer.getName());
							if (rememberMeCheckBox.isChecked()) {
								Settings.getInstance().setRememberAccount(true);
								Settings.getInstance().setLogin(loginField.getTextField().getText().trim());
								Settings.getInstance().setPassword(passwordField.getTextField().getText().trim());
							}
							if (!answer.isConfirmed()) {
								confirmationForm = new ConfirmationForm();
								setObjectsDisable(true);
								loginField.getTextField().setVisible(false);
								passwordField.getTextField().setVisible(false);
								isLoading = false;
							} else {
								Client.getInstance().sendQuery("modules");
								isModules = true;
								loading.setText("Загрузка модулів...");
							}
						}
						isSignIn = false;
					}
				}
			}
		} else if (isConfirmationCodeAcc) {
			if (Client.getInstance().getServerListener() != null) {
				if (Client.getInstance().getServerListener().getObjects().size() > 0) {
					Object object = Client.getInstance().getServerListener().getObjects().pop();
					if (object.getClass() == ConfirmationCodeAccAnswer.class) {
						ConfirmationCodeAccAnswer confirmationCodeAccAnswer = (ConfirmationCodeAccAnswer) object;
						if (confirmationCodeAccAnswer.getValue()) {
							confirmationForm.dispose();
							confirmationForm = null;
							isConfirmationCodeAcc = false;
							isModules = true;
							Client.getInstance().sendQuery("modules");
							loading.setText("Загрузка модулів...");
						} else {
							confirmationForm.setErrorField();
							confirmationFailedForm = new ConfirmationFailedForm();
						}
					}
				}
			}
		} else if (isModules) {
			if (Client.getInstance().getServerListener() != null) {
				if (Client.getInstance().getServerListener().getObjects().size() > 0) {
					final Object object = Client.getInstance().getServerListener().getObjects().pop();
					if (object.getClass() == ModulesAnswer.class) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								ModulesAnswer modulesAnswer = (ModulesAnswer) object;
								Array<technique.Corps> corpses = new Array<technique.Corps>(
										modulesAnswer.getCorpses().size());
								for (int k = 0; k < 1; k++) {
									for (int i = 0; i < modulesAnswer.getCorpses().size(); i++) {
										technique.Corps corps = new technique.Corps(
												modulesAnswer.getCorpses().get(i).getId());
										corps.setPosition(modulesAnswer.getCorpses().get(i).getPosition());
										corps.setLevel(modulesAnswer.getCorpses().get(i).getLevel());
										corps.setExperience(modulesAnswer.getCorpses().get(i).getExperience());
										corps.setMoney(modulesAnswer.getCorpses().get(i).getMoney());
										corps.setSpeedRotation(modulesAnswer.getCorpses().get(i).getSpeedRotation());
										corps.setWidth(modulesAnswer.getCorpses().get(i).getWidth());
										corps.setHeight(modulesAnswer.getCorpses().get(i).getHeight());
										corps.setAvailable(modulesAnswer.getCorpses().get(i).isAvailable());
										corps.setExplored(modulesAnswer.getCorpses().get(i).isExplored());
										corps.setBought(modulesAnswer.getCorpses().get(i).isBought());
										corps.setName(modulesAnswer.getCorpses().get(i).getName());
										if (modulesAnswer.getCorpses().get(i).getTechniqueType().equals("tank")) {
											corps.setTechniqueType(TechniqueType.TANK);
										} else if (modulesAnswer.getCorpses().get(i).getTechniqueType()
												.equals("arty")) {
											corps.setTechniqueType(TechniqueType.ARTY);
										} else if (modulesAnswer.getCorpses().get(i).getTechniqueType()
												.equals("reactive_system")) {
											corps.setTechniqueType(TechniqueType.REACTIVE_SYSTEM);
										} else if (modulesAnswer.getCorpses().get(i).getTechniqueType()
												.equals("flamethrower_system")) {
											corps.setTechniqueType(TechniqueType.FLAMETHROWER_SYSTEM);
										}
										corps.setMaxHealth(modulesAnswer.getCorpses().get(i).getMaxHealth());
										corps.setMaxSpeed(modulesAnswer.getCorpses().get(i).getMaxSpeed());
										corps.setAcceleration(modulesAnswer.getCorpses().get(i).getAcceleration());
										for (int l = 0; l < 1; l++) {
											for (int j = 0; j < modulesAnswer.getCorpses().get(i).getTowers()
													.size(); j++) {
												technique.Tower tower = new technique.Tower(
														modulesAnswer.getCorpses().get(i).getTowers().get(j).getId());
												tower.setPosition(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getPosition());
												tower.setLevel(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getLevel());
												tower.setExperience(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getExperience());
												tower.setMoney(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getMoney());
												tower.setSpeedRotation(modulesAnswer.getCorpses().get(i).getTowers()
														.get(j).getSpeedRotation());
												tower.setWidth(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getWidth());
												tower.setHeight(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getHeight());
												tower.setAvailable(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.isAvailable());
												tower.setExplored(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.isExplored());
												tower.setBought(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.isBought());
												tower.setName(
														modulesAnswer.getCorpses().get(i).getTowers().get(j).getName());
												tower.setTimeReduction(modulesAnswer.getCorpses().get(i).getTowers()
														.get(j).getTimeReduction());
												tower.setMinDamage(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getMinDamage());
												tower.setMaxDamage(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getMaxDamage());
												tower.setMinRadius(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getMinRadius());
												tower.setMaxRadius(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getMaxRadius());
												tower.setRotationLeft(modulesAnswer.getCorpses().get(i).getTowers()
														.get(j).getRotationLeft());
												tower.setRotationRight(modulesAnswer.getCorpses().get(i).getTowers()
														.get(j).getRotationRight());
												tower.setTimeRecharge(modulesAnswer.getCorpses().get(i).getTowers()
														.get(j).getTimeRecharge());
												tower.setX(modulesAnswer.getCorpses().get(i).getTowers().get(j).getX());
												tower.setY(modulesAnswer.getCorpses().get(i).getTowers().get(j).getY());
												tower.setOriginX(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getOriginX());
												tower.setOriginY(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getOriginY());
												tower.setShellsTotal(modulesAnswer.getCorpses().get(i).getTowers()
														.get(j).getShellsTotal());
												Gun gun = new Gun();
												gun.setShellSpeed(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getShellSpeed());
												if (modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getShootingStyle() != null) {
													ShootingStyle shootingStyle = null;
													if (modulesAnswer.getCorpses().get(i).getTowers().get(j)
															.getShootingStyle().equals("in_turn")) {
														shootingStyle = ShootingStyle.IN_TURN;
													} else if (modulesAnswer.getCorpses().get(i).getTowers().get(j)
															.getShootingStyle().equals("together")) {
														shootingStyle = ShootingStyle.TOGETHER;
													} else if (modulesAnswer.getCorpses().get(i).getTowers().get(j)
															.getShootingStyle().equals("random")) {
														shootingStyle = ShootingStyle.RANDOM;
													}
													gun.setCassette(
															modulesAnswer.getCorpses().get(i).getTowers().get(j)
																	.getShellsCassette(),
															modulesAnswer.getCorpses().get(i).getTowers().get(j)
																	.getRechargeShell(),
															shootingStyle);
												}
												gun.setShellWidth(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getShellWidth());
												gun.setShellHeight(modulesAnswer.getCorpses().get(i).getTowers().get(j)
														.getShellHeight());
												tower.setGun(gun);
												corps.getTowers().add(tower);
											}
										}
										corpses.add(corps);
									}
								}
								Objects.getInstance().setCorpses(corpses);
								isModules = false;
								isLogin = true;
							}
						}).start();
					}
				}
			}
		}
		if (Client.getInstance().getServerListener() != null) {
			if (!Client.getInstance().getServerListener().getObjects().isEmpty()) {
				final Object object = Client.getInstance().getServerListener().getObjects().peek();
				if (object.getClass() == RSAKey.class) {
					Client.getInstance().getServerListener().getObjects().remove(object);
					RSA.getInstance().createKey(((RSAKey) object).getData());
				}
			}
		}
	}

	public boolean isLogin() {
		return isLogin;
	}

	private void setObjectsDisable(boolean value) {
		loginField.getTextField().setDisabled(value);
		passwordField.getTextField().setDisabled(value);
		exitButton.setDisable(value);
		forgotPasswordButton.setDisable(value);
		createNewAccButton.setDisable(value);
		rememberMeCheckBox.setDisable(value);
		signInButton.setDisable(value);
	}

	@Override
	public void dispose() {
		background.getTexture().dispose();
		fieldTexture.dispose();
		fieldFocusedTexture.dispose();
		fieldErrorTexture.dispose();
		logo.getTexture().dispose();
		exitButton.getTexture().dispose();
		exitButton.getTextureFocused().dispose();
		exitButton.getTextureOver().dispose();
		loginField.getTextField().remove();
		passwordField.getTextField().remove();
		forgotPasswordButton.dispose();
		createNewAccButton.dispose();
		rememberMeCheckBox.getTexture().dispose();
		rememberMeCheckBox.getTextureFocused().dispose();
		rememberMeCheckBox.getTextureOver().dispose();
		rememberMeCheckBox.getCheckMark().dispose();
		loginLabel.getFont().dispose();
		signInLabel.getFont().dispose();
		loading.dispose();
		stage.clear();
		inputMultiplexer.removeProcessor(stage);
	}
}