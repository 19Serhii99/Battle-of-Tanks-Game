package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import forms.GameMenu;
import forms.SignInForm;
import forms.WaitForGameForm;
import net.Client;
import technique.Corps;
import technique.Tower;
import util.CameraController;
import util.Font;

public final class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private CameraController camera;
	private Settings settings;
	private SignInForm signInForm;
	private GameMenu gameMenu;
	private WaitForGameForm waitForGameForm;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		settings = Settings.getInstance();
		initCamera();
		
		Client.getInstance().connectToServer();
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer(Events.getInstance());
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		initStyles();
		
		signInForm = new SignInForm();
	}
	
	private void initCamera () {
		float cameraWidth = settings.getCameraWidth() <= 0 ? 800 : settings.getCameraWidth();
		float cameraHeight = settings.getCameraHeight() <= 0 ? 600 : settings.getCameraHeight();
		settings.setCameraSize(cameraWidth, cameraHeight);
		
		camera = CameraController.getInstance();
		camera.setSize(cameraWidth, cameraHeight);
		camera.setPosition(cameraWidth / 2, cameraHeight / 2);
	}
	
	private void initStyles () {
		Objects.getInstance().createObjects();
	}

	@Override
	public void render () {
		Gdx.gl20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		camera.update();
		batch.setProjectionMatrix(camera.getCombinedMatrix());
		
		if (signInForm != null) {
			signInForm.show(batch);
			if (signInForm.isLogin()) {
				signInForm.dispose();
				signInForm = null;
				gameMenu = new GameMenu();
			}
		} else if (gameMenu != null) {
			gameMenu.show(batch);
			if (!gameMenu.isAccount()) {
				gameMenu.dispose();
				gameMenu = null;
				signInForm = new SignInForm();
			} else if (gameMenu.isBattle()) {
				Corps corps = gameMenu.getBattleSettingsForm().getCorps();
				Tower tower = gameMenu.getBattleSettingsForm().getTower();
				int id = gameMenu.getBattleSettingsForm().getBattleType().getId();
				BattleType battleType = null;
				if (id == 0) {
					battleType = BattleType.COMMAND;
				} else if (id == 1) {
					battleType = BattleType.ASSAULT;
				} else if (id == 2) {
					battleType = BattleType.DEATHMATCH;
				}
				Client.getInstance().playBattle(battleType, corps.getId(), tower.getId());
				waitForGameForm = new WaitForGameForm(battleType, corps.getId(), tower.getId());
				gameMenu.dispose();
				gameMenu = null;
			}
		} else if (waitForGameForm != null) {
			waitForGameForm.show(batch);
			if (waitForGameForm.isFinished()) {
				waitForGameForm.dispose();
				waitForGameForm = null;
				CameraController.getInstance().setPosition(CameraController.getInstance().getWidth() / 2, CameraController.getInstance().getHeight() / 2);
				gameMenu = new GameMenu();
			}
		}
		
		Events.getInstance().clear();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		settings.save();
		Objects.getInstance().dispose();
		Font.getInstance().dispose();
		if (signInForm != null) signInForm.dispose();
		if (gameMenu != null) gameMenu.dispose();
	}
}