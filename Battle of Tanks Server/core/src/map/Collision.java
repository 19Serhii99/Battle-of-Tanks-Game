package map;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.server.CommandPlayersTable;
import com.mygdx.server.Player;
import com.mygdx.server.PlayersTable;

import answers.Hit;
import answers.RemoveShellsAnswer;
import map.Chunk;
import map.Map;
import map.MapObject;
import map.MapObjectBase;
import net.UDPOutputObject;
import technique.MovingStatus;
import technique.Shell;
import technique.TechniqueType;
import technique.Tower;
import util.Mathematics;

public class Collision {
	private LinkedList<Shell> shells;

	public Collision(LinkedList<Shell> shells) {
		this.shells = shells;
	}

	public void act(Map map, ArrayList<Player> players, PlayersTable table, DatagramSocket socket) {
		final ArrayList<Shell> deadShells = new ArrayList<Shell>(1);
		final Iterator<Shell> shellsIterator = shells.iterator();
		while (shellsIterator.hasNext()) {
			final Shell shell = shellsIterator.next();

			final boolean shellOutside1 = shell.getX() >= map.getChunkGroup().getMapWidth();
			final boolean shellOutside2 = shell.getX() + shell.getWidth() <= 0;
			final boolean shellOutside3 = shell.getY() >= map.getChunkGroup().getMapHeight();
			final boolean shellOutside4 = shell.getY() + shell.getHeight() <= 0;

			if (shellOutside1 || shellOutside2 || shellOutside3 || shellOutside4) {
				shell.die();
				deadShells.add(shell);
				shellsIterator.remove();
			} else {
				TechniqueType techniqueType = null;
				for (Player player : players) {
					if (player.getId() == shell.getIdSender()) {
						techniqueType = player.getTechnique().getCorps().getTechniqueType();
						break;
					}
				}

				if (techniqueType == TechniqueType.TANK) {
					checkShellAndMap(deadShells, shell, shellsIterator);

					if (shell.isLive()) { // Если снаряд всё еще не взорван
						final Iterator<Player> playerIterator = players.iterator();
						while (playerIterator.hasNext()) {
							final Player player = playerIterator.next();

							if (shell.getIdSender() == player.getId())
								continue;

							final Sprite corps = shell;
							final float mapObjectCircle = Mathematics.getDistance(
									player.getTechnique().getCorpsBackground().getX(),
									player.getTechnique().getCorpsBackground().getY(),
									player.getTechnique().getCorpsBackground().getX()
											+ player.getTechnique().getCorpsBackground().getWidth() / 2,
									player.getTechnique().getCorpsBackground().getY()
											+ player.getTechnique().getCorpsBackground().getHeight() / 2);

							final float[] cv = corps.getVertices();

							final float minX = Math.min(Math.min(cv[Batch.X1], cv[Batch.X2]),
									Math.min(cv[Batch.X3], cv[Batch.X4]));
							final float maxX = Math.max(Math.max(cv[Batch.X1], cv[Batch.X2]),
									Math.max(cv[Batch.X3], cv[Batch.X4]));

							final float minY = Math.min(Math.min(cv[Batch.Y1], cv[Batch.Y2]),
									Math.min(cv[Batch.Y3], cv[Batch.Y4]));
							final float maxY = Math.max(Math.max(cv[Batch.Y1], cv[Batch.Y2]),
									Math.max(cv[Batch.Y3], cv[Batch.Y4]));

							final float corpsMiddleX = minX + (maxX - minX) / 2;
							final float corpsMiddleY = minY + (maxY - minY) / 2;
							final float corpsCircle = Mathematics.getDistance(corpsMiddleX, corpsMiddleY,
									player.getCorpsCenter().x, player.getCorpsCenter().y);

							if (corpsCircle <= mapObjectCircle) {
								if (isCollision(player.getTechnique().getCorpsBackground().getVertices(), cv)) {
									shell.die();
									deadShells.add(shell);
									shellsIterator.remove();
									if (player.isLive()) {
										hitPlayer(players, shell, player, table, socket);
									}
								}
							}
						}
					}
				} else { // Для арты
					if (Mathematics.getDistance(shell.getX(), shell.getAverageY(), shell.getNextX(),
							shell.getNextY()) <= 250) {
						for (Chunk chunk : shell.getCurrentChunks()) {
							final Iterator<MapObject> objectBaseIterator = chunk.getTempMapObjects().iterator();
							while (objectBaseIterator.hasNext()) {
								final MapObjectBase objectBase = objectBaseIterator.next();
								if (!objectBase.isWrapper()) {
									final MapObject mapObject = (MapObject) objectBase;
									final Sprite corps = shell;

									final float v[] = mapObject.getBackground().getVertices();
									final float cv[] = corps.getVertices();

									final float minX = Math.min(Math.min(cv[Batch.X1], cv[Batch.X2]),
											Math.min(cv[Batch.X3], cv[Batch.X4]));
									final float maxX = Math.max(Math.max(cv[Batch.X1], cv[Batch.X2]),
											Math.max(cv[Batch.X3], cv[Batch.X4]));

									final float minY = Math.min(Math.min(cv[Batch.Y1], cv[Batch.Y2]),
											Math.min(cv[Batch.Y3], cv[Batch.Y4]));
									final float maxY = Math.max(Math.max(cv[Batch.Y1], cv[Batch.Y2]),
											Math.max(cv[Batch.Y3], cv[Batch.Y4]));

									final float corpsMiddleX = minX + (maxX - minX) / 2;
									final float corpsMiddleY = minY + (maxY - minY) / 2;

									final float mapObjectCircle = Mathematics.getDistance(mapObject.getXAverage(),
											mapObject.getYAverage(), mapObject.getXWidth(), mapObject.getYHeight());
									final float corpsCircle = Mathematics.getDistance(mapObject.getXAverage(),
											mapObject.getYAverage(), corpsMiddleX, corpsMiddleY);

									if (corpsCircle <= mapObjectCircle) {
										if (isCollision(v, cv)) {
											shell.die();
											deadShells.add(shell);
											shellsIterator.remove();
										}
									}
								}
							}
						}
						if (shell.isLive()) {
							final Iterator<Player> playerIterator = players.iterator();
							while (playerIterator.hasNext()) {
								final Player player = playerIterator.next();
								if (shell.getIdSender() != player.getId()) {
									final Sprite corps = shell;

									float mapObjectCircle = Mathematics.getDistance(
											player.getTechnique().getCorpsBackground().getX(),
											player.getTechnique().getCorpsBackground().getY(),
											player.getTechnique().getCorpsBackground().getX()
													+ player.getTechnique().getCorpsBackground().getWidth() / 2,
											player.getTechnique().getCorpsBackground().getY()
													+ player.getTechnique().getCorpsBackground().getHeight() / 2);
									float corpsMiddleX = corps.getX() + corps.getWidth() / 2;
									float corpsMiddleY = corps.getY() + corps.getHeight() / 2;
									float corpsCircle = Mathematics.getDistance(corpsMiddleX, corpsMiddleY,
											player.getCorpsCenter().x, player.getCorpsCenter().y);

									if (corpsCircle <= mapObjectCircle) {
										float v[] = player.getTechnique().getCorpsBackground().getVertices();
										float cv[] = corps.getVertices();
										Vector2 point1 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
												v[Batch.X2], v[Batch.Y2], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
												cv[Batch.Y3]);
										Vector2 point2 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
												v[Batch.X4], v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
												cv[Batch.Y3]);
										Vector2 point3 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2],
												v[Batch.X3], v[Batch.Y3], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
												cv[Batch.Y3]);
										Vector2 point4 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3],
												v[Batch.X4], v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
												cv[Batch.Y3]);

										boolean verification1 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
												v[Batch.X2], v[Batch.Y2], point1.x, point1.y)
												&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
														cv[Batch.Y3], point1.x, point1.y);
										boolean verification2 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
												v[Batch.X4], v[Batch.Y4], point2.x, point2.y)
												&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
														cv[Batch.Y3], point2.x, point2.y);
										boolean verification3 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2],
												v[Batch.X3], v[Batch.Y3], point3.x, point3.y)
												&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
														cv[Batch.Y3], point3.x, point3.y);
										boolean verification4 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3],
												v[Batch.X4], v[Batch.Y4], point4.x, point4.y)
												&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
														cv[Batch.Y3], point4.x, point4.y);

										float x = 0, y = 0;

										if (verification1) {
											x = point1.x - cv[Batch.X3];
											y = point1.y - cv[Batch.Y3];
										} else if (verification2) {
											x = point2.x - cv[Batch.X3];
											y = point2.y - cv[Batch.Y3];
										} else if (verification3) {
											x = point3.x - cv[Batch.X3];
											y = point3.y - cv[Batch.Y3];
										} else if (verification4) {
											x = point4.x - cv[Batch.X3];
											y = point4.y - cv[Batch.Y3];
										}

										shell.translate(x, y);

										Vector2 point5 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
												v[Batch.X2], v[Batch.Y2], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
												cv[Batch.Y4]);
										Vector2 point6 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
												v[Batch.X4], v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
												cv[Batch.Y4]);
										Vector2 point7 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2],
												v[Batch.X3], v[Batch.Y3], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
												cv[Batch.Y4]);
										Vector2 point8 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3],
												v[Batch.X4], v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
												cv[Batch.Y4]);

										boolean verification5 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
												v[Batch.X2], v[Batch.Y2], point5.x, point5.y)
												&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
														cv[Batch.Y4], point5.x, point5.y);
										boolean verification6 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
												v[Batch.X4], v[Batch.Y4], point6.x, point6.y)
												&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
														cv[Batch.Y4], point6.x, point6.y);
										boolean verification7 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2],
												v[Batch.X3], v[Batch.Y3], point7.x, point7.y)
												&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
														cv[Batch.Y4], point7.x, point7.y);
										boolean verification8 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3],
												v[Batch.X4], v[Batch.Y4], point8.x, point8.y)
												&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
														cv[Batch.Y4], point8.x, point8.y);

										if (verification5) {
											x = point5.x - cv[Batch.X4];
											y = point5.y - cv[Batch.Y4];
										} else if (verification6) {
											x = point6.x - cv[Batch.X4];
											y = point6.y - cv[Batch.Y4];
										} else if (verification7) {
											x = point7.x - cv[Batch.X4];
											y = point7.y - cv[Batch.Y4];
										} else if (verification8) {
											x = point8.x - cv[Batch.X4];
											y = point8.y - cv[Batch.Y4];
										}

										shell.translate(x, y);

										if (verification1 || verification2 || verification3 || verification4
												|| verification5 || verification6 || verification7 || verification8) {
											shell.die();
											deadShells.add(shell);
											shellsIterator.remove();
											if (player.isLive()) {
												for (int i = 0; i < players.size(); i++) {
													if (shell.getIdSender() == players.get(i).getId()) {
														if (!((player.isLeftSide() && players.get(i).isLeftSide())
																|| (!player.isLeftSide()
																		&& !players.get(i).isLeftSide()))) {
															int damage = MathUtils.random(
																	players.get(i).getTechnique().getTower()
																			.getMinDamage(),
																	players.get(i).getTechnique().getTower()
																			.getMaxDamage());
															damage = player.hit(damage);
															if (table.isCommand()) {
																CommandPlayersTable cpt = (CommandPlayersTable) table;
																cpt.addDamage(players.get(i),
																		players.get(i).isLeftSide(), damage,
																		!player.isLive());
																Hit hit = new Hit(shell.getIdSender(), player.getId(),
																		damage, players.get(i).isLeftSide());
																for (int j = 0; j < players.size(); j++) {
																	UDPOutputObject object = new UDPOutputObject(
																			players.get(j).getInetAddress(),
																			players.get(j).getPort(), hit);
																	object.sendObject(socket);
																}
															}
															break;
														}
													}
												}
											}
										}
									}
								}
							}
							if (shell.isLive() && Mathematics.getDistance(shell.getX(), shell.getAverageY(),
									shell.getNextX(), shell.getNextY()) <= 100) {
								shell.die();
								deadShells.add(shell);
								shellsIterator.remove();
							}
						}
					}
				}
			}
		}

		if (deadShells.size() > 0) {
			RemoveShellsAnswer answer = new RemoveShellsAnswer();
			for (int i = 0; i < deadShells.size(); i++) {
				Shell shell = deadShells.get(i);
				answer.getShells().add(shell.getId());
			}
			for (Player player : players) {
				UDPOutputObject out = new UDPOutputObject(player.getInetAddress(), player.getPort(), answer);
				out.sendObject(socket);
			}
		}

		Iterator<Player> playerIterator = players.iterator();
		while (playerIterator.hasNext()) {
			Player player = playerIterator.next();
			for (Chunk chunk : player.getTechnique().getCurrentChunks()) {
				Iterator<MapObject> objectBaseIterator = chunk.getTempMapObjects().iterator();
				while (objectBaseIterator.hasNext()) {
					MapObjectBase objectBase = objectBaseIterator.next();
					if (!objectBase.isWrapper()) {
						MapObject mapObject = (MapObject) objectBase;
						Sprite corps = player.getTechnique().getCorpsBackground();

						float mapObjectCircle = Mathematics.getDistance(mapObject.getX(), mapObject.getY(),
								mapObject.getXWidth(), mapObject.getYHeight());
						float corpsMiddleX = corps.getX() + corps.getWidth() / 2;
						float corpsMiddleY = corps.getY() + corps.getHeight() / 2;
						float corpsCircle = Mathematics.getDistance(corpsMiddleX, corpsMiddleY, mapObject.getXAverage(),
								mapObject.getYAverage());

						if (corpsCircle <= mapObjectCircle) {
							if (player.getMovingStatus() != MovingStatus.STOP) {
								float v[] = mapObject.getBackground().getVertices();
								float cv[] = corps.getVertices();

								if (player.getMovingStatus() == MovingStatus.FORWARD
										|| player.getMovingStatus() == MovingStatus.FORWARD_LEFT
										|| player.getMovingStatus() == MovingStatus.FORWARD_RIGHT) {
									// if (Mathematics.isCollision(mapObject.getX(), mapObject.getY(),
									// mapObject.getXWidth(), mapObject.getY(), mapObject.getX(),
									// mapObject.getYHeight(),
									// mapObject.getXWidth(), mapObject.getYHeight(), mapObject, cv[Batch.X3],
									// cv[Batch.Y3])
									// || Mathematics.isCollision(mapObject.getX(), mapObject.getY(),
									// mapObject.getXWidth(), mapObject.getY(), mapObject.getX(),
									// mapObject.getYHeight(),
									// mapObject.getXWidth(), mapObject.getYHeight(), mapObject, cv[Batch.X4],
									// cv[Batch.Y4])) {
									Vector2 point1 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);
									Vector2 point2 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);
									Vector2 point3 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);
									Vector2 point4 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);

									boolean verification1 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], point1.x, point1.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point1.x, point1.y);
									boolean verification2 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], point2.x, point2.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point2.x, point2.y);
									boolean verification3 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], point3.x, point3.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point3.x, point3.y);
									boolean verification4 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], point4.x, point4.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point4.x, point4.y);

									float x = 0, y = 0;

									if (verification1) {
										x = point1.x - cv[Batch.X3];
										y = point1.y - cv[Batch.Y3];
									} else if (verification2) {
										x = point2.x - cv[Batch.X3];
										y = point2.y - cv[Batch.Y3];
									} else if (verification3) {
										x = point3.x - cv[Batch.X3];
										y = point3.y - cv[Batch.Y3];
									} else if (verification4) {
										x = point4.x - cv[Batch.X3];
										y = point4.y - cv[Batch.Y3];
									}

									player.translate(x, y);

									Vector2 point5 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);
									Vector2 point6 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);
									Vector2 point7 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);
									Vector2 point8 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);

									boolean verification5 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], point5.x, point5.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point5.x, point5.y);
									boolean verification6 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], point6.x, point6.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point6.x, point6.y);
									boolean verification7 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], point7.x, point7.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point7.x, point7.y);
									boolean verification8 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], point8.x, point8.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point8.x, point8.y);

									if (verification5) {
										x = point5.x - cv[Batch.X4];
										y = point5.y - cv[Batch.Y4];
									} else if (verification6) {
										x = point6.x - cv[Batch.X4];
										y = point6.y - cv[Batch.Y4];
									} else if (verification7) {
										x = point7.x - cv[Batch.X4];
										y = point7.y - cv[Batch.Y4];
									} else if (verification8) {
										x = point8.x - cv[Batch.X4];
										y = point8.y - cv[Batch.Y4];
									}

									player.translate(x, y);
									// }
								} else {
									// if (Mathematics.isCollision(mapObject.getX(), mapObject.getY(),
									// mapObject.getXWidth(), mapObject.getY(), mapObject.getX(),
									// mapObject.getYHeight(),
									// mapObject.getXWidth(), mapObject.getYHeight(), mapObject, cv[Batch.X1],
									// cv[Batch.Y1])
									// || Mathematics.isCollision(mapObject.getX(), mapObject.getY(),
									// mapObject.getXWidth(), mapObject.getY(), mapObject.getX(),
									// mapObject.getYHeight(),
									// mapObject.getXWidth(), mapObject.getYHeight(), mapObject, cv[Batch.X2],
									// cv[Batch.Y2])) {
									Vector2 point1 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);
									Vector2 point2 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);
									Vector2 point3 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);
									Vector2 point4 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
											cv[Batch.Y3]);

									boolean verification1 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], point1.x, point1.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point1.x, point1.y);
									boolean verification2 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], point2.x, point2.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point2.x, point2.y);
									boolean verification3 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], point3.x, point3.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point3.x, point3.y);
									boolean verification4 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], point4.x, point4.y)
											&& Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
													cv[Batch.Y3], point4.x, point4.y);

									float x = 0, y = 0;

									if (verification1) {
										x = point1.x - cv[Batch.X2];
										y = point1.y - cv[Batch.Y2];
									} else if (verification2) {
										x = point2.x - cv[Batch.X2];
										y = point2.y - cv[Batch.Y2];
									} else if (verification3) {
										x = point3.x - cv[Batch.X2];
										y = point3.y - cv[Batch.Y2];
									} else if (verification4) {
										x = point4.x - cv[Batch.X2];
										y = point4.y - cv[Batch.Y2];
									}

									player.translate(x, y);

									Vector2 point5 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);
									Vector2 point6 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);
									Vector2 point7 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);
									Vector2 point8 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
											cv[Batch.Y4]);

									boolean verification5 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X2], v[Batch.Y2], point5.x, point5.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point5.x, point5.y);
									boolean verification6 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1],
											v[Batch.X4], v[Batch.Y4], point6.x, point6.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point6.x, point6.y);
									boolean verification7 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2],
											v[Batch.X3], v[Batch.Y3], point7.x, point7.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point7.x, point7.y);
									boolean verification8 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3],
											v[Batch.X4], v[Batch.Y4], point8.x, point8.y)
											&& Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
													cv[Batch.Y4], point8.x, point8.y);

									if (verification5) {
										x = point5.x - cv[Batch.X1];
										y = point5.y - cv[Batch.Y1];
									} else if (verification6) {
										x = point6.x - cv[Batch.X1];
										y = point6.y - cv[Batch.Y1];
									} else if (verification7) {
										x = point7.x - cv[Batch.X1];
										y = point7.y - cv[Batch.Y1];
									} else if (verification8) {
										x = point8.x - cv[Batch.X1];
										y = point8.y - cv[Batch.Y1];
									}

									player.translate(x, y);
									// }
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * Проверка снаряда на столкновение с объектами карты
	 * 
	 **/
	private void checkShellAndMap(ArrayList<Shell> deadShells, Shell shell, Iterator<Shell> shellsIterator) {
//		for (Chunk chunk : shell.getCurrentChunks()) {
//			final Iterator<MapObject> objectBaseIterator = chunk.getTempMapObjects().iterator();
//			while (objectBaseIterator.hasNext()) {
//				final MapObjectBase objectBase = objectBaseIterator.next();
//				if (!objectBase.isWrapper()) {
//					final MapObject mapObject = (MapObject) objectBase;
//					final Sprite corps = shell;
//
//					final float mapObjectCircle = Mathematics.getDistance(mapObject.getXAverage(),
//							mapObject.getYAverage(), mapObject.getXWidth(), mapObject.getYHeight());
//
//					final float[] cv = corps.getVertices();
//
//					final float minX = Math.min(Math.min(cv[Batch.X1], cv[Batch.X2]),
//							Math.min(cv[Batch.X3], cv[Batch.X4]));
//					final float maxX = Math.max(Math.max(cv[Batch.X1], cv[Batch.X2]),
//							Math.max(cv[Batch.X3], cv[Batch.X4]));
//
//					final float minY = Math.min(Math.min(cv[Batch.Y1], cv[Batch.Y2]),
//							Math.min(cv[Batch.Y3], cv[Batch.Y4]));
//					final float maxY = Math.max(Math.max(cv[Batch.Y1], cv[Batch.Y2]),
//							Math.max(cv[Batch.Y3], cv[Batch.Y4]));
//
//					final float corpsMiddleX = minX + (maxX - minX) / 2;
//					final float corpsMiddleY = minY + (maxY - minY) / 2;
//					final float corpsCircle = Mathematics.getDistance(mapObject.getXAverage(), mapObject.getYAverage(),
//							corpsMiddleX, corpsMiddleY);
//
//					if (corpsCircle <= mapObjectCircle) {
//						final float v[] = mapObject.getBackground().getVertices();
//						final Vector2 point1 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1], v[Batch.X2],
//								v[Batch.Y2], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3], cv[Batch.Y3]);
//						final Vector2 point2 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1], v[Batch.X4],
//								v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3], cv[Batch.Y3]);
//						final Vector2 point3 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2], v[Batch.X3],
//								v[Batch.Y3], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3], cv[Batch.Y3]);
//						final Vector2 point4 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3], v[Batch.X4],
//								v[Batch.Y4], cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3], cv[Batch.Y3]);
//
//						final boolean ver11 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1], v[Batch.X2],
//								v[Batch.Y2], point1.x, point1.y);
//						final boolean ver12 = Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
//								cv[Batch.Y3], point1.x, point1.y);
//
//						final boolean ver21 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1], v[Batch.X4],
//								v[Batch.Y4], point2.x, point2.y);
//						final boolean ver22 = Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
//								cv[Batch.Y3], point2.x, point2.y);
//
//						final boolean ver31 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2], v[Batch.X3],
//								v[Batch.Y3], point3.x, point3.y);
//						final boolean ver32 = Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
//								cv[Batch.Y3], point3.x, point3.y);
//
//						final boolean ver41 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3], v[Batch.X4],
//								v[Batch.Y4], point4.x, point4.y);
//						final boolean ver42 = Mathematics.pointInSegment(cv[Batch.X2], cv[Batch.Y2], cv[Batch.X3],
//								cv[Batch.Y3], point4.x, point4.y);
//
//						final boolean verification1 = ver11 && ver12;
//						final boolean verification2 = ver21 && ver22;
//						final boolean verification3 = ver31 && ver32;
//						final boolean verification4 = ver41 && ver42;
//
//						float x = 0, y = 0;
//
//						if (verification1) {
//							x = point1.x - cv[Batch.X3];
//							y = point1.y - cv[Batch.Y3];
//						} else if (verification2) {
//							x = point2.x - cv[Batch.X3];
//							y = point2.y - cv[Batch.Y3];
//						} else if (verification3) {
//							x = point3.x - cv[Batch.X3];
//							y = point3.y - cv[Batch.Y3];
//						} else if (verification4) {
//							x = point4.x - cv[Batch.X3];
//							y = point4.y - cv[Batch.Y3];
//						}
//
//						final Vector2 point5 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1], v[Batch.X2],
//								v[Batch.Y2], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4], cv[Batch.Y4]);
//						final Vector2 point6 = Mathematics.getIntersectionPoint(v[Batch.X1], v[Batch.Y1], v[Batch.X4],
//								v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4], cv[Batch.Y4]);
//						final Vector2 point7 = Mathematics.getIntersectionPoint(v[Batch.X2], v[Batch.Y2], v[Batch.X3],
//								v[Batch.Y3], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4], cv[Batch.Y4]);
//						final Vector2 point8 = Mathematics.getIntersectionPoint(v[Batch.X3], v[Batch.Y3], v[Batch.X4],
//								v[Batch.Y4], cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4], cv[Batch.Y4]);
//
//						final boolean ver51 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1], v[Batch.X2],
//								v[Batch.Y2], point5.x, point5.y);
//						final boolean ver52 = Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
//								cv[Batch.Y4], point5.x, point5.y);
//
//						final boolean ver61 = Mathematics.pointInSegment(v[Batch.X1], v[Batch.Y1], v[Batch.X4],
//								v[Batch.Y4], point6.x, point6.y);
//						final boolean ver62 = Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
//								cv[Batch.Y4], point6.x, point6.y);
//
//						final boolean ver71 = Mathematics.pointInSegment(v[Batch.X2], v[Batch.Y2], v[Batch.X3],
//								v[Batch.Y3], point7.x, point7.y);
//						final boolean ver72 = Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
//								cv[Batch.Y4], point7.x, point7.y);
//
//						final boolean ver81 = Mathematics.pointInSegment(v[Batch.X3], v[Batch.Y3], v[Batch.X4],
//								v[Batch.Y4], point8.x, point8.y);
//						final boolean ver82 = Mathematics.pointInSegment(cv[Batch.X1], cv[Batch.Y1], cv[Batch.X4],
//								cv[Batch.Y4], point8.x, point8.y);
//
//						final boolean verification5 = ver51 && ver52;
//						final boolean verification6 = ver61 && ver62;
//						final boolean verification7 = ver71 && ver72;
//						final boolean verification8 = ver81 && ver82;
//
//						x = y = 0;
//
//						if (verification5) {
//							x = point5.x - cv[Batch.X4];
//							y = point5.y - cv[Batch.Y4];
//						} else if (verification6) {
//							x = point6.x - cv[Batch.X4];
//							y = point6.y - cv[Batch.Y4];
//						} else if (verification7) {
//							x = point7.x - cv[Batch.X4];
//							y = point7.y - cv[Batch.Y4];
//						} else if (verification8) {
//							x = point8.x - cv[Batch.X4];
//							y = point8.y - cv[Batch.Y4];
//						}
//
//						if (verification1 || verification2 || verification3 || verification4 || verification5
//								|| verification6 || verification7 || verification8) {
//							shell.die();
//							deadShells.add(shell);
//							shellsIterator.remove();
//						}
//					}
//				}
//			}
//		}

		for (Chunk chunk : shell.getCurrentChunks()) {
			final Iterator<MapObject> objectBaseIterator = chunk.getTempMapObjects().iterator();
			while (objectBaseIterator.hasNext()) {
				final MapObjectBase objectBase = objectBaseIterator.next();
				if (!objectBase.isWrapper()) {
					final MapObject mapObject = (MapObject) objectBase;
					final Sprite corps = shell;

					final float mapObjectCircle = Mathematics.getDistance(mapObject.getXAverage(),
							mapObject.getYAverage(), mapObject.getXWidth(), mapObject.getYHeight());

					final float[] cv = corps.getVertices();

					final float minX = Math.min(Math.min(cv[Batch.X1], cv[Batch.X2]),
							Math.min(cv[Batch.X3], cv[Batch.X4]));
					final float maxX = Math.max(Math.max(cv[Batch.X1], cv[Batch.X2]),
							Math.max(cv[Batch.X3], cv[Batch.X4]));

					final float minY = Math.min(Math.min(cv[Batch.Y1], cv[Batch.Y2]),
							Math.min(cv[Batch.Y3], cv[Batch.Y4]));
					final float maxY = Math.max(Math.max(cv[Batch.Y1], cv[Batch.Y2]),
							Math.max(cv[Batch.Y3], cv[Batch.Y4]));

					final float corpsMiddleX = minX + (maxX - minX) / 2;
					final float corpsMiddleY = minY + (maxY - minY) / 2;
					final float corpsCircle = Mathematics.getDistance(mapObject.getXAverage(), mapObject.getYAverage(),
							corpsMiddleX, corpsMiddleY);

					if (corpsCircle <= mapObjectCircle) {
						if (isCollision(mapObject.getBackground().getVertices(), cv)) {
							shell.die();
							deadShells.add(shell);
							shellsIterator.remove();
						}
					}
				}
			}
		}
	}

	/**
	 * Функция проверяет пересечение объектов, где @v2 - объект, который
	 * сталкивается
	 **/
	private boolean isCollision(float[] v1, float[] v2) {
		final Vector2 point1 = Mathematics.getIntersectionPoint(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X2], v1[Batch.Y2],
				v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3]);
		final Vector2 point2 = Mathematics.getIntersectionPoint(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X4], v1[Batch.Y4],
				v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3]);
		final Vector2 point3 = Mathematics.getIntersectionPoint(v1[Batch.X2], v1[Batch.Y2], v1[Batch.X3], v1[Batch.Y3],
				v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3]);
		final Vector2 point4 = Mathematics.getIntersectionPoint(v1[Batch.X3], v1[Batch.Y3], v1[Batch.X4], v1[Batch.Y4],
				v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3]);

		final boolean ver11 = Mathematics.pointInSegment(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X2], v1[Batch.Y2],
				point1.x, point1.y);
		final boolean ver12 = Mathematics.pointInSegment(v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3],
				point1.x, point1.y);

		final boolean ver21 = Mathematics.pointInSegment(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X4], v1[Batch.Y4],
				point2.x, point2.y);
		final boolean ver22 = Mathematics.pointInSegment(v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3],
				point2.x, point2.y);

		final boolean ver31 = Mathematics.pointInSegment(v1[Batch.X2], v1[Batch.Y2], v1[Batch.X3], v1[Batch.Y3],
				point3.x, point3.y);
		final boolean ver32 = Mathematics.pointInSegment(v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3],
				point3.x, point3.y);

		final boolean ver41 = Mathematics.pointInSegment(v1[Batch.X3], v1[Batch.Y3], v1[Batch.X4], v1[Batch.Y4],
				point4.x, point4.y);
		final boolean ver42 = Mathematics.pointInSegment(v2[Batch.X2], v2[Batch.Y2], v2[Batch.X3], v2[Batch.Y3],
				point4.x, point4.y);

		final boolean verification1 = ver11 && ver12;
		final boolean verification2 = ver21 && ver22;
		final boolean verification3 = ver31 && ver32;
		final boolean verification4 = ver41 && ver42;

