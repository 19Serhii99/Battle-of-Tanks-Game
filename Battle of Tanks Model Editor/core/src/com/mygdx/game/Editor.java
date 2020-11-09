package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import techniqueEditor.TechniqueEditor;

public class Editor extends ApplicationAdapter {
	private SpriteBatch batch;
	private static OrthographicCamera camera;
	//private MapEditor mapEditor;
	private TechniqueEditor techniqueEditor;
	private Stage stage;
	private Skin skin;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera();
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.position.x += camera.viewportWidth / 2;
		camera.position.y += camera.viewportHeight / 2;
		
		stage = new Stage();
		
		skin = new Skin();
		skin.add("button", new Texture(Gdx.files.internal("textures/button.png")));
		skin.add("buttonActivated", new Texture(Gdx.files.internal("textures/button_activated.png")));
		skin.add("field", new Texture(Gdx.files.internal("textures/field.png")));
		skin.add("cursor", new Texture(Gdx.files.internal("textures/cursor.png")));
		skin.add("selection", new Texture(Gdx.files.internal("textures/selection.png")));
		
		//mapEditor = new MapEditor(stage, skin);
		
		techniqueEditor = new TechniqueEditor(stage, skin);
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(Events.getEvents());
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
		camera.update();
		//mapEditor.show(batch);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		techniqueEditor.show(batch);
		batch.end();
		stage.act();
		stage.draw();
		Events.getEvents().clear();
	}
	
	@Override
	public void dispose () {
		//mapEditor.dispose();
		skin.dispose();
		stage.clear();
		stage.dispose();
		batch.dispose();
	}
	
	public static OrthographicCamera getCamera () {
		return camera;
	}
}