package com.mygdx.game;

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

import util.Font;
import util.TextureCreator;

public class OpeningWindow implements Disposable {
	private Sprite background;
	private BitmapFontCache label;
	private TextField field;
	private TextButton open;
	private TextButton cancel;	
	private boolean isOpen;
	private boolean isCancel;
	
	public OpeningWindow (BitmapFont bitmapFont, TextButtonStyle textButtonStyle, TextFieldStyle textFieldStyle, final Stage stage) {
		background = new Sprite(TextureCreator.createTexture(Color.BROWN));
		background.setSize(700, 200);
		background.setPosition(Editor.getCamera().viewportWidth / 2 - background.getWidth() / 2, Editor.getCamera().viewportHeight / 2 - background.getHeight() / 2);
		
		label = new BitmapFontCache(bitmapFont);
		label.setText("Enter path to file", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter path to file") / 2, background.getVertices()[Batch.Y2] - 10);

		field = new TextField("", textFieldStyle);
		field.setSize(background.getWidth(), 50);
		field.setPosition(background.getX(), background.getVertices()[Batch.Y2] - 100);
		field.setAlignment(Align.center);
		
		open = new TextButton("Open", textButtonStyle);
		open.setSize(200, 50);
		open.setPosition(background.getX() + 10, background.getY() + 10);
		
		cancel = new TextButton("Cancel", textButtonStyle);
		cancel.setSize(200, 50);
		cancel.setPosition(background.getX() + background.getWidth() - cancel.getWidth() - 10, background.getY() + 10);
		
		open.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= open.getX() && cursor.x <= open.getX() + open.getWidth()
				&& cursor.y >= open.getY() && cursor.y <= open.getY() + open.getHeight()) {
					isOpen = true;
				}
			}
		});
		
		cancel.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= cancel.getX() && cursor.x <= cancel.getX() + cancel.getWidth()
				&& cursor.y >= cancel.getY() && cursor.y <= cancel.getY() + cancel.getHeight()) {
					isCancel = true;
				}
			}
		});
		
		stage.addActor(field);
		stage.addActor(open);
		stage.addActor(cancel);
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
		label.draw(batch);
	}
	
	public boolean isOpen () {
		return isOpen;
	}
	
	public boolean isCancel () {
		return isCancel;
	}
	
	public String getPath () {
		return field.getText();
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		field.remove();
		open.remove();
		cancel.remove();
	}
}