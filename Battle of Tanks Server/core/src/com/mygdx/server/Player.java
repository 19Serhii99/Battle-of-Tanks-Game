package com.mygdx.server;

import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import answers.CreateShellsAnswer;
import answers.MyLocalAnswer;
import answers.OtherPlayerLocalAnswer;
import answers.OtherPlayersLocalAnswer;
import map.Chunk;
import map.Map;
import net.UDP;
import net.UDPInputObject;
import net.UDPOutputObject;
import queries.BattlePlayerData;
import queries.UDPData;
import technique.Aim;
import technique.MovingStatus;
import technique.Shell;
import technique.Shooting;
import technique.Technique;
import util.Axis;

public class Player {
	private volatile ArrayDeque <UDPInputObject> UDPObjects;
	private volatile ArrayDeque <Object> TCPObjects;
	private UserHandler user;
	private Technique technique;
	private Vector2 corpsCenter;
	private Vector2 towerCenter;
	private MovingStatus movingStatus;
	private Shooting shooting;
	private Axis axis;
	private Aim aim;
	
	private InetAddress inetAddressUDP;
	private int portUDP;
	
	private int id;
	private int currentHealth;
	private float currentSpeed;
	private float delta;
	private boolean isReady;
	private boolean isConnected;
	private boolean isLeftSide;
	private boolean isLive = true;
	private boolean updated = false;
	
	public Player (Technique technique, UserHandler user, int id, LinkedList <Shell> shells) {
		this.technique = technique;
		this.user = user;
		this.UDPObjects = new ArrayDeque <UDPInputObject>();
		this.TCPObjects = new ArrayDeque <Object>();
		this.id = id;
		this.axis = new Axis();
		this.shooting = new Shooting(technique, id);
		this.shooting.setShellsList(shells);
		if (technique.getTower().getGun().isCassette())
			this.shooting.setShootingStyle(technique.getTower().getGun().getCassette().getShootingStyle());
		this.aim = new Aim(this);
		this.aim.setSize(technique.getTower().getMaxRadius() * 2, technique.getTower().getMaxRadius() * 2);
		this.currentHealth = technique.getCorps().getMaxHealth();
	}
	
	public void setStartPosition (float x, float y, Map map) {
		technique.getCorpsBackground().setPosition(x, y);
		technique.getTowerBackground().setPosition(technique.getCorpsBackground().getX() + technique.getTower().getX(), technique.getCorpsBackground().getY() + technique.getTower().getY());
		technique.getTowerBackground().setOrigin(technique.getTower().getOriginX(), technique.getTower().getOriginY());
		
		corpsCenter = new Vector2(technique.getCorpsBackground().getX() + technique.getCorpsBackground().getWidth() / 2,
				technique.getCorpsBackground().getY() + technique.getCorpsBackground().getHeight() / 2);
		towerCenter = new Vector2(technique.getTowerBackground().getX() + technique.getTowerBackground().getOriginX(),
				technique.getTowerBackground().getY() + technique.getTowerBackground().getOriginY());
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += x;
			point.y += y;
		}
		
		aim.setXY(towerCenter.x + 200, towerCenter.y);
		
		float v[] = technique.getCorpsBackground().getVertices();
		
