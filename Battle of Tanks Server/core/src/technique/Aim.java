package technique;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.server.Player;

import util.Mathematics;

public class Aim extends Sprite {
	private Player player;
	private Sprite line;
	
	private float time = 0.0f;
	
	private float x;
	private float y;
	
	public Aim (Player player) {
		this.player = player;
		
		super.setSize(player.getTechnique().getTower().getMaxRadius() * 2, player.getTechnique().getTower().getMaxRadius() * 2);
		
		line = new Sprite();
	}
	
	public void show (float delta) {
		time += delta;
		
		float percent = (time * 100) / player.getTechnique().getTower().getTimeReduction();
		float size = player.getTechnique().getTower().getMaxRadius() * 2 - ((player.getTechnique().getTower().getMaxRadius() * 2 * percent) / 100);
		
		size = size < player.getTechnique().getTower().getMinRadius() * 2 ? player.getTechnique().getTower().getMinRadius() * 2 : size;
		
		super.setSize(size, size);
		super.setPosition(x - super.getWidth() / 2, y - super.getHeight() / 2);
		
		float distance = Mathematics.getDistance(player.getTowerCenter().x, player.getTowerCenter().y, x, y);
		float widthLine = x - player.getTowerCenter().x;
		float heightLine = y - player.getTowerCenter().y;
		
		float angle = (float)(Math.atan2(heightLine, widthLine) * 180 / Math.PI);
		
		line.setSize(distance, 2);
		line.setRotation(angle);
		line.setPosition(player.getTowerCenter().x, player.getTowerCenter().y);
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
}