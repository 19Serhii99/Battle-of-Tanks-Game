package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import gui.Label;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class SignInFailForm implements Disposable {
	private Sprite sprite;
	private Label label;
	
	public SignInFailForm () {
		Texture texture = TextureCreator.createTexture(Color.BLACK);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		sprite = new Sprite(texture);
		sprite.setSize(300, 50);
		sprite.setPosition(CameraController.getInstance().getWidth() / 2 - sprite.getWidth() / 2, -sprite.getHeight());
		sprite.setAlpha(1.0f);
		
		BitmapFont bitmapFont = Font.getInstance().generateBitmapFont(Color.RED, 20);
		bitmapFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		label = new Label("Невірний логін або пароль", bitmapFont);
		label.setPosition(sprite.getX() + sprite.getWidth() / 2 - label.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2 + label.getHeight() / 2);
	}
	
	public void show (SpriteBatch batch) {
		if (Math.abs(sprite.getY() - 100) <= Gdx.graphics.getDeltaTime() * 200) {
			sprite.setPosition(sprite.getX(), 100);
		} else {
			sprite.setPosition(sprite.getX(), sprite.getY() + Gdx.graphics.getDeltaTime() * 200);
		}
		label.setPosition(sprite.getX() + sprite.getWidth() / 2 - label.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2 + label.getHeight() / 2);
		
		batch.begin();
		sprite.draw(batch);
		label.draw(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		sprite.getTexture().dispose();
		label.getFont().dispose();
	}
}