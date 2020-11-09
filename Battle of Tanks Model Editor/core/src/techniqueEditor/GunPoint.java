package techniqueEditor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

import util.TextureCreator;

public class GunPoint implements Disposable {
	private Sprite background;
	
	public GunPoint (float x, float y) {
		background = new Sprite(TextureCreator.createTexture(Color.RED));
		background.setSize(5, 5);
		background.setPosition(x, y);
		background.setOriginCenter();
	}
	
	public Sprite getBackground () {
		return background;
	}

	@Override
	public void dispose () {
		background.getTexture().dispose();
	}
}
