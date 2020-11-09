package com.mygdx.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import forms.CreateOrLoadMapForm;
import util.CameraController;

public class Editor extends ApplicationAdapter {
	private SpriteBatch batch;
	private CreateOrLoadMapForm createOrLoadMapForm;
	private Sprite background;
	private Workspace workspace;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		CameraController.getInstance().setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		CameraController.getInstance().setPosition(CameraController.getInstance().getWidth() / 2, CameraController.getInstance().getHeight() / 2);
		
		Objects.getInstance().createObjects();
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(Events.getInstance());
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/background.jpg"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		background = new Sprite(backgroundTexture);
		background.setSize(CameraController.getInstance().getWidth(), CameraController.getInstance().getHeight());
		
		createOrLoadMapForm = new CreateOrLoadMapForm();
		
		workspace = new Workspace();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		CameraController.getInstance().update();
		batch.setProjectionMatrix(CameraController.getInstance().getCombinedMatrix());
		
		batch.begin();
		background.draw(batch);
		batch.end();
		
		if (createOrLoadMapForm != null) {
			createOrLoadMapForm.show(batch);
			if (createOrLoadMapForm.isCreate()) {
				workspace.createMap(createOrLoadMapForm.getCreatingMapForm().getChunkWidth(), createOrLoadMapForm.getCreatingMapForm().getChunkHeight(),
						createOrLoadMapForm.getCreatingMapForm().getTileWidth(), createOrLoadMapForm.getCreatingMapForm().getTileHeight(),
						createOrLoadMapForm.getCreatingMapForm().getTileAmountX(), createOrLoadMapForm.getCreatingMapForm().getTileAmountY());
				createOrLoadMapForm.dispose();
				createOrLoadMapForm = null;
			} else if (createOrLoadMapForm.isOpen()) {
				workspace.loadMap(createOrLoadMapForm.getLoadingMapForm().getPath());
				createOrLoadMapForm.dispose();
				createOrLoadMapForm = null;
			}
		} else {
			workspace.show(batch);
		}
		
		Events.getInstance().clear();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.getTexture().dispose();
		Objects.getInstance().dispose();
		if (createOrLoadMapForm != null) createOrLoadMapForm.dispose();
		workspace.dispose();
	}
}