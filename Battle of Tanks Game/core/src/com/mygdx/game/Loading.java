package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import gui.Label;
import util.Axis;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class Loading implements Disposable {
	private Sprite background;
	private Label label;
	private Array <Sprite> circles;
	private BitmapFont bitmapFont;
	
	private float centerX;
	private float centerY;
	private float alphaBackground = 0.7f;
	private float alpha = 0.0f;
	private boolean isHide = false;
	
	public Loading () {
		centerX = CameraController.getInstance().getWidth() / 2;
		centerY = CameraController.getInstance().getHeight() / 2;
		
		background = new Sprite(TextureCreator.createTexture(new Color(0.0f, 0.0f, 0.0f, 1.0f)));
		background.setSize(CameraController.getInstance().getWidth(), CameraController.getInstance().getHeight());
		background.setPosition(centerX - background.getWidth() / 2, centerY - background.getHeight() / 2);
		
		circles = new Array <Sprite>(8);
		
		Texture texture = new Texture(Gdx.files.internal("images/gui/loadingCircle.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		for (int i = 0; i < 8; i++) {
			circles.add(new Sprite(texture));
			circles.get(i).getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			circles.get(i).setSize(10, 10);
			circles.get(i).setPosition(centerX + 40,  centerY);
			Axis.getInstance().turnAxis(45 * i);
			Vector2 pos = Axis.getInstance().turnPoint(centerX, centerY, circles.get(i).getX(), circles.get(i).getY());
			circles.get(i).setPosition(pos.x, pos.y);
		}
		
		bitmapFont = Font.getInstance().generateBitmapFont(Color.WHITE, 40);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void setText (String text) {
		label = new Label(text, bitmapFont);
		label.setPosition(CameraController.getInstance().getWidth() / 2 - (label.getWidth() + 10) / 2, CameraController.getInstance().getHeight() / 2 + label.getHeight() / 2);
		for (int i = 0; i < circles.size; i++) {
			float tempX = centerX - 40;
			float tempWidth = label.getX() + label.getWidth() - tempX;
			circles.get(i).setPosition(circles.get(i).getX() + tempWidth + 5,  circles.get(i).getY());
		}
		centerX = label.getX() + label.getWidth() + 45;
	}
	
	public void show (SpriteBatch batch) {
		batch.begin();
		if (isHide) {
			alpha -= Gdx.graphics.getDeltaTime() * 5;
			if (alpha < 0.0f) alpha = 0.0f;
		} else {
			alpha += Gdx.graphics.getDeltaTime() * 2;
			if (alpha > 1.0f) alpha = 1.0f;
		}
		background.setAlpha(alpha < alphaBackground ? alpha : alphaBackground);
		background.draw(batch);
		Axis.getInstance().turnAxis(200 * Gdx.graphics.getDeltaTime());
		for (Sprite circle : circles) {
			Vector2 pos = Axis.getInstance().turnPoint(centerX, centerY, circle.getX(), circle.getY());
			circle.setPosition(pos.x, pos.y);
			circle.setAlpha(alpha);
			circle.draw(batch);
		}
		label.setAlphas(alpha);
		label.draw(batch);
		batch.end();
	}
	
	public void setAlpha (float alpha) {
		this.alpha = alpha;
	}
	
	public void hide () {
		isHide = true;
	}
	
	public float getAlpha () {
		return alpha;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
		circles.get(0).getTexture().dispose();
		label.getFont().dispose();
	}
}
