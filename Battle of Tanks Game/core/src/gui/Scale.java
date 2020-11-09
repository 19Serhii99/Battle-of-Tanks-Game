package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import util.CameraController;
import util.Font;
import util.TextureCreator;

public class Scale extends BaseActor implements Disposable {
	private Sprite background;
	private Sprite backgroundOver;
	private Sprite handle;
	
	private Label value;
	
	private float maxValue;
	private float currentValue;
	
	private boolean isIntValues;
	
	public Scale (float maxValue, float currentValue, float x, float y, float width, float height, boolean isIntValues) {
		this.maxValue = maxValue;
		this.isIntValues = isIntValues;
		
		background = new Sprite(TextureCreator.createTexture(Color.GRAY));
		background.setSize(width, height);
		background.setPosition(x, y);
		
		backgroundOver = new Sprite(TextureCreator.createTexture(Color.GREEN));
		backgroundOver.setPosition(x, y);
		
		float percent = (currentValue * 100) / maxValue;
		float maxP = background.getVertices()[Batch.X3] - background.getX();
		float newX = (maxP * percent) / 100;
		
		handle = new Sprite(TextureCreator.createTexture(new Color(221f / 255f, 91f / 255f, 91f / 255f, 1.0f)));
		handle.setSize(20, 50);
		handle.setPosition(newX + background.getX(), y - handle.getHeight() / 2 + background.getHeight() / 2);
		
		backgroundOver.setSize(Math.abs(handle.getX() - background.getX()), background.getHeight());
		
		
		String valueString;
		
		if (isIntValues) {
			int valueTemp = (int) currentValue;
			valueString = String.valueOf(valueTemp);
		} else {
			valueString = String.valueOf(currentValue);
		}
		
		value = new Label(valueString, Font.getInstance().generateBitmapFont(new Color(221f / 255f, 91f / 255f, 91f / 255f, 1.0f), 25));
		value.setPosition(background.getVertices()[Batch.X3] + 20, background.getY() + background.getHeight() / 2 + value.getHeight() / 2);
	}
	
	public void setValue (float inputValue) {
		currentValue = inputValue;
		
		float percent = (currentValue * 100) / maxValue;
		float maxP = background.getVertices()[Batch.X3] - background.getX();
		float newX = (maxP * percent) / 100;
		
		handle.setPosition(newX + background.getX(), background.getY() - handle.getHeight() / 2 + background.getHeight() / 2);
		
		backgroundOver.setSize(Math.abs(handle.getX() - background.getX()), background.getHeight());
		
		String valueString;
		
		if (isIntValues) {
			int valueTemp = (int) currentValue;
			valueString = String.valueOf(valueTemp);
		} else {
			valueString = String.valueOf(currentValue);
		}
		
		value.setText(valueString);
		value.setPosition(background.getVertices()[Batch.X3] + 20, background.getY() + background.getHeight() / 2 + value.getHeight() / 2);
	}
	
	public void show (SpriteBatch batch) {
		super.act(background.getX(), handle.getY(), background.getWidth(), handle.getHeight());
		
		if (super.isOver()) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				Vector3 cursor = CameraController.getInstance().unproject();
				handle.setPosition(cursor.x - handle.getWidth() / 2, handle.getY());
				
				float handleX = handle.getX() + handle.getWidth() / 2 - background.getX();
				float backgroundX = background.getVertices()[Batch.X3] - background.getX();
				float percent = (handleX * 100) / backgroundX;
				
				currentValue = (percent * maxValue) / 100;
				backgroundOver.setSize(Math.abs(handle.getX() - background.getX()), background.getHeight());
				
				if (isIntValues) {
					int valueTemp = (int) currentValue;
					value.setText(String.valueOf(valueTemp));
				} else {
					value.setText(String.valueOf(currentValue));
				}
				
				value.setPosition(background.getVertices()[Batch.X3] + 20, background.getY() + background.getHeight() / 2 + value.getHeight() / 2);
			}
		}
		
		background.draw(batch);
		backgroundOver.draw(batch);
		handle.draw(batch);
		value.draw(batch);
	}
	
	public float getMaxValues () {
		return maxValue;
	}
	
	public float getCurrentValue () {
		return currentValue;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		backgroundOver.getTexture().dispose();
		handle.getTexture().dispose();
		value.getFont().dispose();
	}
}