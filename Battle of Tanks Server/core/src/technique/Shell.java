package technique;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import map.Chunk;
import util.GeometricData;
import util.Mathematics;

public class Shell extends Sprite {
	private boolean isLive;
	private float distanceX;
	private float distanceY;
	private float speed;
	private float averageY;
	private float nextX;
	private float nextY;
	private int id;
	private int idSender;
	private int idRecipient;

	private GeometricData geometricData;
	private ArrayList<Chunk> currentChunks;

	public Shell(int id, int idSender) {
		this.id = id;
		this.idSender = idSender;
		this.isLive = true;
		this.currentChunks = new ArrayList<Chunk>();
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setEnableLive(boolean value) {
		isLive = value;
	}

	public void setIdRecipient(int id) {
		idRecipient = id;
	}

	public void nextPoint(float x, float y, float nextX, float nextY, ArrayList<Chunk> chunks) {
		super.setPosition(x - super.getWidth() / 2, y - super.getHeight() / 2);

		float tempX = x + super.getWidth() / 2;
		float tempY = y + super.getHeight() / 2;

		float distance = Mathematics.getDistance(tempX, tempY, nextX, nextY);
		float angle = MathUtils.atan2(nextY - tempY, nextX - tempX) * 180 / MathUtils.PI;

		if (angle < 0)
			angle += 360;

		super.setRotation(angle);

		distanceX = ((nextX - tempX) / distance) * speed;
		distanceY = ((nextY - tempY) / distance) * speed;

		averageY = super.getVertices()[Batch.Y3] + (super.getVertices()[Batch.Y4] - super.getVertices()[Batch.Y3]) / 2;

		geometricData = new GeometricData();
		geometricData.calculateData(getVertices()[Batch.X1], getVertices()[Batch.Y1], getVertices()[Batch.X2],
				getVertices()[Batch.Y2], getVertices()[Batch.X3], getVertices()[Batch.Y3], getVertices()[Batch.X4],
				getVertices()[Batch.Y4]);

		for (Chunk chunk : chunks)
			currentChunks.add(chunk);

		this.nextX = nextX;
		this.nextY = nextY;
	}

	public void moveTo(float delta) {
		float translateX = distanceX * delta;
		float translateY = distanceY * delta;
		super.translate(translateX, translateY);
		averageY += translateY;
	}

	public void die() {
		isLive = false;
	}

	public float getSpeed() {
		return speed;
	}

	public boolean isLive() {
		return isLive;
	}

	public int getIdSender() {
		return idSender;
	}

	public int getIdRecipient() {
		return idRecipient;
	}

	public float getAverageY() {
		return averageY;
	}

	public ArrayList<Chunk> getCurrentChunks() {
		return currentChunks;
	}

	public int getId() {
		return id;
	}

	public float getNextX() {
		return nextX;
	}

	public float getNextY() {
		return nextY;
	}
}