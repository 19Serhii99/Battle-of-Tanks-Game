package technique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import additional.Animation;
import map.Chunk;
import util.GeometricData;
import util.Mathematics;

public class Shell extends Sprite implements Disposable {
	private boolean isLive;
	private boolean isDie;
	private float distanceX;
	private float distanceY;
	private float speed;
	private float averageY;
	private int idSender;
	private int idRecipient;
	private int id;

	private GeometricData geometricData;
	private Array<Chunk> currentChunks;
	private Animation animation;
	private Animation flame;

	public Shell(Texture texture, Texture destroyTexture, int id, int idSender) {
		super(texture);
		this.id = id;
		this.idSender = idSender;
		this.isLive = true;
		this.currentChunks = new Array<Chunk>();
		this.animation = new Animation(destroyTexture, 5, 3, 180, 180, 0.1f);
	}

	public Shell(Texture texture, Texture destroyTexture, int id, int idSender, Texture flameTexture) {
		this(texture, destroyTexture, id, idSender);
		flame = new Animation(flameTexture, 8, 4, 64, 128, 0.01f);
		flame.setEndless(true);
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

	public void nextPoint(float x, float y, float nextX, float nextY) {
		super.setPosition(x - super.getWidth() / 2, y - super.getHeight() / 2);

		float tempX = x + super.getWidth() / 2;
		float tempY = y + super.getHeight() / 2;

		float distance = Mathematics.getDistance(tempX, tempY, nextX, nextY);
		float angle = MathUtils.atan2(nextY - tempY, nextX - tempX) * 180 / MathUtils.PI;

		if (angle < 0)
			angle += 360;

		super.setRotation(angle);

		if (flame != null) {
			flame.setSize(30, 150);
			flame.setPosition(
					super.getVertices()[Batch.X1] + (super.getVertices()[Batch.X2] - super.getVertices()[Batch.X1]) / 2
							- flame.getWidth() / 2,
					super.getVertices()[Batch.Y1] + (super.getVertices()[Batch.Y2] - super.getVertices()[Batch.Y1]) / 2
							- flame.getHeight() / 2);
			flame.setRotation(angle + 90);
		}

		distanceX = ((nextX - tempX) / distance) * speed;
		distanceY = ((nextY - tempY) / distance) * speed;

		averageY = super.getVertices()[Batch.Y3] + (super.getVertices()[Batch.Y4] - super.getVertices()[Batch.Y3]) / 2;

		geometricData = new GeometricData();
		geometricData.calculateData(getVertices()[Batch.X1], getVertices()[Batch.Y1], getVertices()[Batch.X2],
				getVertices()[Batch.Y2], getVertices()[Batch.X3], getVertices()[Batch.Y3], getVertices()[Batch.X4],
				getVertices()[Batch.Y4]);
	}

	public void moveTo(SpriteBatch batch) {
		if (!isDie) {
			float translateX = distanceX * Gdx.graphics.getDeltaTime();
			float translateY = distanceY * Gdx.graphics.getDeltaTime();
			super.translate(translateX, translateY);
			averageY += translateY;
			super.draw(batch);
			if (flame != null) {
				flame.setPosition(flame.getX() + translateX, flame.getY() + translateY);
				flame.show(batch);
			}
		} else {
			animation.show(batch);
			if (animation.isFinished()) {
				isLive = false;
			}
		}
	}

	public void die() {
		isDie = true;
		animation.setSize(180, 180);
		animation.setPosition(
				(super.getVertices()[Batch.X3] + super.getVertices()[Batch.X4]) / 2 - animation.getWidth() / 2,
				(super.getVertices()[Batch.Y3] + super.getVertices()[Batch.Y4]) / 2 - animation.getHeight() / 2);
	}

	public float getSpeed() {
		return speed;
	}

	public boolean isLive() {
		return isLive;
	}

	public boolean isDie() {
		return isDie;
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

	public Array<Chunk> getCurrentChunks() {
		return currentChunks;
	}

	public int getId() {
		return id;
	}

	@Override
	public void dispose() {
		super.getTexture().dispose();
	}
}