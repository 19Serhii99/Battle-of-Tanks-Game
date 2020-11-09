package technique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import util.TextureCreator;

public class Model implements Disposable {
	private Body body;
	private Tower tower;
	private Shooting shooting;
	private Axis axis;
	private MovingStatus movingStatus;
	
	private Sprite test;
	private Sprite test1;
	
	public Model (Texture bodyTexture, Texture towerTexture) {
		body = new Body(bodyTexture);
		tower = new Tower(towerTexture);
		axis = Axis.getInstance();
		
		test = new Sprite(TextureCreator.createTexture(Color.RED));
		test.setSize(5, 5);
		
		test1 = new Sprite(TextureCreator.createTexture(Color.GREEN));
		test1.setSize(5, 5);
	}
	
	public void createShooting (int countShells, Array <Vector2> gunPoints, Texture shellTexture) {
		shooting = new Shooting(countShells, gunPoints);
		shooting.setTexture(shellTexture);
	}
	
	public void moveForward () {
		float x = (body.getVertices()[Batch.X4] - body.getVertices()[Batch.X1]) / body.getWidth() * body.getSpeed() * Gdx.graphics.getDeltaTime();
		float y = (body.getVertices()[Batch.Y4] - body.getVertices()[Batch.Y1]) / body.getWidth() * body.getSpeed() * Gdx.graphics.getDeltaTime();
		
		body.translate(x, y);
		body.getCenterPoint().x += x;
		body.getCenterPoint().y += y;
		body.setSpeed(body.getSpeed() + body.getAcceleration() * Gdx.graphics.getDeltaTime());
		
		if (body.getSpeed() > body.getMaxSpeed()) body.setSpeed(body.getMaxSpeed());
		
		tower.movePoints(x, y);
		tower.translate(x, y);
		
		for (Vector2 point : shooting.getGunPoints()) {
			point.x += x;
			point.y += y;
		}
		
		movingStatus = MovingStatus.FORWARD;
		
		test1.setPosition(tower.getCenterPoint().x, tower.getCenterPoint().y);
	}
	
	public void moveBackward () {
		float x = (body.getVertices()[Batch.X1] - body.getVertices()[Batch.X4]) / body.getWidth() * body.getSpeed() * Gdx.graphics.getDeltaTime();
		float y = (body.getVertices()[Batch.Y1] - body.getVertices()[Batch.Y4]) / body.getWidth() * body.getSpeed() * Gdx.graphics.getDeltaTime();
		
		body.translate(x, y);
		body.getCenterPoint().x += x;
		body.getCenterPoint().y += y;
		body.setSpeed(body.getSpeed() + body.getAcceleration() * Gdx.graphics.getDeltaTime());
		
		if (body.getSpeed() > body.getMaxSpeed()) body.setSpeed(body.getMaxSpeed());
		
		tower.movePoints(x, y);
		tower.translate(x, y);
		
		for (Vector2 point : shooting.getGunPoints()) {
			point.x += x;
			point.y += y;
		}
		
		movingStatus = MovingStatus.BACKWARD;
	}
	
	public void slow () {
		float speed = body.getSpeed() - body.getAcceleration() * Gdx.graphics.getDeltaTime();
		body.setSpeed(speed < 0 ? 0 : speed);
	}
	
	public void moveForwardLeft () {
		moveForward();
		turnLeft();
		movingStatus = MovingStatus.FORWARD_LEFT;
	}
	
	public void moveForwardRight () {
		moveForward();
		turnRight();
		movingStatus = MovingStatus.FORWARD_RIGHT;
	}
	
	public void moveBackwardLeft () {
		moveBackward();
		turnRight();
		movingStatus = MovingStatus.BACKWARD_LEFT;
	}
	
	public void moveBackwardRight () {
		moveBackward();
		turnLeft();
		movingStatus = MovingStatus.BACKWARD_RIGHT;
	}
	