		for (Chunk chunk : map.getChunkGroup().getChunks()) {
			float width = chunk.isRightLast() ? chunk.getVirtualWidth() : map.getChunkGroup().getChunkWidth();
			float height = chunk.isBottomLast() ? chunk.getVirtualHeight() : map.getChunkGroup().getChunkHeight();
			if ((v[Batch.X1] >= chunk.getX() && v[Batch.X1] <= chunk.getX() + width && v[Batch.Y1] >= chunk.getY() && v[Batch.Y1] <= chunk.getY() + height)
					|| (v[Batch.X2] >= chunk.getX() && v[Batch.X2] <= chunk.getX() + width && v[Batch.Y2] >= chunk.getY() && v[Batch.Y2] <= chunk.getY() + height)
					|| (v[Batch.X3] >= chunk.getX() && v[Batch.X3] <= chunk.getX() + width && v[Batch.Y3] >= chunk.getY() && v[Batch.Y3] <= chunk.getY() + height)
					|| (v[Batch.X4] >= chunk.getX() && v[Batch.X4] <= chunk.getX() + width && v[Batch.Y4] >= chunk.getY() && v[Batch.Y4] <= chunk.getY() + height)) {
				boolean has = false;
				for (Chunk ownChunk : technique.getCurrentChunks()) {
					if (chunk.equals(ownChunk)) {
						has = true;
						break;
					}
				}
				if (!has) {
					technique.getCurrentChunks().add(chunk);
				}
			}
		}
	}
	
	public int hit (int damage) {
		int temp = currentHealth - damage;
		if (temp < 0) damage += temp;
		
		currentHealth -= damage;
		if (currentHealth == 0) isLive = false;
		
		return damage;
	}
	
	public void addUDPInputObject (UDPInputObject object) {
		UDPObjects.add(object);
	}
	
	public void addTCPInputObject (Object object) {
		TCPObjects.add(object);
	}
	
	public void readinessTest () {
		Iterator <Object> objectIterator = TCPObjects.iterator();
		while (objectIterator.hasNext()) {
			Object object = objectIterator.next();
			if (object.getClass() == UDPData.class) {
				UDPData data = (UDPData) object;
				inetAddressUDP = data.getInetAddress();
				portUDP = data.getPort();
				isReady = true;
			}
			objectIterator.remove();
		}
	}
	
	public void act (UDP udp, CreateShellsAnswer createShellsAnswer, ArrayList <Player> players) {
		while (UDPObjects.size() > 0) {
			UDPInputObject object = UDPObjects.poll();
			if (object != null) {
				if (object.getObject().getClass() == BattlePlayerData.class) {
					BattlePlayerData data = (BattlePlayerData) object.getObject();
					delta = data.getDelta();
					if (isLive) {
						boolean isW = false;
						boolean isS = false;
						boolean isA = false;
						boolean isD = false;
						for (String key : data.getKeys()) {
							if (key.equals("w")) isW = true;
							if (key.equals("s")) isS = true;
							if (key.equals("a")) isA = true;
							if (key.equals("d")) isD = true;
						}
						if (isW || isS) {
							if (isW && !isA && !isD) {
								moveForward();
							} else if (isW && isA && !isD) {
								moveForwardLeft();
							} else if (isW && !isA && isD) {
								moveForwardRight();
							}
							if (isS && !isA && !isD) {
								moveBackward();
							} else if (isS && isA && !isD) {
								moveBackwardLeft();
							} else if (isS && !isA && isD) {
								moveBackwardRight();
							}
							aim.reset();
						} else {
							if (isA && !isD) {
								turnLeft();
								aim.reset();
							} else if (!isA && isD) {
								turnRight();
								aim.reset();
							}
						}
						turnTower(data.getCursorX(), data.getCursorY());
						aim.setXY(data.getCursorX(), data.getCursorY());
						aim.show(delta);
						if (data.isMouseLeftPressed()) {
							aim.reset();
							shooting.createShell(MathUtils.random(aim.getX() - aim.getWidth() / 2, aim.getX() + aim.getWidth() / 2), MathUtils.random(aim.getY() - aim.getHeight() / 2, aim.getY() + aim.getHeight() / 2), aim,
									createShellsAnswer, technique.getCurrentChunks());
						}
						MyLocalAnswer answer = new MyLocalAnswer(technique.getCorpsBackground().getX(), technique.getCorpsBackground().getY(), technique.getCorpsBackground().getRotation(), technique.getTowerBackground().getRotation(),
								towerCenter.x, towerCenter.y);
						UDPOutputObject objectOutput = new UDPOutputObject(inetAddressUDP, portUDP, answer);
						objectOutput.sendObject(udp.getSocket());
					}
					OtherPlayersLocalAnswer other = new OtherPlayersLocalAnswer();
					for (int i = 0; i < players.size(); i++) {
						Player player = players.get(i);
						if (player.getId() != id && player.isLive) {
							other.getPlayers().add(new OtherPlayerLocalAnswer(player.getId(), player.getTechnique().getCorpsBackground().getX(), player.getTechnique().getCorpsBackground().getY(),
									player.getTechnique().getCorpsBackground().getRotation(), player.getTechnique().getTowerBackground().getRotation(), player.getTowerCenter().x, player.getTowerCenter().y));
						}
					}
					UDPOutputObject objectOutput = new UDPOutputObject(inetAddressUDP, portUDP, other);
					objectOutput.sendObject(udp.getSocket());
				}
				updated = true;
			}
		}
	}
	
	public void moveForward () {
		Sprite background = technique.getCorpsBackground();
		float x = (background.getVertices()[Batch.X4] - background.getVertices()[Batch.X1]) / background.getWidth() * currentSpeed;
		float y = (background.getVertices()[Batch.Y4] - background.getVertices()[Batch.Y1]) / background.getWidth() * currentSpeed;
		
		background.translate(x, y);
		technique.getTowerBackground().translate(x, y);
		
		corpsCenter.x += x;
		corpsCenter.y += y;
		towerCenter.x += x;
		towerCenter.y += y;
		
		float tempSpeed = (currentSpeed + technique.getCorps().getAcceleration()) * delta;
		currentSpeed = tempSpeed > technique.getCorps().getMaxSpeed() ? technique.getCorps().getMaxSpeed() : tempSpeed;
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += x;
			point.y += y;
		}
		
		movingStatus = MovingStatus.FORWARD;
	}
	
	public void moveBackward () {
		Sprite background = technique.getCorpsBackground();
		float x = (background.getVertices()[Batch.X1] - background.getVertices()[Batch.X4]) / background.getWidth() * currentSpeed;
		float y = (background.getVertices()[Batch.Y1] - background.getVertices()[Batch.Y4]) / background.getWidth() * currentSpeed;
		
		background.translate(x, y);
		technique.getTowerBackground().translate(x, y);
		
		corpsCenter.x += x;
		corpsCenter.y += y;
		towerCenter.x += x;
		towerCenter.y += y;
		
		float tempSpeed = (currentSpeed + technique.getCorps().getAcceleration()) * delta;
		currentSpeed = tempSpeed > technique.getCorps().getMaxSpeed() ? technique.getCorps().getMaxSpeed() : tempSpeed;
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += x;
			point.y += y;
		}
		
		movingStatus = MovingStatus.BACKWARD;
	}
	
	public void slow () {
		currentSpeed = 0;
	}
	
	public void setUpdated (boolean updated) {
		this.updated = updated;
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
		float rotation = technique.getCorpsBackground().getRotation() + technique.getCorps().getSpeedRotation() * delta;
		
		if (rotation >= 360) rotation -= 360;
		else if (rotation < 0) rotation += 360;
		
		technique.getCorpsBackground().setRotation(rotation);
		axis.turnAxis(technique.getCorps().getSpeedRotation() * delta);
		
		Vector2 pointTemp = axis.turnPoint(corpsCenter.x, corpsCenter.y, towerCenter.x, towerCenter.y);
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += pointTemp.x - towerCenter.x;
			point.y += pointTemp.y - towerCenter.y;
		}
		
		towerCenter.set(pointTemp.x, pointTemp.y);
		technique.getTowerBackground().setPosition(towerCenter.x - technique.getTower().getOriginX(), towerCenter.y - technique.getTower().getOriginY());
	}
	
	public void turnRight () {
		float rotation = technique.getCorpsBackground().getRotation() - technique.getCorps().getSpeedRotation() * delta;
		
		if (rotation >= 360) rotation -= 360;
		else if (rotation < 0) rotation += 360;
		
		technique.getCorpsBackground().setRotation(rotation);
		axis.turnAxis(-technique.getCorps().getSpeedRotation() * delta);
		
		Vector2 pointTemp = axis.turnPoint(corpsCenter.x, corpsCenter.y, towerCenter.x, towerCenter.y);
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += pointTemp.x - towerCenter.x;
			point.y += pointTemp.y - towerCenter.y;
		}
		
		towerCenter.set(pointTemp.x, pointTemp.y);
		technique.getTowerBackground().setPosition(towerCenter.x - technique.getTower().getOriginX(), towerCenter.y - technique.getTower().getOriginY());
	}
	
	public void turnTower (float x, float y) {
		float rotation = MathUtils.atan2(y - towerCenter.y, x - towerCenter.x) * 180 / MathUtils.PI;
		float speedTemp = technique.getTower().getSpeedRotation() * delta;
		
		if (rotation < 0) rotation += 360;
		
		if (Math.abs(rotation - technique.getTowerBackground().getRotation()) > speedTemp) {
			if (rotation >= technique.getTowerBackground().getRotation()) {
				if (rotation - technique.getTowerBackground().getRotation() >= 180) {
					rotation = technique.getTowerBackground().getRotation() - speedTemp;
					axis.turnAxis(-speedTemp);
				} else {
					rotation = technique.getTowerBackground().getRotation() + speedTemp;
					axis.turnAxis(speedTemp);
				}
			} else {
				if (technique.getTowerBackground().getRotation() - rotation >= 180) {
					rotation = technique.getTowerBackground().getRotation() + speedTemp;
					axis.turnAxis(speedTemp);
				} else {
					rotation = technique.getTowerBackground().getRotation() - speedTemp;
					axis.turnAxis(-speedTemp);
				}
			}
		} else {
			float angle = rotation - technique.getTowerBackground().getRotation();
			axis.turnAxis(angle);
		}
		
		for (Vector2 point : technique.getGunPoints()) {
			Vector2 pointTemp = axis.turnPoint(towerCenter.x, towerCenter.y, point.x, point.y);
			point.x = pointTemp.x;
			point.y = pointTemp.y;
		}
		
		if (rotation >= 360) rotation -= 360;
		else if (rotation < 0) rotation += 360;
		
		technique.getTowerBackground().setRotation(rotation);
	}
	
	public void translate (float x, float y) {
		corpsCenter.x += x;
		corpsCenter.y += y;
		towerCenter.x += x;
		towerCenter.y += y;
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += x;
			point.y += y;
		}
		
		technique.getCorpsBackground().translate(x, y);
		technique.getTowerBackground().translate(x, y);
	}
	
	public void rotate (float rotation) {
		technique.getCorpsBackground().rotate(rotation);
		axis.turnAxis(rotation);
		
		Vector2 pointTemp = axis.turnPoint(corpsCenter.x, corpsCenter.y, towerCenter.x, towerCenter.y);
		
		for (Vector2 point : technique.getGunPoints()) {
			point.x += pointTemp.x - towerCenter.x;
			point.y += pointTemp.y - towerCenter.y;
		}
		
		towerCenter.set(pointTemp.x, pointTemp.y);
		technique.getTowerBackground().setPosition(towerCenter.x - technique.getTower().getOriginX(), towerCenter.y - technique.getTower().getOriginY());
		
		for (Vector2 point : technique.getGunPoints()) {
			pointTemp = axis.turnPoint(towerCenter.x, towerCenter.y, point.x, point.y);
			point.x = pointTemp.x;
			point.y = pointTemp.y;
		}
		
		technique.getTowerBackground().rotate(rotation);
	}
	
	public void setLive (boolean live) {
		this.isLive = live;
	}
	
	public void setConnected (boolean connected) {
		isConnected = connected;
	}
	
	public void setLeftSide (boolean value) {
		isLeftSide = value;
	}
	
	public UserHandler getUser () {
		return user;
	}
	
	public Technique getTechnique () {
		return technique;
	}
	
	public int getId () {
		return id;
	}
	
	public Vector2 getCorpsCenter () {
		return corpsCenter;
	}
	
	public Vector2 getTowerCenter () {
		return towerCenter;
	}
	
	public MovingStatus getMovingStatus () {
		return movingStatus;
	}
	
	public InetAddress getInetAddress () {
		return inetAddressUDP;
	}
	
	public Shooting getShooting () {
		return shooting;
	}
	
	public int getPort () {
		return portUDP;
	}
	
	public boolean isLive () {
		return isLive;
	}
	
	public boolean isReady () {
		return isReady;
	}
	
	public boolean isConnected () {
		return isConnected;
	}
	
	public boolean isLeftSide () {
		return isLeftSide;
	}
	
	public float getDelta () {
		return delta;
	}
	
	public boolean isUpdated () {
		return updated;
	}
	
	public int getHealth () {
		return currentHealth;
	}
}