package net;

import java.util.ArrayDeque;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.DeathsBlock;

import answers.BattleEndedAnswer;
import answers.CreateShellsAnswer;
import answers.Hit;
import answers.MyLocalAnswer;
import answers.OtherPlayerLocalAnswer;
import answers.OtherPlayersLocalAnswer;
import answers.RemoveShellsAnswer;
import answers.ShellAnswer;
import map.Map;
import technique.Gun;
import technique.Shell;
import technique.Technique;
import technique.TechniqueType;
import util.CameraController;

public class PlayerListener extends Thread {
	private ArrayDeque<UDPInputObject> UDPObjects;
	
	private Boolean isStartPosition;

	public PlayerListener() {

	}

	public void addObject(UDPInputObject object) {
		synchronized (UDPObjects) {
			UDPObjects.add(object);
		}
	}

	@Override
	public void run() {
		while (true) {
			synchronized (UDPObjects) {
				if (!UDPObjects.isEmpty()) {
					final Object object = UDPObjects.poll().getObject();
					if (object != null) {
						if (object.getClass() == MyLocalAnswer.class) {
							MyLocalAnswer answer = (MyLocalAnswer) object;
							//myLocalController(answer, null, null, null, daemon);
						} 
//							else if (object.getClass() == CreateShellsAnswer.class) {
//							CreateShellsAnswer answer = (CreateShellsAnswer) object;
//							for (ShellAnswer shellAnswer : answer.getShells()) {
//								for (Player player : players) {
//									if (shellAnswer.getIdSender() == player.getId()) {
//										Gun gun = player.getTechnique().getTower().getGun();
//										Shell shell;
//										if (technique.getCorps().getTechniqueType() == TechniqueType.REACTIVE_SYSTEM
//												|| technique.getCorps()
//														.getTechniqueType() == TechniqueType.FLAMETHROWER_SYSTEM) {
//											shell = new Shell(gun.getShellTexture(), destroyShellTexture,
//													shellAnswer.getId(), shellAnswer.getIdSender(), flameTexture);
//										} else {
//											shell = new Shell(gun.getShellTexture(), destroyShellTexture,
//													shellAnswer.getId(), shellAnswer.getIdSender());
//										}
//										if (shell.getIdSender() == id)
//											aim.reset();
//										shell.setSpeed(gun.getShellSpeed());
//										shell.setSize(gun.getShellWidth(), gun.getShellHeight());
//										shell.setOriginCenter();
//										shell.nextPoint(shellAnswer.getX(), shellAnswer.getY(), shellAnswer.getNextX(),
//												shellAnswer.getNextY());
//										shells.add(shell);
//										sounds.add(gun.getShotSound().play());
//										if (sounds.size == 4) {
//											gun.getShotSound().stop(sounds.get(0));
//											sounds.removeIndex(0);
//										}
//										break;
//									}
//								}
//							}
//						} else if (object.getClass() == OtherPlayersLocalAnswer.class) {
//							OtherPlayersLocalAnswer answer = (OtherPlayersLocalAnswer) object;
//							for (OtherPlayerLocalAnswer other : answer.getPlayers()) {
//								for (Player player : players) {
//									if (player.getId() == other.getId()) {
//										player.getTechnique().getCorpsBackground().setPosition(other.getX(),
//												other.getY());
//										player.getTechnique().getCorpsBackground().setRotation(other.getRotation());
//										player.getTechnique().getTowerBackground()
//												.setRotation(other.getRotationTower());
//										player.getTechnique().getTowerBackground().setPosition(
//												other.getXTower() - player.getTechnique().getTower().getOriginX(),
//												other.getYTower() - player.getTechnique().getTower().getOriginY());
//										break;
//									}
//								}
//							}
//						} else if (object.getClass() == RemoveShellsAnswer.class) {
//							RemoveShellsAnswer answer = (RemoveShellsAnswer) object;
//							for (Integer shell : answer.getShells()) {
//								for (int i = 0; i < shells.size(); i++) {
//									if (shell == shells.get(i).getId()) {
//										shells.get(i).die();
//									}
//								}
//							}
//						} else if (object.getClass() == Hit.class) {
//							Hit answer = (Hit) object;
//							for (Player player : players) {
//								if (player.getId() == answer.getIdRecipient()) {
//									int damage = player.hit(answer.getDamage());
//									if (table.getClass() == CommandPlayersTable.class) {
//										for (int i = 0; i < players.size; i++) {
//											if (players.get(i).getId() == answer.getIdSender()) {
//												((CommandPlayersTable) table).addDamage(players.get(i),
//														answer.isLeftSide(), damage, !player.isLive());
//												if (!player.isLive()) {
//													deathsBlock.addKilling(players.get(i).getNameLabel().getText(),
//															player.getNameLabel().getText());
//												}
//												break;
//											}
//										}
//									}
//								}
//							}
//						} else if (object.getClass() == BattleEndedAnswer.class) {
//							BattleEndedAnswer answer = (BattleEndedAnswer) object;
//							gotExperience = answer.getExp();
//							gotMoney = answer.getMoney();
//							isWon = answer.isWon();
//							isBattleFinished = true;
//						}
					}
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void myLocalController(MyLocalAnswer answer, Technique technique, Map map, DeathsBlock deathsBlock, Boolean isStartPosition) {
		final Sprite tSprite = technique.getCorpsBackground();
		tSprite.setPosition(answer.getX(), answer.getY());
		tSprite.setRotation(answer.getRotation());
		tSprite.setRotation(answer.getRotationTower());
		tSprite.setPosition(answer.getXTower() - technique.getTower().getOriginX(),
				answer.getYTower() - technique.getTower().getOriginY());
		final CameraController camera = CameraController.getInstance();
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
			final float tempY = camera.getY();
			camera.setPosition(cameraX, cameraY);
			deathsBlock.translate(cameraY - tempY);
		}
		if (!isStartPosition) {
			isStartPosition = true;
			map.getChunkGroup().lookingForActiveChunks(camera.getX() - camera.getWidth() / 2,
					camera.getY() + camera.getHeight() / 2, camera.getWidth(), camera.getHeight());
		}
	}
}