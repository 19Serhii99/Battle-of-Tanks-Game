package map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import util.GeometricData;

public class MapObjectBase extends GeometricData implements Wrapper {
	private float x;
	private float y;
	private float width;
	private float height;
	private float xWidth;
	private float yHeight;
	private float xAverage;
	private float yAverage;
	
	protected Sprite background;
	
	public MapObjectBase () {
		background = new Sprite();
	}
	
	public void setPosition (float x, float y) {
		this.x = x;
		this.y = y;
		this.background.setPosition(x, y);
	}
	
	public void setSize (float width, float height) {
		this.width = width;
		this.height = height;
		this.background.setSize(width, height);
		this.background.setOriginCenter();
	}
	
	public void buildData () {
		super.calculateData(background.getVertices()[Batch.X1], background.getVertices()[Batch.Y1], background.getVertices()[Batch.X2], background.getVertices()[Batch.Y2],
				background.getVertices()[Batch.X3], background.getVertices()[Batch.Y3], background.getVertices()[Batch.X4], background.getVertices()[Batch.Y4]);
		Rectangle rectangle = background.getBoundingRectangle();
		xWidth = rectangle.getX() + rectangle.getWidth();
		yHeight = rectangle.getY() + rectangle.getHeight();
		xAverage = rectangle.getX() + rectangle.getWidth() / 2;
		yAverage = rectangle.getY() + rectangle.getHeight() / 2;
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
	
	public Sprite getBackground () {
		return background;
	}
	
	public float getXWidth () {
		return xWidth;
	}
	
	public float getYHeight () {
		return yHeight;
	}
	
	public float getXAverage () {
		return xAverage;
	}
	
	public float getYAverage () {
		return yAverage;
	}
	
	@Override
	public boolean isWrapper () {
		return false;
	}
}