package techniqueEditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.game.Editor;
import com.mygdx.game.OpeningWindow;

import technique.Controller;
import technique.Model;
import technique.ShootingStyle;
import technique.TechniqueType;
import technique.View;
import util.Font;
import util.TextureCreator;

public class TechniqueProperties implements Disposable {
	private TechniqueWorkspace workspace;
	private Sprite background;
	private FreeTypeFontParameter freeTypeFontParameter;
	private BitmapFont bitmapFont;
	private BitmapFontCache propertiesLabel;
	private TextButtonStyle textButtonStyle;
	private TextButton bodyButton;
	private TextButton towerButton;
	private ViewTowersWindow viewTowersWindow;
	private BlockBodyData blockBodyData;
	private BlockTowerData blockTowerData;
	private TextFieldStyle textFieldStyle;
	private TextButton saveButton;
	private TextButton applyButton;
	private TextButton changeTextureButton;
	private TextButton firstPageButton;
	private TextButton secondPageButton;
	private OpeningWindow textureOpeningWindow;
	private OpeningWindow saveModelWindow;
	private OpeningWindow openSoundShotWindow;
	private OpeningWindow openSoundHitWindow;
	private TextButton pointsButton;
	private Array <GunPoint> gunPoints;
	private GunPoint currentGunPoint;
	private PointsWindow pointsWindow;
	private TextButton disablePointsButton;
	private TextButton addPointButton;
	private TextButton testingButton;
	private TextButton completeTestingButton;
	private TextButton addSoundShotButton;
	private TextButton addSoundHitButton;
	private TextButton shellButton;
	private Sound sound;
	private Model model;
	private View view;
	private Controller controller;
	private ShellPropertiesWindow shellPropertiesWindow;
	
	private boolean isTechnique = true;
	private boolean isPoint = false;
	private boolean isTesting = false;
	