//		float x = 0, y = 0;
//
//		if (verification1) {
//			x = point1.x - v2[Batch.X3];
//			y = point1.y - v2[Batch.Y3];
//		} else if (verification2) {
//			x = point2.x - v2[Batch.X3];
//			y = point2.y - v2[Batch.Y3];
//		} else if (verification3) {
//			x = point3.x - v2[Batch.X3];
//			y = point3.y - v2[Batch.Y3];
//		} else if (verification4) {
//			x = point4.x - v2[Batch.X3];
//			y = point4.y - v2[Batch.Y3];
//		}

		final Vector2 point5 = Mathematics.getIntersectionPoint(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X2], v1[Batch.Y2],
				v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4]);
		final Vector2 point6 = Mathematics.getIntersectionPoint(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X4], v1[Batch.Y4],
				v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4]);
		final Vector2 point7 = Mathematics.getIntersectionPoint(v1[Batch.X2], v1[Batch.Y2], v1[Batch.X3], v1[Batch.Y3],
				v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4]);
		final Vector2 point8 = Mathematics.getIntersectionPoint(v1[Batch.X3], v1[Batch.Y3], v1[Batch.X4], v1[Batch.Y4],
				v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4]);

		final boolean ver51 = Mathematics.pointInSegment(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X2], v1[Batch.Y2],
				point5.x, point5.y);
		final boolean ver52 = Mathematics.pointInSegment(v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4],
				point5.x, point5.y);

		final boolean ver61 = Mathematics.pointInSegment(v1[Batch.X1], v1[Batch.Y1], v1[Batch.X4], v1[Batch.Y4],
				point6.x, point6.y);
		final boolean ver62 = Mathematics.pointInSegment(v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4],
				point6.x, point6.y);

		final boolean ver71 = Mathematics.pointInSegment(v1[Batch.X2], v1[Batch.Y2], v1[Batch.X3], v1[Batch.Y3],
				point7.x, point7.y);
		final boolean ver72 = Mathematics.pointInSegment(v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4],
				point7.x, point7.y);

		final boolean ver81 = Mathematics.pointInSegment(v1[Batch.X3], v1[Batch.Y3], v1[Batch.X4], v1[Batch.Y4],
				point8.x, point8.y);
		final boolean ver82 = Mathematics.pointInSegment(v2[Batch.X1], v2[Batch.Y1], v2[Batch.X4], v2[Batch.Y4],
				point8.x, point8.y);

		final boolean verification5 = ver51 && ver52;
		final boolean verification6 = ver61 && ver62;
		final boolean verification7 = ver71 && ver72;
		final boolean verification8 = ver81 && ver82;

