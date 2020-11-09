package map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.editor.Events;

import util.CameraController;
import util.Mathematics;

public class BaseManager implements Disposable {
	private Sprite base;
	private Vector3 cursorLast;
	
	private boolean moving = false;
	private boolean disabled = false;
	
	public BaseManager (float startX, float startY) {
		Texture texture = new Texture(Gdx.files.internal("images/base.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		base = new Sprite(texture);
		base.setSize(200, 200);
		base.setPosition(startX, startY);
		
		cursorLast = new Vector3();
	}
	
	public void show (SpriteBatch batch) {
		if (!disabled) {
			base.draw(batch);
			if (Events.getInstance().isMouseLeftPressed()) {
				Vector3 cursor = CameraController.getInstance().unproject();
				if (Mathematics.getDistance(cursor.x, cursor.y, base.getX() + base.getWidth() / 2, base.getY() + base.getHeight() / 2) <= base.getWidth() / 2) {
					moving = true;
					cursorLast = new Vector3(cursor);
				}
			}
			if (moving) {
				Vector3 cursor = CameraController.getInstance().unproject();
				float x = cursor.x - cursorLast.x;
				float y = cursor.y - cursorLast.y;
				base.translate(x, y);
				cursorLast = new Vector3(cursor);
				if (Events.getInstance().isMouseLeftReleased()) {
					moving = false;
				}
			}
		}
	}
	
	public void setDisabled (boolean disabled) {
		this.disabled = disabled;
	}
	
	public Sprite getBase () {
		return base;
	}
	
	public boolean isDisabled () {
		return disabled;
	}
	
	@Override
	public void dispose () {
		base.getTexture().dispose();
	}
}