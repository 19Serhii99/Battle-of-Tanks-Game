package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import gui.Label;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class ConfirmationFailedForm implements Disposable {
	private Sprite background;
	private Label label;
	
	private float alpha = 0.0f;
	private boolean isHide = false;
	
	public ConfirmationFailedForm () {
		background = new Sprite(TextureCreator.createTexture(Color.BLACK));
		background.setSize(300, 50);
		background.setPosition(CameraController.getInstance().getWidth() / 2 - background.getWidth() / 2, 200);
		
		BitmapFont labelFont = Font.getInstance().generateBitmapFont(Color.RED, 30);
		labelFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		label = new Label("Невірний код", labelFont);
		label.setPosition(background.getX() + background.getWidth() / 2 - label.getWidth() / 2, background.getY() + background.getHeight() / 2 + label.getHeight() / 2);
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
		label.setAlphas(alpha);
		
		batch.begin();
		background.draw(batch);
		label.draw(batch);
		batch.end();
	}
	
	public float getAlpha () {
		return alpha;
	}
	
	public void hide () {
		isHide = true;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		label.getFont().dispose();
	}
}