//		x = y = 0;
//
//		if (verification5) {
//			x = point5.x - v2[Batch.X4];
//			y = point5.y - v2[Batch.Y4];
//		} else if (verification6) {
//			x = point6.x - v2[Batch.X4];
//			y = point6.y - v2[Batch.Y4];
//		} else if (verification7) {
//			x = point7.x - v2[Batch.X4];
//			y = point7.y - v2[Batch.Y4];
//		} else if (verification8) {
//			x = point8.x - v2[Batch.X4];
//			y = point8.y - v2[Batch.Y4];
//		}

		return verification1 || verification2 || verification3 || verification4 || verification5 || verification6
				|| verification7 || verification8;
	}

	private void hitPlayer(ArrayList<Player> players, Shell shell, Player player, PlayersTable table,
			DatagramSocket socket) {
		for (int i = 0; i < players.size(); i++) {
			if (shell.getIdSender() != players.get(i).getId())
				continue;

			if ((player.isLeftSide() && players.get(i).isLeftSide())
					|| (!player.isLeftSide() && !players.get(i).isLeftSide()))
				continue;

			final int damage = player.hit(getRandomDamage(player.getTechnique().getTower()));

			if (table.isCommand()) {
				final CommandPlayersTable cpt = (CommandPlayersTable) table;
				cpt.addDamage(players.get(i), players.get(i).isLeftSide(), damage, !player.isLive());

				final Hit hit = new Hit(shell.getIdSender(), player.getId(), damage, players.get(i).isLeftSide());
				sendHitEveryPlayer(players, hit, socket);
			}
			break;
		}
	}

	private int getRandomDamage(Tower tower) {
		return MathUtils.random(tower.getMinDamage(), tower.getMaxDamage());
	}

	private void sendHitEveryPlayer(ArrayList<Player> players, Hit hit, DatagramSocket socket) {
		for (int i = 0; i < players.size(); i++) {
			final UDPOutputObject object = new UDPOutputObject(players.get(i).getInetAddress(),
					players.get(i).getPort(), hit);
			object.sendObject(socket);
		}
	}

	public LinkedList<Shell> getShells() {
		return shells;
	}
}