	public void turnLeft () {
		float rotation = body.getRotation() + body.getSpeedRotation() * Gdx.graphics.getDeltaTime();
		
		if (rotation >= 360) rotation -= 360;
		else if (rotation < 0) rotation += 360;
		
		body.setRotation(rotation);
		
		axis.turnAxis(body.getSpeedRotation() * Gdx.graphics.getDeltaTime());
		
		Vector2 pointTemp = axis.turnPoint(body.getCenterPoint().x, body.getCenterPoint().y, tower.getCenterPoint().x, tower.getCenterPoint().y);
		
		for (Vector2 point : shooting.getGunPoints()) {
			point.x += pointTemp.x - tower.getCenterPoint().x;
			point.y += pointTemp.y - tower.getCenterPoint().y;
		}
		
		tower.getCenterPoint().set(pointTemp.x, pointTemp.y);
		tower.setPosition(tower.getCenterPoint().x - tower.getOriginX(), tower.getCenterPoint().y - tower.getOriginY());
	}
	
	public void turnRight () {
		float rotation = body.getRotation() - body.getSpeedRotation() * Gdx.graphics.getDeltaTime();
		
		if (rotation >= 360) rotation -= 360;
		else if (rotation < 0) rotation += 360;
		
		body.setRotation(rotation);
		axis.turnAxis(-body.getSpeedRotation() * Gdx.graphics.getDeltaTime());
		
		Vector2 pointTemp = axis.turnPoint(body.getCenterPoint().x, body.getCenterPoint().y, tower.getCenterPoint().x, tower.getCenterPoint().y);
		
		for (Vector2 point : shooting.getGunPoints())  {
			point.x += pointTemp.x - tower.getCenterPoint().x;
			point.y += pointTemp.y - tower.getCenterPoint().y;
		}
		
		tower.getCenterPoint().set(pointTemp.x, pointTemp.y);
		tower.setPosition(tower.getCenterPoint().x - tower.getOriginX(), tower.getCenterPoint().y - tower.getOriginY());
	}
	
	public void turnTower (float x, float y) {
		float rotation = MathUtils.atan2(y - tower.getCenterPoint().y, x - tower.getCenterPoint().x) * 180 / MathUtils.PI;
		float speedTemp = tower.getSpeedRotation() * Gdx.graphics.getDeltaTime();
		
		if (rotation < 0) rotation += 360;
		
		if (Math.abs(rotation - tower.getRotation()) > speedTemp) {
			if (rotation >= tower.getRotation()) {
				if (rotation - tower.getRotation() >= 180) {
					rotation = tower.getRotation() - speedTemp;
					axis.turnAxis(-speedTemp);
				} else {
					rotation = tower.getRotation() + speedTemp;
					axis.turnAxis(speedTemp);
				}
			} else {
				if (tower.getRotation() - rotation >= 180) {
					rotation = tower.getRotation() + speedTemp;
					axis.turnAxis(speedTemp);
				} else {
					rotation = tower.getRotation() - speedTemp;
					axis.turnAxis(-speedTemp);
				}
			}
		} else {
			float angle = rotation - tower.getRotation();
			axis.turnAxis(angle);
		}
		
		for (Vector2 point : shooting.getGunPoints()) {
			Vector2 pointTemp = axis.turnPoint(tower.getCenterPoint().x, tower.getCenterPoint().y, point.x, point.y);
			point.x = pointTemp.x;
			point.y = pointTemp.y;
			test.setPosition(point.x, point.y);
		}
		
		if (rotation >= 360) rotation -= 360;
		else if (rotation < 0) rotation += 360;
		
		tower.setRotation(rotation);
	}
	
	public Body getBody () {
		return body;
	}
	
	public Tower getTower () {
		return tower;
	}
	
	public MovingStatus getMovingStatus () {
		return movingStatus;
	}
	
	public Sprite getTest () {
		return test;
	}
	
	public Sprite getTest1 () {
		return test1;
	}
	
	public Shooting getShooting () {
		return shooting;
	}
	
	@Override
	public void dispose () {
		body.dispose();
		tower.dispose();
	}
}