package net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.DeathsBlock;

import additional.Animation;
import answers.BattleEndedAnswer;
import answers.CreateShellsAnswer;
import answers.Hit;
import answers.MyLocalAnswer;
import answers.OtherPlayerLocalAnswer;
import answers.OtherPlayersLocalAnswer;
import answers.RemoveShellsAnswer;
import answers.ShellAnswer;
import gui.Label;
import map.Map;
import queries.BattlePlayerData;
import technique.Aim;
import technique.Gun;
import technique.MovingStatus;
import technique.Shell;
import technique.Technique;
import technique.TechniqueType;
import util.CameraController;
import util.Font;
import util.TextureCreator;

public class Player implements Disposable {
	private ArrayDeque<UDPInputObject> UDPObjects;
	private ArrayDeque<Object> TCPObjects;
	private Technique technique;
	private Vector2 corpsCenter;
	private Vector2 towerCenter;
	private MovingStatus movingStatus;

	private Animation destroyAnimation;
	private Sprite healthLine;
	private Label name;
	private Label hp;
	private String playerName;
	private Music tankWaiting;
	private Music tankMoving1;
	private Music tankMoving2;
	private Music tankMoving3;
	private Music destroy;
	private Texture destroyShellTexture;
	private Texture flameTexture;
	private Aim aim;

	private Array<Long> sounds;
	private Label hps;
	private BitmapFont hpsFont;

	private int id;
	private int currentHealth;
	private boolean isStartPosition;
	private boolean isLive = true;
	private boolean isLeftSide;
	private boolean isMe;
	private long timeHps;
	private float alphaHps = 1.0f;

	private int gotExperience;
	private int gotMoney;

	private boolean isBattleFinished;
	private boolean isWon;

	public Player(Technique technique, int id, Texture destroyTexture, Texture destroyShellTexture, String playerName) {
		this.technique = technique;
		this.id = id;
		this.UDPObjects = new ArrayDeque<UDPInputObject>();
		this.TCPObjects = new ArrayDeque<Object>();
		this.playerName = playerName;
		this.currentHealth = technique.getCorps().getMaxHealth();
		this.destroyShellTexture = destroyShellTexture;

		this.destroyAnimation = new Animation(destroyTexture, 8, 4, 256, 256, 0.1f);

		this.tankWaiting = Gdx.audio.newMusic(Gdx.files.internal("sounds/tank_wait.mp3"));
		this.tankWaiting.setLooping(true);
		this.tankWaiting.setVolume(0.3f);

		this.tankMoving1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/tank_move1.mp3"));
		this.tankMoving2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/tank_move2.mp3"));
		this.tankMoving3 = Gdx.audio.newMusic(Gdx.files.internal("sounds/tank_stop.mp3"));

		this.tankMoving1.setVolume(0.3f);
		this.tankMoving2.setVolume(0.3f);
		this.tankMoving3.setVolume(0.3f);

		this.destroy = Gdx.audio.newMusic(Gdx.files.internal("sounds/destroy.mp3"));

		this.sounds = new Array<Long>();

		this.hpsFont = Font.getInstance().generateBitmapFont(Color.RED, 25);

		this.aim = new Aim(this);
		this.aim.setSize(technique.getTower().getMaxRadius() * 2, technique.getTower().getMaxRadius() * 2);
	}

	public void setFlameTexture(Texture flameTexture) {
		this.flameTexture = flameTexture;
	}

	public void addUDPInputObject(UDPInputObject object) {
		UDPObjects.add(object);
	}

	public void addTCPInputObject(Object object) {
		TCPObjects.add(object);
	}

