package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Events;

import util.CameraController;

public class Scrollbar {
	private Sprite background;
	private Sprite line;
	private Vector3 startCursor;
	
	private Texture texture;
	private Texture focusedTexture;
	private Texture overTexture;
	
	private float y;
	private float totalHeight;
	private float alpha = 0.0f;
	private boolean isScrolling = false;
	private boolean isHide = false;
	
	public Scrollbar (Texture backgroundTexture, Texture lineTexture, Texture lineFocusedTexture, Texture lineOverTexture, float width, float height, float totalHeight, float x, float y) {
		background = new Sprite(backgroundTexture);
		background.setSize(width, height);
		background.setPosition(x, y);
		
		this.totalHeight = totalHeight;
		
		texture = lineTexture;
		focusedTexture = lineFocusedTexture;
		overTexture = lineOverTexture;
		
		calculateHeight(totalHeight);
	}
	
	public void calculateHeight (float totalHeight) {
		this.totalHeight = totalHeight;
		float percentTrue = (background.getHeight() * 100) / totalHeight;
		float percentFalse = 100 - percentTrue;
		float percentFalseOfBackground = (percentFalse * background.getHeight()) / 100;
		float newHeight = background.getHeight() - percentFalseOfBackground;
		
		line = new Sprite(texture);
		line.setSize(background.getWidth() - 3, newHeight);
		line.setPosition(background.getX() + background.getWidth() / 2 - line.getWidth() / 2, background.getVertices()[Batch.Y2] - line.getHeight());
		this.y = line.getY();
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
		line.setAlpha(alpha);
		
		background.draw(batch);
		line.draw(batch);
		
		Vector3 cursor = CameraController.getInstance().unproject();
		if (cursor.x >= line.getX() && cursor.x <= line.getX() + line.getWidth() && cursor.y >= line.getY() && cursor.y <= line.getY() + line.getHeight()) {
			line.setRegion(overTexture);
			if (Events.getInstance().isMouseLeftPressed()) {
				isScrolling = true;
				startCursor = new Vector3(cursor);
			} else if (Events.getInstance().isMouseLeftReleased()) {
				isScrolling = false;
				this.y = line.getY();
			}
		} else {
			line.setRegion(texture);
		}
		
		if (isScrolling) {
			float y = cursor.y - startCursor.y;
			float newY = this.y + y;
			if (newY < background.getY()) {
				newY = background.getY();
			} else if (newY + line.getHeight() > background.getVertices()[Batch.Y2]) {
				newY = background.getVertices()[Batch.Y2] - line.getHeight();
			}
			line.setPosition(line.getX(), newY);
			if (Events.getInstance().isMouseLeftReleased()) {
				isScrolling = false;
				this.y = line.getY();
			}
			line.setRegion(focusedTexture);
		}
	}
	
	public float heightScrolling () {
		float height = background.getVertices()[Batch.Y2] - line.getY();
		float percent = (height * 100) / background.getHeight();
		float heightScrolling = (totalHeight * percent) / 100;
		float differentPercent = (line.getHeight() * 100) / background.getHeight();
		float different = (totalHeight * differentPercent) / 100;
		return heightScrolling - different;
	}
	
	public float getTotalHeight () {
		return totalHeight;
	}
	
	public float getLineHeight () {
		return line.getHeight();
	}
	
	public boolean isScrolling () {
		return isScrolling;
	}
}