package technique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import net.Player;

public class Aim extends Sprite implements Disposable {
	private Player player;
	
	private float time = 0.0f;
	
	private float x;
	private float y;
	
	public Aim (Player player) {
		this.player = player;
		
		Texture texture = new Texture(Gdx.files.internal("images/aim.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		super.setRegion(texture);
		super.setSize(player.getTechnique().getTower().getMaxRadius() * 2, player.getTechnique().getTower().getMaxRadius() * 2);
	}
	
	public void show (SpriteBatch batch) {
		time += Gdx.graphics.getDeltaTime();
		
		float percent = (time * 100) / player.getTechnique().getTower().getTimeReduction();
		float size = player.getTechnique().getTower().getMaxRadius() * 2 - ((player.getTechnique().getTower().getMaxRadius() * 2 * percent) / 100);
		
		size = size < player.getTechnique().getTower().getMinRadius() * 2 ? player.getTechnique().getTower().getMinRadius() * 2 : size;
		
		super.setSize(size, size);
		super.setPosition(x - super.getWidth() / 2, y - super.getHeight() / 2);
		
		super.draw(batch);
	}
	
	public void reset () {
		super.setSize(player.getTechnique().getTower().getMaxRadius() * 2, player.getTechnique().getTower().getMaxRadius() * 2);
		time = 0;
	}
	
	public void setXY (float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX () {
		return x;
	}
	
	public float getY () {
		return y;
	}
	
	@Override
	public void dispose () {
		super.getTexture().dispose();
	}
}