	public void act(DatagramSocket socket, String host, int portUDP, Map map, Array<Player> players,
			LinkedList<Shell> shells, PlayersTable table, DeathsBlock deathsBlock) {
		BattlePlayerData battlePlayerData = new BattlePlayerData(Gdx.graphics.getDeltaTime());
		if (!(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT))) {
			if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.S)) {
				if (Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.A)
						&& !Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("w");
				} else if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.A)
						&& !Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("w");
					battlePlayerData.getKeys().add("a");
				} else if (Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.A)
						&& Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("w");
					battlePlayerData.getKeys().add("d");
				}
				if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)
						&& !Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("s");
				} else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.A)
						&& !Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("s");
					battlePlayerData.getKeys().add("a");
				} else if (Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A)
						&& Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("s");
					battlePlayerData.getKeys().add("d");
				}
				aim.reset();
			} else {
				if (Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("a");
					aim.reset();
				} else if (!Gdx.input.isKeyPressed(Keys.A) && Gdx.input.isKeyPressed(Keys.D)) {
					battlePlayerData.getKeys().add("d");
					aim.reset();
				}
			}
		}

		Vector3 cursorPosition = CameraController.getInstance().unproject();
		battlePlayerData.setCursorPosition(cursorPosition.x, cursorPosition.y);
		battlePlayerData.setMouseLeftPressed(Gdx.input.isButtonPressed(Buttons.LEFT));

		aim.setXY(cursorPosition.x, cursorPosition.y);

		try {
			final UDPOutputObject battlePlayerDataObject = new UDPOutputObject(InetAddress.getByName(host), portUDP,
					battlePlayerData);
			battlePlayerDataObject.sendObject(socket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		while (UDPObjects.size() > 0) {
			final Object object = UDPObjects.poll().getObject();
			if (object != null) {
				if (object.getClass() == MyLocalAnswer.class) {
					MyLocalAnswer answer = (MyLocalAnswer) object;
					technique.getCorpsBackground().setPosition(answer.getX(), answer.getY());
					technique.getCorpsBackground().setRotation(answer.getRotation());
					technique.getTowerBackground().setRotation(answer.getRotationTower());
					technique.getTowerBackground().setPosition(answer.getXTower() - technique.getTower().getOriginX(),
							answer.getYTower() - technique.getTower().getOriginY());
					CameraController camera = CameraController.getInstance();
					if (technique.getCorps().getTechniqueType() == TechniqueType.TANK) {
						float cameraX = technique.getCorpsBackground().getX();
						float cameraY = technique.getCorpsBackground().getY();
						if (cameraX < camera.getWidth() / 2) {
							cameraX = camera.getWidth() / 2;
						} else if (cameraX > map.getChunkGroup().getMapWidth() - camera.getWidth() / 2) {
							cameraX = map.getChunkGroup().getMapWidth() - camera.getWidth() / 2;
						}
						if (cameraY < camera.getHeight() / 2) {
							cameraY = camera.getHeight() / 2;
						} else if (cameraY > map.getChunkGroup().getMapHeight() - camera.getHeight() / 2) {
							cameraY = map.getChunkGroup().getMapHeight() - camera.getHeight() / 2;
						}
						float tempY = camera.getY();
						camera.setPosition(cameraX, cameraY);
						deathsBlock.translate(cameraY - tempY);
					}
					if (!isStartPosition) {
						isStartPosition = true;
						map.getChunkGroup().lookingForActiveChunks(camera.getX() - camera.getWidth() / 2,
								camera.getY() + camera.getHeight() / 2, camera.getWidth(), camera.getHeight());
					}
				} else if (object.getClass() == CreateShellsAnswer.class) {
					CreateShellsAnswer answer = (CreateShellsAnswer) object;
					for (ShellAnswer shellAnswer : answer.getShells()) {
						for (Player player : players) {
							if (shellAnswer.getIdSender() == player.getId()) {
								Gun gun = player.getTechnique().getTower().getGun();
								Shell shell;
								if (technique.getCorps().getTechniqueType() == TechniqueType.REACTIVE_SYSTEM
										|| technique.getCorps()
												.getTechniqueType() == TechniqueType.FLAMETHROWER_SYSTEM) {
									shell = new Shell(gun.getShellTexture(), destroyShellTexture, shellAnswer.getId(),
											shellAnswer.getIdSender(), flameTexture);
								} else {
									shell = new Shell(gun.getShellTexture(), destroyShellTexture, shellAnswer.getId(),
											shellAnswer.getIdSender());
								}
								if (shell.getIdSender() == id)
									aim.reset();
								shell.setSpeed(gun.getShellSpeed());
								shell.setSize(gun.getShellWidth(), gun.getShellHeight());
								shell.setOriginCenter();
								shell.nextPoint(shellAnswer.getX(), shellAnswer.getY(), shellAnswer.getNextX(),
										shellAnswer.getNextY());
								shells.add(shell);
								sounds.add(gun.getShotSound().play());
								if (sounds.size == 4) {
									gun.getShotSound().stop(sounds.get(0));
									sounds.removeIndex(0);
								}
								break;
							}
						}
					}
				} else if (object.getClass() == OtherPlayersLocalAnswer.class) {
					OtherPlayersLocalAnswer answer = (OtherPlayersLocalAnswer) object;
					for (OtherPlayerLocalAnswer other : answer.getPlayers()) {
						for (Player player : players) {
							if (player.getId() == other.getId()) {
								player.getTechnique().getCorpsBackground().setPosition(other.getX(), other.getY());
								player.getTechnique().getCorpsBackground().setRotation(other.getRotation());
								player.getTechnique().getTowerBackground().setRotation(other.getRotationTower());
								player.getTechnique().getTowerBackground().setPosition(
										other.getXTower() - player.getTechnique().getTower().getOriginX(),
										other.getYTower() - player.getTechnique().getTower().getOriginY());
								break;
							}
						}
					}
				} else if (object.getClass() == RemoveShellsAnswer.class) {
					RemoveShellsAnswer answer = (RemoveShellsAnswer) object;
					for (Integer shell : answer.getShells()) {
						for (int i = 0; i < shells.size(); i++) {
							if (shell == shells.get(i).getId()) {
								shells.get(i).die();
							}
						}
					}
				} else if (object.getClass() == Hit.class) {
					Hit answer = (Hit) object;
					for (Player player : players) {
						if (player.getId() == answer.getIdRecipient()) {
							int damage = player.hit(answer.getDamage());
							if (table.getClass() == CommandPlayersTable.class) {
								for (int i = 0; i < players.size; i++) {
									if (players.get(i).getId() == answer.getIdSender()) {
										((CommandPlayersTable) table).addDamage(players.get(i), answer.isLeftSide(),
												damage, !player.isLive());
										if (!player.isLive()) {
											deathsBlock.addKilling(players.get(i).getNameLabel().getText(),
													player.getNameLabel().getText());
										}
										break;
									}
								}
							}
						}
					}
				} else if (object.getClass() == BattleEndedAnswer.class) {
					BattleEndedAnswer answer = (BattleEndedAnswer) object;
					gotExperience = answer.getExp();
					gotMoney = answer.getMoney();
					isWon = answer.isWon();
					isBattleFinished = true;
				}
			}
		}

		TechniqueType techniqueType = technique.getCorps().getTechniqueType();

		if (techniqueType == TechniqueType.ARTY || techniqueType == TechniqueType.REACTIVE_SYSTEM
				|| techniqueType == TechniqueType.FLAMETHROWER_SYSTEM) {
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				CameraController camera = CameraController.getInstance();
				if (Gdx.input.isKeyPressed(Keys.A)) {
					camera.translate(-500 * Gdx.graphics.getDeltaTime(), 0);
				}
				if (Gdx.input.isKeyPressed(Keys.D)) {
					camera.translate(500 * Gdx.graphics.getDeltaTime(), 0);
				}
				if (Gdx.input.isKeyPressed(Keys.W)) {
					camera.translate(0, 500 * Gdx.graphics.getDeltaTime());
				}
				if (Gdx.input.isKeyPressed(Keys.S)) {
					camera.translate(0, -500 * Gdx.graphics.getDeltaTime());
				}
				float cameraX = camera.getX();
				float cameraY = camera.getY();
				if (cameraX < camera.getWidth() / 2) {
					cameraX = camera.getWidth() / 2;
				} else if (cameraX > map.getChunkGroup().getMapWidth() - camera.getWidth() / 2) {
					cameraX = map.getChunkGroup().getMapWidth() - camera.getWidth() / 2;
				}
				if (cameraY < camera.getHeight() / 2) {
					cameraY = camera.getHeight() / 2;
				} else if (cameraY > map.getChunkGroup().getMapHeight() - camera.getHeight() / 2) {
					cameraY = map.getChunkGroup().getMapHeight() - camera.getHeight() / 2;
				}
				float tempY = camera.getY();
				camera.setPosition(cameraX, cameraY);
				deathsBlock.translate(cameraY - tempY);
			}
		}
	}

	public int hit(int damage) {
		int temp = currentHealth - damage;

		if (temp < 0) {
			damage += temp;
		}

		if (isLive) {
			if (hps == null) {
				hps = new Label("-" + damage, hpsFont);
			} else {
				StringBuffer text = new StringBuffer(hps.getText());
				text.deleteCharAt(0);
				int hp = Integer.parseInt(text.toString());
				hps.setText("-" + (hp + damage));
			}
			timeHps = System.currentTimeMillis();
			hps.setPosition(hp.getX() + hp.getWidth() + 20, hp.getY());
		}

		currentHealth -= damage;
		if (currentHealth == 0) {
			setDestroyedAmination();
			technique.getCorpsBackground().setRegion(technique.getCorps().getDestroyedTexture());
			technique.getTowerBackground().setRegion(technique.getTower().getDestroyedTexture());
			isLive = false;
		}

		float percent = (currentHealth * 100) / technique.getCorps().getMaxHealth();
		float width = (200 * percent) / 100;

		if (percent > 20 && percent < 50) {
			healthLine.getTexture().dispose();
			healthLine.setRegion(TextureCreator.createTexture(Color.ORANGE));
		} else if (percent < 20) {
			healthLine.getTexture().dispose();
			healthLine.setRegion(TextureCreator.createTexture(Color.RED));
		}

		healthLine.setSize(width, healthLine.getHeight());

		hp.setText(currentHealth + " hp");

		return damage;
	}

	public void createDataAroundPlayer(Color color) {
		healthLine = new Sprite(TextureCreator.createTexture(Color.GREEN));
		healthLine.setSize(200, 10);

		name = new Label(playerName, Font.getInstance().generateBitmapFont(color, 20));
		hp = new Label(currentHealth + " hp", name.getFont());
	}

	public void setDestroyedAmination() {
		destroy.play();
		destroyAnimation.setSize(500, 500);
		destroyAnimation.setPosition(
				technique.getCorpsBackground().getX()
						+ technique.getCorpsBackground().getBoundingRectangle().getWidth() / 2
						- destroyAnimation.getWidth() / 2,
				technique.getCorpsBackground().getY()
						+ technique.getCorpsBackground().getBoundingRectangle().getHeight() / 2
						- destroyAnimation.getHeight() / 2);
	}

	public void translate(float x, float y) {
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

	public void updateHps() {
		if (System.currentTimeMillis() - timeHps > 1500) {
			alphaHps -= Gdx.graphics.getDeltaTime() * 2;
			if (alphaHps < 0.0f) {
				alphaHps = 0.0f;
				hps = null;
			} else {
				hps.setAlphas(alphaHps);
			}
		} else {
			alphaHps = 1.0f;
			hps.setAlphas(alphaHps);
		}
	}

	public void rotate(float rotation) {
		technique.getCorpsBackground().rotate(rotation);
		technique.getTowerBackground().rotate(rotation);
	}

	public void setMe(boolean value) {
		isMe = value;
	}

	public void setLeftSide(boolean value) {
		isLeftSide = value;
	}

	public Technique getTechnique() {
		return technique;
	}

	public Vector2 getCorpsCenter() {
		return corpsCenter;
	}

	public Vector2 getTowerCenter() {
		return towerCenter;
	}

	public MovingStatus getMovingStatus() {
		return movingStatus;
	}

	public int getId() {
		return id;
	}

	public void setStartPositionEnabled(boolean enabled) {
		this.isStartPosition = enabled;
	}

	public boolean isStartPosition() {
		return isStartPosition;
	}

	public boolean isLeftSide() {
		return isLeftSide;
	}

	public Label getNameLabel() {
		return name;
	}

	public Sprite getHealthLine() {
		return healthLine;
	}

	public Label getHp() {
		return hp;
	}

	public boolean isMe() {
		return isMe;
	}

	public Animation getDestroyAnimation() {
		return destroyAnimation;
	}

	public boolean isLive() {
		return isLive;
	}

	public Label getHps() {
		return hps;
	}

	public int getGotExperience() {
		return gotExperience;
	}

	public int getGotMoney() {
		return gotMoney;
	}

	public boolean isBattleFinished() {
		return isBattleFinished;
	}

	public boolean isWon() {
		return isWon;
	}

	public Aim getAim() {
		return aim;
	}

	@Override
	public void dispose() {
		tankWaiting.dispose();
		tankMoving1.dispose();
		tankMoving2.dispose();
		tankMoving3.dispose();
		destroy.dispose();
		hpsFont.dispose();
		aim.dispose();
	}
}