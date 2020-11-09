package additional;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {
	private Array <TextureRegion> regions;
	private Texture texture;
	
	private float time;
	private float currentTime;
	
	private float x;
	private float y;
	private float width;
	private float height;
	private float rotation;
	
	private boolean isFinished = false;
	private boolean isEndless = false;
	
	private int index;
	
	public Animation (Texture texture, int amountX, int amountY, int regionWidth, int regionHeight, float time) {
		regions = new Array <TextureRegion>();
		
		for (int i = 0; i < amountY; i++) {
			for (int j = 0; j < amountX; j++) {
				TextureRegion region = new TextureRegion(texture, j * regionWidth, i * regionHeight, regionWidth, regionHeight);
				regions.add(region);
			}
		}
		
		this.texture = texture;
		this.time = time;
	}
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize (float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public void setRotation (float angle) {
		rotation = angle;
	}
	
	public void show (SpriteBatch batch) {
		currentTime += Gdx.graphics.getDeltaTime();
		
		if (currentTime >= time) {
			index++;
			if (index >= regions.size) {
				index = 0;
				isFinished = true;
			}
			currentTime = 0;
		}
		
		if (isFinished) {
			if (isEndless) {
				draw(batch);
			}
		} else {
			draw(batch);
		}
	}
	
	private void draw (SpriteBatch batch) {
		batch.draw(regions.get(index), x, y, width / 2, height / 2, width, height, 1, 1, rotation);
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	public float getWidth () {
		return width;
	}
	
	public float getHeight () {
		return height;
	}
	
	public float getRotation () {
		return rotation;
	}
	
	public void reset () {
		index = 0;
		isFinished = false;
	}
	
	public void setEndless (boolean endless) {
		this.isEndless = endless;
	}
	
	public void setFinished (boolean finished) {
		this.isFinished = finished;
	}
	
	public boolean isFinished () {
		return isFinished;
	}
	
	public boolean isEndless () {
		return isEndless;
	}
	
	public Array <TextureRegion> getRegions () {
		return regions;
	}
	
	public Texture getTexture () {
		return texture;
	}
}