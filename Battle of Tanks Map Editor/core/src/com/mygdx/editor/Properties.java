package com.mygdx.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import forms.FileChooserForm;
import forms.TexturesTable;
import gui.Label;
import gui.TextButton;
import map.MapTexture;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class Properties implements Disposable {
	private Sprite background;
	private Label caption;
	private TextButton mapTexturesButton;
	private FileChooserForm fileChooserForm;
	private TexturesTable texturesTable;
	private Workspace workspace;
	
	private boolean isBlockWorkspaceEvents = false;
	
	public Properties (Workspace workspace) {
		this.workspace = workspace;
		
		background = new Sprite(TextureCreator.createTexture(Color.GRAY));
		background.setSize(300, 750);
		background.setPosition(CameraController.getInstance().getWidth() - background.getWidth() - 20, 10);
		
		BitmapFont font = Font.getInstance().generateBitmapFont(Color.WHITE, 20);
		
		caption = new Label("Свойства", font);
		caption.setPosition(background.getX() + background.getWidth() / 2 - caption.getWidth() / 2, background.getVertices()[Batch.Y2] - 10);
		
		mapTexturesButton = new TextButton("Загрузить текстуру", Objects.getInstance().getButtonBitmapFont());
		mapTexturesButton.setBitmapFontFocused(Objects.getInstance().getButtonFocusedBitmapFont());
		mapTexturesButton.setTexture(Objects.getInstance().getButtonTexture());
		mapTexturesButton.setTextureOver(Objects.getInstance().getButtonOverTexture());
		mapTexturesButton.setTextureFocused(Objects.getInstance().getButtonFocusedTexture());
		mapTexturesButton.setSize(290, 50);
		mapTexturesButton.setPosition(background.getX() + background.getWidth() / 2 - mapTexturesButton.getWidth() / 2, background.getVertices()[Batch.Y2] - 100);
		
		texturesTable = new TexturesTable(this);
	}
	
	public TexturesTable getTexturesTable () {
		return texturesTable;
	}
	
	public void show (SpriteBatch batch) {
		batch.begin();
		
		background.draw(batch);
		caption.draw(batch);
		mapTexturesButton.show(batch);
		
		if (mapTexturesButton.isReleased()) {
			if (fileChooserForm == null) {
				fileChooserForm = new FileChooserForm();
			}
		}
		
		texturesTable.show(batch, workspace.getMapTextures());
		
		batch.end();
		
		if (fileChooserForm != null) {
			isBlockWorkspaceEvents = true;
			fileChooserForm.show(batch);
			if (fileChooserForm.isCancel()) {
				fileChooserForm.dispose();
				fileChooserForm = null;
				isBlockWorkspaceEvents = false;
			} else if (fileChooserForm.isOpen()) {
				try {
					MapTexture mapTexture = new MapTexture(workspace.getMapTextures().size, new Texture(Gdx.files.absolute(fileChooserForm.getPath())));
					workspace.getMapTextures().add(mapTexture);
					texturesTable.addMapTexture(mapTexture);
				} catch (GdxRuntimeException e) {
					System.out.println("Ошибка открытия файла " + fileChooserForm.getPath());
				}
				fileChooserForm.dispose();
				fileChooserForm = null;
				isBlockWorkspaceEvents = false;
			}
		}
	}
	
	public boolean isBlockWorkspaceEvents () {
		return isBlockWorkspaceEvents;
	}
	
	public Sprite getBackground () {
		return background;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		caption.getFont().dispose();
	}
}