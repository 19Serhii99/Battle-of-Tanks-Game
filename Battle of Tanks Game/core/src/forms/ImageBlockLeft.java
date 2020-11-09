package forms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import util.CameraController;

public class ImageBlockLeft implements Disposable {
	private Sprite background;
	private Sprite tank1;
	private Sprite tank2;
	private Sprite tank3;
	private Sprite tank4;
	
	private boolean isHide = false;
	private float alpha = 0.0f;
	
	public ImageBlockLeft () {
		initBackground();
	}
	
	private void initBackground () {
		Texture backgroundTexture = new Texture(Gdx.files.internal("images/menu/formFriends.png"));
		backgroundTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		background = new Sprite(backgroundTexture);
		background.setSize(400, 920);
		background.setPosition(50, CameraController.getInstance().getHeight() - background.getHeight() - 70);
		
		tank1 = new Sprite(new Texture(Gdx.files.internal("images/tank5.png")));
		tank2 = new Sprite(new Texture(Gdx.files.internal("images/tank6.png")));
		tank3 = new Sprite(new Texture(Gdx.files.internal("images/tank6.png")));
		tank4 = new Sprite(new Texture(Gdx.files.internal("images/tank7.png")));
		
		tank1.setSize(400, 200);
		tank2.setSize(400, 200);
		tank3.setSize(400, 200);
		tank4.setSize(400, 200);
		
		tank1.setPosition(background.getX() + background.getWidth() / 2 - tank1.getWidth() / 2, background.getY() + 100);
		tank2.setPosition(background.getX() + background.getWidth() / 2 - tank2.getWidth() / 2, background.getY() + 300);
		tank3.setPosition(background.getX() + background.getWidth() / 2 - tank3.getWidth() / 2, background.getY() + 500);
		tank4.setPosition(background.getX() + background.getWidth() / 2 - tank4.getWidth() / 2, background.getY() + 700);
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
		tank1.setAlpha(alpha);
		tank2.setAlpha(alpha);
		tank3.setAlpha(alpha);
		tank4.setAlpha(alpha);
			
		batch.begin();
		background.draw(batch);
		tank1.draw(batch);
		tank2.draw(batch);
		tank3.draw(batch);
		tank4.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		tank1.getTexture().dispose();
		tank2.getTexture().dispose();
		tank3.getTexture().dispose();
		tank4.getTexture().dispose();
	}
}