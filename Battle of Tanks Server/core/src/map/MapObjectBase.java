package map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

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

	public MapObjectBase() {
		background = new Sprite();
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		this.background.setPosition(x, y);
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		this.background.setSize(width, height);
		this.background.setOriginCenter();
	}

	public void buildData() {
		final float[] v = background.getVertices();

		super.calculateData(v[Batch.X1], v[Batch.Y1], v[Batch.X2], v[Batch.Y2], v[Batch.X3], v[Batch.Y3], v[Batch.X4],
				v[Batch.Y4]);

		final float minX = Math.min(Math.min(v[Batch.X1], v[Batch.X2]), Math.min(v[Batch.X3], v[Batch.X4]));
		final float maxX = Math.max(Math.max(v[Batch.X1], v[Batch.X2]), Math.max(v[Batch.X3], v[Batch.X4]));

		final float minY = Math.min(Math.min(v[Batch.Y1], v[Batch.Y2]), Math.min(v[Batch.Y3], v[Batch.Y4]));
		final float maxY = Math.max(Math.max(v[Batch.Y1], v[Batch.Y2]), Math.max(v[Batch.Y3], v[Batch.Y4]));

		xAverage = minX + (maxX - minX) / 2;
		yAverage = minY + (maxY - minY) / 2;

		xWidth = minX + (maxX - minX);
		yHeight = minY + (maxY - minY);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Sprite getBackground() {
		return background;
	}

	public float getXWidth() {
		return xWidth;
	}

	public float getYHeight() {
		return yHeight;
	}

	public float getXAverage() {
		return xAverage;
	}

	public float getYAverage() {
		return yAverage;
	}

	@Override
	public boolean isWrapper() {
		return false;
	}
}