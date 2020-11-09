package techniqueEditor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import util.TextureCreator;

public abstract class Block implements Disposable {
	protected Sprite background;
	
	protected int position;
	protected int money;
	protected float speedRotation;
	
	public Block (float x, float y) {
		background = new Sprite(TextureCreator.createTexture(Color.BLUE));
		background.setOriginCenter();
		background.setSize(50, 50);
		background.setPosition(x, y);
	}
	
	public void show (SpriteBatch batch) {
		background.draw(batch);
	}
	
	public void setTexture (Texture texture) {
		if (background.getTexture() != null) {
			background.getTexture().dispose();
		}
		background.setTexture(texture);
	}
	
	public void setPosition (int position) {
		this.position = position;
	}
	
	public void setMoney (int money) {
		this.money = money;
	}
	
	public void setSpeedRotation (float speedRotation) {
		this.speedRotation = speedRotation;
	}
	
	public void setWidth (float width) {
		background.setSize(width, background.getHeight());
	}
	
	public void setHeight (float height) {
		background.setSize(background.getWidth(), height);
	}
	
	public void setX (float x) {
		background.setX(x);
	}
	
	public void setY (float y) {
		background.setY(y);
	}
	
	public int getPosition () {
		return position;
	}
	
	public int getMoney () {
		return money;
	}
	
	public float getSpeedRotation () {
		return speedRotation;
	}
	
	public float getWidth () {
		return background.getWidth();
	}
	
	public float getHeight () {
		return background.getHeight();
	}

	public float getX () {
		return background.getX();
	}
	
	public float getY () {
		return background.getY();
	}
	
	public Sprite getBackground () {
		return background;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
	}
}