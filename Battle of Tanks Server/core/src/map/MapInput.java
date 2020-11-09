package map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import map.Chunk;
import map.ChunkGroup;
import map.MapObject;
import map.MapObjectWrapper;
import map.MapTexture;
import map.Tile;
import map.Vertex;

public class MapInput {
	public static void loadMap (String path, ChunkGroup group, Array <MapTexture> mapTextures, Array <MapTexture> wallTextures, Array <Spawn> spawnCommandOne, Array <Spawn> spawnCommandTwo) {
		FileHandle file = Gdx.files.internal("maps/" + path + "/map.txt");
		if (file.exists()) {
			String cmd [] = file.readString().split(" ");
			group.setTileAmount(Integer.parseInt(cmd[0]), Integer.parseInt(cmd[1]));
			group.setTileSize(Float.parseFloat(cmd[2]), Float.parseFloat(cmd[3]));
			group.setChunkAmount(Integer.parseInt(cmd[4]), Integer.parseInt(cmd[5]));
			group.setChunkSize(Float.parseFloat(cmd[6]), Float.parseFloat(cmd[7]));
			
			int index;
			
			for (index = 8; index < cmd.length; index++) {
				if (cmd[index].equals("chunksBegin")) {
					String next = cmd[++index];
					while (!next.equals("chunksEnd")) {
						if (next.equals("chunkBegin")) {
							Chunk chunk = new Chunk(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
							next = cmd[++index];
							while (!next.equals("chunkEnd")) {
								if (next.equals("tileBegin")) {
									next = cmd[++index];
									while (!next.equals("tileEnd")) {
										float x = Float.parseFloat(next);
										float y = Float.parseFloat(cmd[++index]);
										int id = Integer.parseInt(cmd[++index]);
										MapTexture mapTexture = null;
										for (int j = 0; j < mapTextures.size; j++) {
											if (id == mapTextures.get(j).getId()) {
												mapTexture = mapTextures.get(j);
												break;
											}
										}
										Tile tile = new Tile(mapTexture);
										tile.setPosition(x, y);
										next = cmd[++index];
										chunk.getTiles().add(tile);
									}
								} else if (next.equals("wallBegin")) {
									next = cmd[++index];
									while (!next.equals("wallEnd")) {
										if (next.equals("mapObjectBegin")) {
											next = cmd[++index];
											while (!next.equals("mapObjectEnd")) {
												MapObject mapObject = new MapObject();
												mapObject.setPosition(Float.parseFloat(next), Float.parseFloat(cmd[++index]));
												mapObject.setSize(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
												mapObject.setRotation(Float.parseFloat(cmd[++index]));
												mapObject.setWall(Boolean.parseBoolean(cmd[++index]));
												int id = Integer.parseInt(cmd[++index]);
												for (MapTexture wallTexture : wallTextures) {
													if (id == wallTexture.getId()) {
														mapObject.setWholeTexture(wallTexture);
														break;
													}
												}
												mapObject.setDestroyedWall(Boolean.parseBoolean(cmd[++index]));
												if (mapObject.isDestroyedWall()) {
													id = Integer.parseInt(cmd[++index]);
													for (MapTexture wallTexture : wallTextures) {
														if (id == wallTexture.getId()) {
															mapObject.setDestroyedTexture(wallTexture);
															break;
														}
													}
												} else {
													index++;
												}
												next = cmd[++index];
												chunk.getMapObjects().add(mapObject);
												mapObject.buildData();
											}
										} else if (next.equals("mapObjectWrapperBegin")) {
											next = cmd[++index];
											MapObjectWrapper wrapper = new MapObjectWrapper();
											wrapper.setPosition(Float.parseFloat(next), Float.parseFloat(cmd[++index]));
											wrapper.setSize(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
											next = cmd[++index];
											while (!next.equals("mapObjectWrapperEnd")) {
												if (next.equals("mapObjectBegin")) {
													next = cmd[++index];
													while (!next.equals("mapObjectEnd")) {
														MapObject mapObject = new MapObject();
														mapObject.setPosition(Float.parseFloat(next), Float.parseFloat(cmd[++index]));
														mapObject.setSize(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
														mapObject.setRotation(Float.parseFloat(cmd[++index]));
														mapObject.setWall(Boolean.parseBoolean(cmd[++index]));
														int id = Integer.parseInt(cmd[++index]);
														for (MapTexture wallTexture : wallTextures) {
															if (id == wallTexture.getId()) {
																mapObject.setWholeTexture(wallTexture);
																break;
															}
														}
														mapObject.setDestroyedWall(Boolean.parseBoolean(cmd[++index]));
														if (mapObject.isDestroyedWall()) {
															id = Integer.parseInt(cmd[++index]);
															for (MapTexture wallTexture : wallTextures) {
																if (id == wallTexture.getId()) {
																	mapObject.setDestroyedTexture(wallTexture);
																	break;
																}
															}
														} else {
															index++;
														}
														next = cmd[++index];
														wrapper.getMapObjects().add(mapObject);
													}
												}
											}
											chunk.getMapObjects().add(wrapper);
										}
										next = cmd[++index];
									}
								} else if (next.equals("graphBegin")) {
									next = cmd[++index];
									while (!next.equals("graphEnd")) {
										Vertex vertex = new Vertex(Float.parseFloat(next), Float.parseFloat(cmd[++index]));
										vertex.setSpawnOne(Boolean.parseBoolean(cmd[++index]));
										vertex.setSpawnTwo(Boolean.parseBoolean(cmd[++index]));
										if (cmd[++index].equals("neighborBegin")) {
											next = cmd[++index];
											while (!next.equals("neighborEnd")) {
												vertex.getTempNeighbors().add(new Vertex(Float.parseFloat(next), Float.parseFloat(cmd[++index])));
												next = cmd[++index];
											}
										}
										chunk.getVertices().add(vertex);
										next = cmd[++index];
									}
								}
								next = cmd[++index];
							}
							group.getChunks().add(chunk);
						}
						next = cmd[++index];
					}
				} else if (cmd[index].equals("baseOne")) {
					//group.getBaseOne().getBase().setPosition(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
				} else if (cmd[index].equals("baseTwo")) {
					//group.getBaseTwo().getBase().setPosition(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
				}
			}
		}
		group.lookingForNeighbors();
		
		for (Chunk chunk : group.getChunks()) {
			for (MapObjectBase base : chunk.getMapObjects()) {
				if (!base.isWrapper()) {
					MapObject mapObject = (MapObject) base;
					chunk.getTempMapObjects().add(mapObject);
					for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
						neighbor.getChunk().getTempMapObjects().add(mapObject);
					}
				}
			}
		}
		
		for (int i = 0; i < group.getChunks().size; i++) {
			for (int j = 0; j < group.getChunks().get(i).getVertices().size; j++) {
				if (group.getChunks().get(i).getVertices().get(j).isSpawnOne()) {
					spawnCommandOne.add(new Spawn(group.getChunks().get(i).getVertices().get(j)));
				} else if (group.getChunks().get(i).getVertices().get(j).isSpawnTwo()) {
					spawnCommandTwo.add(new Spawn(group.getChunks().get(i).getVertices().get(j)));
				}
				for (int k = 0; k < group.getChunks().get(i).getVertices().get(j).getTempNeighbors().size; k++) {
					for (int l = 0; l < group.getChunks().size; l++) {
						for (int u = 0; u < group.getChunks().get(l).getVertices().size; u++) {
							if (group.getChunks().get(i).getVertices().get(j).getTempNeighbors().get(k).getX() == group.getChunks().get(l).getVertices().get(u).getX()
									&& group.getChunks().get(i).getVertices().get(j).getTempNeighbors().get(k).getY() == group.getChunks().get(l).getVertices().get(u).getY()) {
								group.getChunks().get(i).getVertices().get(j).getNeighbors().add(group.getChunks().get(l).getVertices().get(u));
							}
						}
					}
				}
				group.getChunks().get(i).getVertices().get(j).getTempNeighbors().clear();
			}
		}
	}
}