	public TechniqueProperties (final TechniqueWorkspace workspace, final Stage stage, Skin skin, final TextButton backButton, final TextButton insertTowerButton) {
		this.workspace = workspace;
		
		background = new Sprite(TextureCreator.createTexture(Color.GRAY));
		background.setSize(500, 700);
		background.setPosition(Editor.getCamera().viewportWidth - background.getWidth() - 10, 10);
	
		freeTypeFontParameter = new FreeTypeFontParameter();
		freeTypeFontParameter.size = 20;
		
		bitmapFont = Font.getFont().generateBitmapFont(freeTypeFontParameter);
		
		propertiesLabel = new BitmapFontCache(bitmapFont);
		propertiesLabel.setText("Properties", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Properties") / 2, background.getVertices()[Batch.Y2] - 5);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("button");
		textButtonStyle.over = skin.getDrawable("buttonActivated");
		textButtonStyle.font = bitmapFont;
		textButtonStyle.fontColor = Color.WHITE;
		
		textFieldStyle = new TextFieldStyle();
		textFieldStyle.background = skin.getDrawable("field");
		textFieldStyle.cursor = skin.getDrawable("cursor");
		textFieldStyle.selection = skin.getDrawable("selection");
		textFieldStyle.font = bitmapFont;
		textFieldStyle.fontColor = Color.WHITE;
		
		bodyButton = new TextButton("Body", textButtonStyle);
		bodyButton.setSize(130, 40);
		bodyButton.setPosition(background.getX() + 10, background.getVertices()[Batch.Y2] - 70);
		bodyButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= bodyButton.getX() && cursor.x <= bodyButton.getX() + bodyButton.getWidth()
				&& cursor.y >= bodyButton.getY() && cursor.y <= bodyButton.getY() + bodyButton.getHeight()) {
					if (!isTechnique) {
						blockTowerData.hide();
						blockBodyData.resume();				
						isTechnique = true;					
						firstPageButton.setVisible(false);
						secondPageButton.setVisible(false);
						pointsButton.setVisible(false);
						disablePointsButton.setVisible(false);
						addPointButton.setVisible(false);
					}
				}
			}
		});
		
		towerButton = new TextButton("Towers", textButtonStyle);
		towerButton.setSize(130, 40);
		towerButton.setPosition(background.getX() + background.getWidth() - towerButton.getWidth() - 10, background.getVertices()[Batch.Y2] - 70);
		towerButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}		
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= towerButton.getX() && cursor.x <= towerButton.getX() + towerButton.getWidth()
				&& cursor.y >= towerButton.getY() && cursor.y <= towerButton.getY() + towerButton.getHeight()) {
					if (workspace.getBlockTowers().size == 1) {
						if (isTechnique) {
							blockBodyData.hide();
							blockTowerData.resume();
							isTechnique = false;
						}
						workspace.setCurrentBlockTower(workspace.getBlockTowers().get(0));
						firstPageButton.setVisible(true);
						secondPageButton.setVisible(true);
						pointsButton.setVisible(true);
						disablePointsButton.setVisible(true);
						addPointButton.setVisible(true);
						blockTowerData.setFirstPage();
					} else {
						if (viewTowersWindow == null) {
							viewTowersWindow = new ViewTowersWindow(workspace, textButtonStyle, stage);
						}
					}
				}
			}
		});
		
		saveButton = new TextButton("Save", textButtonStyle);
		saveButton.setSize(130, 40);
		saveButton.setPosition(50, 80);
		saveButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= saveButton.getX() && cursor.x <= saveButton.getX() + saveButton.getWidth()
				&& cursor.y >= saveButton.getY() && cursor.y <= saveButton.getY() + saveButton.getHeight()) {
					if (saveModelWindow == null) {
						saveModelWindow = new OpeningWindow(bitmapFont, textButtonStyle, textFieldStyle, stage);
					}
				}
			}
		});
		
		applyButton = new TextButton("Apply", textButtonStyle);
		applyButton.setSize(130, 40);
		applyButton.setPosition(saveButton.getX() + saveButton.getWidth() + 10, saveButton.getY());
		applyButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= applyButton.getX() && cursor.x <= applyButton.getX() + applyButton.getWidth()
				&& cursor.y >= applyButton.getY() && cursor.y <= applyButton.getY() + applyButton.getHeight()) {
					if (isTechnique) {
						workspace.getBlockBody().setPosition(blockBodyData.getPosition());
						//workspace.getBlockBody().setX(blockBodyData.getX());
						//workspace.getBlockBody().setY(blockBodyData.getY());
						workspace.getBlockBody().setWidth(blockBodyData.getWidth());
						workspace.getBlockBody().setHeight(blockBodyData.getHeight());
						workspace.getBlockBody().setMoney(blockBodyData.getMoney());
						workspace.getBlockBody().setSpeedRotation(blockBodyData.getSpeedRotation());
						workspace.getBlockBody().setName(blockBodyData.getName());
						if (blockBodyData.getTechniqueType().toLowerCase().equals("tank")) {
							workspace.getBlockBody().setTechniqueType(TechniqueType.TANK);
						} else if (blockBodyData.getTechniqueType().toLowerCase().equals("arty")) {
							workspace.getBlockBody().setTechniqueType(TechniqueType.ARTY);
						} else if (blockBodyData.getTechniqueType().toLowerCase().equals("reactive_system")) {
							workspace.getBlockBody().setTechniqueType(TechniqueType.REACTIVE_SYSTEM);
						} else if (blockBodyData.getTechniqueType().toLowerCase().equals("flamethrower_system")) {
							workspace.getBlockBody().setTechniqueType(TechniqueType.FLAMETHROWER_SYSTEM);
						} else {
							System.out.println("Error technique type");
						}
						workspace.getBlockBody().setMaxSpeed(blockBodyData.getMaxSpeed());
						workspace.getBlockBody().setAcceleration(blockBodyData.getAcceleration());
						workspace.getBlockBody().setMaxHealth(blockBodyData.getMaxHealth());
					} else {
						workspace.getCurrentBlockTower().setName(blockTowerData.getName());
						workspace.getCurrentBlockTower().setPosition(blockTowerData.getPosition());
						workspace.getCurrentBlockTower().setX(workspace.getBackogrund().getX() + blockTowerData.getX());
						workspace.getCurrentBlockTower().setY(workspace.getBackogrund().getY() + blockTowerData.getY());
						workspace.getCurrentBlockTower().setWidth(blockTowerData.getWidth());
						workspace.getCurrentBlockTower().setHeight(blockTowerData.getHeight());
						workspace.getCurrentBlockTower().setTimeReduction(blockTowerData.getTimeReduction());
						workspace.getCurrentBlockTower().setMinRadius(blockTowerData.getMinRadius());
						workspace.getCurrentBlockTower().setMaxRadius(blockTowerData.getMaxRadius());
						workspace.getCurrentBlockTower().setRotationLeft(blockTowerData.getRotationLeft());
						workspace.getCurrentBlockTower().setRotationRight(blockTowerData.getRotationRight());
						workspace.getCurrentBlockTower().setXOrigin(blockTowerData.getXOrigin());
						workspace.getCurrentBlockTower().setYOrigin(blockTowerData.getYOrigin());
						workspace.getCurrentBlockTower().setTimeRecharge(blockTowerData.getTimeRecharge());
						workspace.getCurrentBlockTower().setTimeRechargeShellCassette(blockTowerData.getTimeRechargeShellCassette());
						workspace.getCurrentBlockTower().setExperience(blockTowerData.getExperience());
						workspace.getCurrentBlockTower().setMinDamage(blockTowerData.getMinDamage());
						workspace.getCurrentBlockTower().setMaxDamage(blockTowerData.getMaxDamage());
						workspace.getCurrentBlockTower().setTotalShells(blockTowerData.getTotalShells());
						workspace.getCurrentBlockTower().setTotalShellsCassette(blockTowerData.getTotalShellsCassette());
						if (blockTowerData.getShootingStyle().toLowerCase().equals("together")) {
							workspace.getCurrentBlockTower().setShootingStyle(ShootingStyle.TOGETHER);
						} else if (blockTowerData.getShootingStyle().toLowerCase().equals("in_turn")) {
							workspace.getCurrentBlockTower().setShootingStyle(ShootingStyle.IN_TURN);
						} else if (blockTowerData.getShootingStyle().toLowerCase().equals("random")) {
							workspace.getCurrentBlockTower().setShootingStyle(ShootingStyle.RANDOM);
						} else {
							System.out.println("Error shooting style");
						}
						workspace.getCurrentBlockTower().setSpeedRotation(blockTowerData.getSpeedRotation());
					}
				}
			}
		});
		
		changeTextureButton = new TextButton("Change texture", textButtonStyle);
		changeTextureButton.setSize(140, 40);
		changeTextureButton.setPosition(applyButton.getX() + applyButton.getWidth() + 10, applyButton.getY());
		changeTextureButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= changeTextureButton.getX() && cursor.x <= changeTextureButton.getX() + changeTextureButton.getWidth()
				&& cursor.y >= changeTextureButton.getY() && cursor.y <= changeTextureButton.getY() + changeTextureButton.getHeight()) {
					if (textureOpeningWindow == null) {
						textureOpeningWindow = new OpeningWindow(bitmapFont, textButtonStyle, textFieldStyle, stage);
					}
				}
			}
		});
		
		firstPageButton = new TextButton("1", textButtonStyle);
		firstPageButton.setVisible(false);
		firstPageButton.setSize(30, 40);
		firstPageButton.setPosition(bodyButton.getX() + bodyButton.getWidth() + 40, bodyButton.getY());
		firstPageButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= firstPageButton.getX() && cursor.x <= firstPageButton.getX() + firstPageButton.getWidth()
				&& cursor.y >= firstPageButton.getY() && cursor.y <= firstPageButton.getY() + firstPageButton.getHeight()) {
					blockTowerData.setFirstPage();
				}
			}
		});
		
		secondPageButton = new TextButton("2", textButtonStyle);
		secondPageButton.setVisible(false);
		secondPageButton.setSize(30, 40);
		secondPageButton.setPosition(firstPageButton.getX() + firstPageButton.getWidth() + 10, firstPageButton.getY());
		secondPageButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= secondPageButton.getX() && cursor.x <= secondPageButton.getX() + secondPageButton.getWidth()
				&& cursor.y >= secondPageButton.getY() && cursor.y <= secondPageButton.getY() + secondPageButton.getHeight()) {
					blockTowerData.setSecondPage();
				}
			}
		});
		
		gunPoints = new Array <GunPoint>(1);
		
		pointsButton = new TextButton("Points", textButtonStyle);
		pointsButton.setVisible(false);
		pointsButton.setSize(140, 40);
		pointsButton.setPosition(changeTextureButton.getX() + changeTextureButton.getWidth() + 10, changeTextureButton.getY());
		pointsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= pointsButton.getX() && cursor.x <= pointsButton.getX() + pointsButton.getWidth()
				&& cursor.y >= pointsButton.getY() && cursor.y <= pointsButton.getY() + pointsButton.getHeight()) {
					if (pointsWindow == null) {
						if (gunPoints.size > 1) {
							pointsWindow = new PointsWindow(stage, textButtonStyle, gunPoints);
						} else if (gunPoints.size == 1) {
							currentGunPoint = gunPoints.get(0);
							isPoint = true;
						}
					}	
				}
			}
		});
		
		disablePointsButton = new TextButton("Disable points", textButtonStyle);
		disablePointsButton.setVisible(false);
		disablePointsButton.setSize(140, 40);
		disablePointsButton.setPosition(pointsButton.getX() + pointsButton.getWidth() + 10, pointsButton.getY());
		disablePointsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= disablePointsButton.getX() && cursor.x <= disablePointsButton.getX() + disablePointsButton.getWidth()
				&& cursor.y >= disablePointsButton.getY() && cursor.y <= disablePointsButton.getY() + disablePointsButton.getHeight()) {
					isPoint = false;
				}
			}
		});
		
		addPointButton = new TextButton("Add point", textButtonStyle);
		addPointButton.setVisible(false);
		addPointButton.setSize(130, 40);
		addPointButton.setPosition(saveButton.getX(), saveButton.getY() - saveButton.getHeight() - 5);
		addPointButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= addPointButton.getX() && cursor.x <= addPointButton.getX() + addPointButton.getWidth()
				&& cursor.y >= addPointButton.getY() && cursor.y <= addPointButton.getY() + addPointButton.getHeight()) {
					gunPoints.add(new GunPoint(workspace.getBackogrund().getX(), workspace.getBackogrund().getY()));
					isPoint = true;
				}
			}
		});
		
		testingButton = new TextButton("Testing", textButtonStyle);
		testingButton.setSize(130, 40);
		testingButton.setPosition(200, Editor.getCamera().viewportHeight - testingButton.getHeight() - 10);
		testingButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= testingButton.getX() && cursor.x <= testingButton.getX() + testingButton.getWidth()
				&& cursor.y >= testingButton.getY() && cursor.y <= testingButton.getY() + testingButton.getHeight()) {
					model = new Model(workspace.getBlockBody().getBackground().getTexture(), workspace.getCurrentBlockTower().getBackground().getTexture());
					model.getBody().setSize(workspace.getBlockBody().getBackground().getWidth(), workspace.getBlockBody().getBackground().getHeight());
					model.getBody().setMaxSpeed(workspace.getBlockBody().getMaxSpeed());
					model.getBody().setAcceleration(workspace.getBlockBody().getAcceleration());
					model.getBody().setSpeedRotation(workspace.getBlockBody().getSpeedRotation());
					model.getBody().setOriginCenter();
					
					/*model = new Model(new Texture(Gdx.files.internal("textures/corpus.png")), new Texture(Gdx.files.internal("textures/tower.png")));
					model.getBody().setSize(574, 381);
					model.getBody().setMaxSpeed(50);
					model.getBody().setAcceleration(50);
					model.getBody().setSpeedRotation(200);
					model.getBody().setOriginCenter();
					model.getBody().setPosition(0, 0);
					model.getBody().getCenterPoint().set(model.getBody().getWidth() / 2, model.getBody().getHeight() / 2);*/
					
					model.getTower().setSize(workspace.getCurrentBlockTower().getBackground().getWidth(), workspace.getCurrentBlockTower().getBackground().getHeight());
					model.getTower().setSpeedRotation(workspace.getCurrentBlockTower().getSpeedRotation());
					model.getTower().getCenterPoint().set(workspace.getCurrentBlockTower().getX() - workspace.getBlockBody().getX(),
							workspace.getCurrentBlockTower().getY() - workspace.getBlockBody().getY());
					model.getTower().setOrigin(workspace.getCurrentBlockTower().getXOrigin(), workspace.getCurrentBlockTower().getYOrigin());
					model.getTower().setPosition(model.getTower().getCenterPoint().x, model.getTower().getCenterPoint().y);
					
					/*model.getTower().setSize(150, 50);
					model.getTower().setSpeedRotation(500);
					model.getTower().getCenterPoint().set(workspace.getCurrentBlockTower().getX() - workspace.getBlockBody().getX() + workspace.getCurrentBlockTower().getXOrigin(),
							workspace.getCurrentBlockTower().getY() - workspace.getBlockBody().getY() + workspace.getCurrentBlockTower().getYOrigin());
					model.getTower().setOrigin(workspace.getCurrentBlockTower().getXOrigin(), workspace.getCurrentBlockTower().getYOrigin());
					model.getTower().setPosition(model.getTower().getCenterPoint().x - model.getTower().getOriginX(), model.getTower().getCenterPoint().y - model.getTower().getOriginY());*/
					
					Array <Vector2> points = new Array <Vector2>(gunPoints.size);
					
					for (GunPoint point : gunPoints) {
						float tempX = point.getBackground().getX() - workspace.getBlockBody().getX();
						float tempY = point.getBackground().getY() + point.getBackground().getHeight() / 2 - workspace.getBlockBody().getY();
						points.add(new Vector2(tempX, tempY));
					}
					
					try {
						model.createShooting(workspace.getCurrentBlockTower().getTotalShells(), points, new Texture(Gdx.files.absolute(shellPropertiesWindow.getTexture())));
						model.createShooting(100, points, new Texture(Gdx.files.internal("textures/shell.png")));
					} catch (GdxRuntimeException e) {
						e.printStackTrace();
					}
					
					model.getShooting().setSound(sound);
					model.getShooting().setShootingStyle(workspace.getCurrentBlockTower().getShootingStyle());
					model.getShooting().setSpeed(workspace.getCurrentBlockTower().getSpeedRotation());
					model.getShooting().setTimeRecharge(workspace.getCurrentBlockTower().getTimeRecharge());
					
					if (workspace.getCurrentBlockTower().getTimeRechargeShellCassette() > 0.0f) {
						model.getShooting().setShootingStyle(ShootingStyle.IN_TURN);
						model.getShooting().setCassette(workspace.getCurrentBlockTower().getTotalShellsCassette(), workspace.getCurrentBlockTower().getTimeRechargeShellCassette());
						//model.getShooting().setCassette(5, 0.5f);
					}
					
					view = new View(model);
					controller = new Controller(model);
					
					isTesting = true;
					isPoint = false;
					
					workspace.setVisible(false);
					backButton.setVisible(false);
					insertTowerButton.setVisible(false);
					testingButton.setVisible(false);
					saveButton.setVisible(false);
					applyButton.setVisible(false);
					bodyButton.setVisible(false);
					towerButton.setVisible(false);
					changeTextureButton.setVisible(false);
					completeTestingButton.setVisible(true);
					shellButton.setVisible(false);
					addSoundShotButton.setVisible(false);
					blockTowerData.hide();
					if (!isTechnique) {
						firstPageButton.setVisible(false);
						secondPageButton.setVisible(false);
						pointsButton.setVisible(false);
						disablePointsButton.setVisible(false);
						addPointButton.setVisible(false);
					} else blockBodyData.hide();
				}
			}
		});
		
		completeTestingButton = new TextButton("Complete testing", textButtonStyle);
		completeTestingButton.setVisible(false);
		completeTestingButton.setSize(150, 40);
		completeTestingButton.setPosition(Editor.getCamera().viewportWidth - completeTestingButton.getWidth() - 10, Editor.getCamera().viewportHeight - completeTestingButton.getHeight() - 10);
		completeTestingButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= completeTestingButton.getX() && cursor.x <= completeTestingButton.getX() + completeTestingButton.getWidth()
				&& cursor.y >= completeTestingButton.getY() && cursor.y <= completeTestingButton.getY() + completeTestingButton.getHeight()) {
					model = null;
					view = null;
					controller = null;
					
					isTesting = false;
					isPoint = true;
					
					workspace.setVisible(true);
					backButton.setVisible(true);
					insertTowerButton.setVisible(true);
					testingButton.setVisible(true);
					saveButton.setVisible(true);
					applyButton.setVisible(true);
					bodyButton.setVisible(true);
					towerButton.setVisible(true);
					changeTextureButton.setVisible(true);
					completeTestingButton.setVisible(false);
					shellButton.setVisible(true);
					addSoundShotButton.setVisible(true);
					blockTowerData.resume();
					if (!isTechnique) {
						firstPageButton.setVisible(true);
						secondPageButton.setVisible(true);
						pointsButton.setVisible(true);
						disablePointsButton.setVisible(true);
						addPointButton.setVisible(true);
					} else {
						blockBodyData.resume();
					}
				}
			}
		});
		
		addSoundShotButton = new TextButton("Add shot", textButtonStyle);
		addSoundShotButton.setSize(changeTextureButton.getWidth(), 40);
		addSoundShotButton.setPosition(changeTextureButton.getX(), addPointButton.getY());
		addSoundShotButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= addSoundShotButton.getX() && cursor.x <= addSoundShotButton.getX() + addSoundShotButton.getWidth()
				&& cursor.y >= addSoundShotButton.getY() && cursor.y <= addSoundShotButton.getY() + addSoundShotButton.getHeight()) {
					if (openSoundShotWindow == null) {
						openSoundShotWindow = new OpeningWindow(bitmapFont, textButtonStyle, textFieldStyle, stage);	
					}
				}
			}
		});
		
		addSoundHitButton = new TextButton("Add hit", textButtonStyle);
		addSoundHitButton.setSize(addSoundShotButton.getWidth(), 40);
		addSoundHitButton.setPosition(addSoundShotButton.getX() + addSoundShotButton.getWidth(), addSoundShotButton.getY());
		addSoundHitButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= addSoundHitButton.getX() && cursor.x <= addSoundHitButton.getX() + addSoundHitButton.getWidth()
				&& cursor.y >= addSoundHitButton.getY() && cursor.y <= addSoundHitButton.getY() + addSoundHitButton.getHeight()) {
					if (openSoundHitWindow == null) {
						openSoundHitWindow = new OpeningWindow(bitmapFont, textButtonStyle, textFieldStyle, stage);	
					}
				}
			}
		});
		
		shellButton = new TextButton("Shell", textButtonStyle);
		shellButton.setSize(applyButton.getWidth(), 40);
		shellButton.setPosition(applyButton.getX(), addSoundShotButton.getY());
		shellButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= shellButton.getX() && cursor.x <= shellButton.getX() + shellButton.getWidth()
				&& cursor.y >= shellButton.getY() && cursor.y <= shellButton.getY() + shellButton.getHeight()) {
					if (!shellPropertiesWindow.isVisible()) {
						shellPropertiesWindow.resume();	
					}
				}
			}
		});
		
		stage.addActor(bodyButton);
		stage.addActor(towerButton);
		stage.addActor(saveButton);
		stage.addActor(applyButton);
		stage.addActor(changeTextureButton);
		stage.addActor(firstPageButton);
		stage.addActor(secondPageButton);
		stage.addActor(pointsButton);
		stage.addActor(disablePointsButton);
		stage.addActor(addPointButton);
		stage.addActor(testingButton);
		stage.addActor(completeTestingButton);
		stage.addActor(addSoundShotButton);
		stage.addActor(shellButton);
		stage.addActor(addSoundHitButton);
		
		blockBodyData = new BlockBodyData(bitmapFont, textFieldStyle, background.getX() + 5, background.getY() + background.getHeight(), stage);
		
		blockTowerData = new BlockTowerData(bitmapFont, textFieldStyle, background.getX() + 5, background.getY() + background.getHeight(), stage);
		blockTowerData.hide();
		
		shellPropertiesWindow = new ShellPropertiesWindow(bitmapFont, stage, textFieldStyle, textButtonStyle);
	}
	
	public void show (SpriteBatch batch) {
		if (isTesting) {
			controller.act();
			view.draw(batch);
		} else {
			background.draw(batch);
			propertiesLabel.draw(batch);	
				
			if (isTechnique) {
				blockBodyData.show(batch);
			} else {
				blockTowerData.show(batch);
			}		
		}
		
		if (isPoint && currentGunPoint != null) {
			currentGunPoint.getBackground().draw(batch);
			if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
				Vector3 cursor = Editor.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (cursor.x >= workspace.getBackogrund().getX() && cursor.x <= workspace.getBackogrund().getX() + workspace.getBackogrund().getWidth()
						&& cursor.y >= workspace.getBackogrund().getY() && cursor.y <= workspace.getBackogrund().getY() + workspace.getBackogrund().getHeight()) {
					currentGunPoint.getBackground().setPosition(cursor.x, cursor.y);
				}
			}
		}
		
		if (viewTowersWindow != null) {
			viewTowersWindow.show(batch);
			if (viewTowersWindow.isSelected()) {
				for (BlockTower blockTower : workspace.getBlockTowers()) {
					if (blockTower.getPosition() == viewTowersWindow.getPositionTower()) {
						if (isTechnique) {
							blockBodyData.hide();
							blockTowerData.resume();
							isTechnique = false;
						}
						workspace.setCurrentBlockTower(blockTower);
						firstPageButton.setVisible(true);
						secondPageButton.setVisible(true);
						pointsButton.setVisible(true);
						disablePointsButton.setVisible(true);
						addPointButton.setVisible(true);
						blockTowerData.setFirstPage();
						viewTowersWindow.dispose();
						viewTowersWindow = null;
						break;
					}
					viewTowersWindow.dispose();
					viewTowersWindow = null;
				}
			}
		}
		
		if (textureOpeningWindow != null) {
			textureOpeningWindow.show(batch);
			if (textureOpeningWindow.isOpen()) {
				try {
					Texture texture = new Texture(Gdx.files.absolute(textureOpeningWindow.getPath()));
					if (isTechnique) {
						workspace.getBlockBody().setTexture(texture);
					} else {
						workspace.getCurrentBlockTower().setTexture(texture);	
					}
				} catch (GdxRuntimeException e) {
					System.out.println("Error loading texture: " + textureOpeningWindow.getPath());
				}
				textureOpeningWindow.dispose();
				textureOpeningWindow = null;
			} else if (textureOpeningWindow.isCancel()) {
				textureOpeningWindow.dispose();
				textureOpeningWindow = null;
			}
		}
		
		if (saveModelWindow != null) {
			saveModelWindow.show(batch);
			if (saveModelWindow.isOpen()) {
				ModelIO.saveModel(saveModelWindow.getPath(), workspace.getBlockBody(), workspace.getBlockTowers(), gunPoints, shellPropertiesWindow,
						workspace.getBlockBody().getBackground().getTexture().toString(), workspace.getCurrentBlockTower().getBackground().getTexture().toString());
				saveModelWindow.dispose();
				saveModelWindow = null;
			} else if (saveModelWindow.isCancel()) {
				saveModelWindow.dispose();
				saveModelWindow = null;
			}
		}
		
		if (pointsWindow != null) {
			pointsWindow.show(batch);
			if (pointsWindow.isSelected()) {
				currentGunPoint = gunPoints.get(pointsWindow.getId());
				isPoint = true;
				pointsWindow.dispose();
				pointsWindow = null;
			}
		}
		
		if (openSoundShotWindow != null) {
			openSoundShotWindow.show(batch);
			if (openSoundShotWindow.isOpen()) {
				if (sound != null) sound.dispose();
				try {
					sound = Gdx.audio.newSound(Gdx.files.absolute(openSoundShotWindow.getPath()));	
				} catch (GdxRuntimeException e) {
					System.out.println("Error loading sound of shot: " + openSoundShotWindow.getPath());
				}
				openSoundShotWindow.dispose();
				openSoundShotWindow = null;
			} else if (openSoundShotWindow.isCancel()) {
				openSoundShotWindow.dispose();
				openSoundShotWindow = null;
			}
		}
		
		shellPropertiesWindow.show(batch);
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		bitmapFont.dispose();
		
		bodyButton.remove();
		towerButton.remove();
		changeTextureButton.remove();
		saveButton.remove();
		applyButton.remove();
		firstPageButton.remove();
		secondPageButton.remove();
		pointsButton.remove();
		disablePointsButton.remove();
		addPointButton.remove();
		testingButton.remove();
		completeTestingButton.remove();
		addSoundShotButton.remove();
		shellButton.remove();
		
		if (sound != null) sound.dispose();
		shellPropertiesWindow.dispose();
		
		for (GunPoint point : gunPoints) point.dispose();
	}
}