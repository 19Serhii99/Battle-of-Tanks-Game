package forms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import gui.BaseActor;
import gui.Button;
import map.MapTexture;
import util.TextureCreator;

public class TextureItem extends BaseActor implements Disposable {
	private Sprite background;
	private Button removeButton;
	
	private MapTexture mapTexture;
	
	public TextureItem (float x, float y, float width, float height, MapTexture mapTexture) {
		background = new Sprite(mapTexture.getTexture());
		background.setSize(width, height);
		background.setPosition(x, y);
		
		this.mapTexture = mapTexture;
		
		removeButton = new Button();
		removeButton.setTexture(TextureCreator.createTexture(Color.RED));
		removeButton.setSize(5, 5);
		removeButton.setPosition(background.getVertices()[Batch.X4] - removeButton.getWidth(), background.getVertices()[Batch.Y2] - removeButton.getHeight());
	}
	
	public void setPosition (float x, float y) {
		background.setPosition(x, y);
		removeButton.setPosition(background.getVertices()[Batch.X4] - removeButton.getWidth(), background.getVertices()[Batch.Y2] - removeButton.getHeight());
	}
	
	public void show (SpriteBatch batch) {
		super.act(background.getX(), background.getY(), background.getWidth(), background.getHeight());
		
		background.draw(batch);
		removeButton.show(batch);
	}
	
	public boolean isRemove () {
		return removeButton.isReleased();
	}
	
	public MapTexture getMapTexture () {
		return mapTexture;
	}
	
	public Sprite getBackground () {
		return background;
	}
	
	@Override
	public void dispose () {
		background.getTexture().dispose();
		removeButton.getTexture().dispose();
	}
}