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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

import util.Font;
import util.TextureCreator;

public class WindowCreatingMap implements Disposable {
	private BitmapFontCache caption;
	private Sprite background;
	private BitmapFontCache countRowsLabel;
	private BitmapFontCache countColumnsLabel;
	private BitmapFontCache tileWidthLabel;
	private BitmapFontCache tileHeightLabel;
	private BitmapFontCache chunkWidthLabel;
	private BitmapFontCache chunkHeightLabel;
	private TextField countRowsField;
	private TextField countColumnsField;
	private TextField tileWidthField;
	private TextField tileHeightField;
	private TextField chunkWidthField;
	private TextField chunkHeightField;
	private TextButton createButton;
	private TextButton cancelButton;
	private boolean isCreate;
	private boolean isCancel;
	
	
	public WindowCreatingMap (final Stage stage, TextFieldStyle textFieldStyle, TextButtonStyle textButtonStyle, Skin skin, BitmapFont bitmapFont) {		
		background = new Sprite(TextureCreator.createTexture(Color.BROWN));
		background.setSize(500, 650);
		background.setPosition(Editor.getCamera().viewportWidth / 2 - background.getWidth() / 2, Editor.getCamera().viewportHeight / 2 - background.getHeight() / 2);

		caption = new BitmapFontCache(bitmapFont);
		caption.setText("Creating Map", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Creating Map") / 2, background.getVertices()[Batch.Y2] - 10);
		
		countRowsLabel = new BitmapFontCache(bitmapFont);
		countRowsLabel.setText("Enter count rows:", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter count rows:") / 2, background.getVertices()[Batch.Y2] - 40);
		
		countColumnsLabel = new BitmapFontCache(bitmapFont);
		countColumnsLabel.setText("Enter count columns:", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter count columns:") / 2, background.getVertices()[Batch.Y2] - 130);
	
		tileWidthLabel = new BitmapFontCache(bitmapFont);
		tileWidthLabel.setText("Enter tile width:", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter tile width:") / 2, background.getVertices()[Batch.Y2] - 220);
		
		tileHeightLabel = new BitmapFontCache(bitmapFont);
		tileHeightLabel.setText("Enter tile height:", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter tile height:") / 2, background.getVertices()[Batch.Y2] - 310);

		chunkWidthLabel = new BitmapFontCache(bitmapFont);
		chunkWidthLabel.setText("Enter chunk width:", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter chunk width:") / 2, background.getVertices()[Batch.Y2] - 400);

		chunkHeightLabel = new BitmapFontCache(bitmapFont);
		chunkHeightLabel.setText("Enter chunk height:", background.getX() + background.getWidth() / 2 - Font.getFont().getWidth(bitmapFont, "Enter chunk height:") / 2, background.getVertices()[Batch.Y2] - 490);
		
		countRowsField = new TextField("", textFieldStyle);
		countRowsField.setSize(300, 50);
		countRowsField.setAlignment(Align.center);
		countRowsField.setPosition(background.getX() + background.getWidth() / 2 - countRowsField.getWidth() / 2, background.getVertices()[Batch.Y2] - 120);
		
		countColumnsField = new TextField("", textFieldStyle);
		countColumnsField.setSize(300, 50);
		countColumnsField.setAlignment(Align.center);
		countColumnsField.setPosition(background.getX() + background.getWidth() / 2 - countColumnsField.getWidth() / 2, background.getVertices()[Batch.Y2] - 210);
		
		tileWidthField = new TextField("", textFieldStyle);
		tileWidthField.setSize(300, 50);
		tileWidthField.setAlignment(Align.center);
		tileWidthField.setPosition(background.getX() + background.getWidth() / 2 - tileWidthField.getWidth() / 2, background.getVertices()[Batch.Y2] - 300);

		tileHeightField = new TextField("", textFieldStyle);
		tileHeightField.setSize(300, 50);
		tileHeightField.setAlignment(Align.center);
		tileHeightField.setPosition(background.getX() + background.getWidth() / 2 - tileHeightField.getWidth() / 2, background.getVertices()[Batch.Y2] - 390);
		
		chunkWidthField = new TextField("", textFieldStyle);
		chunkWidthField.setSize(300, 50);
		chunkWidthField.setAlignment(Align.center);
		chunkWidthField.setPosition(background.getX() + background.getWidth() / 2 - chunkWidthField.getWidth() / 2, background.getVertices()[Batch.Y2] - 480);
		
		chunkHeightField = new TextField("", textFieldStyle);
		chunkHeightField.setSize(300, 50);
		chunkHeightField.setAlignment(Align.center);
		chunkHeightField.setPosition(background.getX() + background.getWidth() / 2 - chunkHeightField.getWidth() / 2, background.getVertices()[Batch.Y2] - 570);
		
		createButton = new TextButton("Create", textButtonStyle);
		createButton.setSize(230, 50);
		createButton.setPosition(background.getX() + 5, background.getY() + 10);
		
		cancelButton = new TextButton("Cancel", textButtonStyle);
		cancelButton.setSize(230, 50);
		cancelButton.setPosition(background.getX() + background.getWidth() - cancelButton.getWidth() - 5, background.getY() + 10);
		
		createButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= createButton.getX() && cursor.x <= createButton.getX() + createButton.getWidth()
				&& cursor.y >= createButton.getY() && cursor.y <= createButton.getY() + createButton.getHeight()) {
					isCreate = true;
				}
			}
		});
		
		cancelButton.addListener(new InputListener() {
			@Override
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			@Override
			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				Vector3 cursor = stage.getViewport().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.0f));
				if (button == Buttons.LEFT && cursor.x >= cancelButton.getX() && cursor.x <= cancelButton.getX() + cancelButton.getWidth()
				&& cursor.y >= cancelButton.getY() && cursor.y <= cancelButton.getY() + cancelButton.getHeight()) {
					isCancel = true;
				}
			}
		});
		
		stage.addActor(countRowsField);
		stage.addActor(countColumnsField);
		stage.addActor(tileWidthField);
		stage.addActor(tileHeightField);
		stage.addActor(chunkWidthField);
		stage.addActor(chunkHeightField);
		stage.addActor(createButton);
		stage.addActor(cancelButton);
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
		caption.draw(batch);
		countRowsLabel.draw(batch);
		countColumnsLabel.draw(batch);
		tileWidthLabel.draw(batch);
		tileHeightLabel.draw(batch);
		chunkWidthLabel.draw(batch);
		chunkHeightLabel.draw(batch);
	}
	
	public boolean isCreate () {
		return isCreate;
	}
	
	public boolean isCancel () {
		return isCancel;
	}

	public String getCountRows () {
		return countRowsField.getText();
	}
	
	public String getCountColumns () {
		return countColumnsField.getText();
	}
	
	public String getTileWidth () {
		return tileWidthField.getText();
	}
	
	public String getTileHeight () {
		return tileHeightField.getText();
	}
	
	public String getChunkWidth () {
		return chunkWidthField.getText();
	}
	
	public String getChunkHeight () {
		return chunkHeightField.getText();
	}
	
	@Override
	public void dispose () {
		countRowsField.remove();
		countColumnsField.remove();
		tileWidthField.remove();
		tileHeightField.remove();
		chunkWidthField.remove();
		chunkHeightField.remove();
		createButton.remove();
		cancelButton.remove();
		background.getTexture().dispose();;
	}
}
