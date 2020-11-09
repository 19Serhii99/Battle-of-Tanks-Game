package com.mygdx.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import map.Chunk;
import map.ChunkGroup;
import map.ChunkNeighbor;
import map.ChunkSide;
import map.MapObject;
import map.MapObjectBase;
import map.MapObjectWrapper;
import map.MapTexture;
import map.Tile;
import map.Vertex;
import map.WallTexture;

public class MapIO {
	public static void saveMap (Workspace workspace, String path) {
		for (Chunk chunk : workspace.getChunkGroup().getChunks()) {
			for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
				if (neighbor.getChunkSide() == ChunkSide.LEFT) {
					neighbor.getChunk().changePosition(chunk.getX() - workspace.getChunkGroup().getChunkWidth() - ChunkGroup.shift, chunk.getY());
					if (!neighbor.getChunk().isActivated()) workspace.getChunkGroup().getActiveChunks().add(neighbor.getChunk());
					neighbor.getChunk().setActivated(true);
				} else if (neighbor.getChunkSide() == ChunkSide.RIGHT) {
					neighbor.getChunk().changePosition(chunk.getX() + workspace.getChunkGroup().getChunkWidth() + ChunkGroup.shift, chunk.getY());
					if (!neighbor.getChunk().isActivated()) workspace.getChunkGroup().getActiveChunks().add(neighbor.getChunk());
					neighbor.getChunk().setActivated(true);
				} else if (neighbor.getChunkSide() == ChunkSide.UP) {
					neighbor.getChunk().changePosition(chunk.getX(), chunk.getY() + workspace.getChunkGroup().getChunkHeight() + ChunkGroup.shift);
					if (!neighbor.getChunk().isActivated()) workspace.getChunkGroup().getActiveChunks().add(neighbor.getChunk());
					neighbor.getChunk().setActivated(true);
				} else if (neighbor.getChunkSide() == ChunkSide.DOWN) {
					neighbor.getChunk().changePosition(chunk.getX(), chunk.getY() - workspace.getChunkGroup().getChunkHeight() - ChunkGroup.shift);
					if (!neighbor.getChunk().isActivated()) workspace.getChunkGroup().getActiveChunks().add(neighbor.getChunk());
					neighbor.getChunk().setActivated(true);
				}
			}
		}
		
		float minX = 0;
		float minY = 0;
		
		for (int i = 0; i < workspace.getChunkGroup().getChunks().size; i++) {
			Chunk chunk = workspace.getChunkGroup().getChunks().get(i);
			chunk.changePosition(chunk.getX() + 5000000, chunk.getY() + 5000000);
			
			if (i == 0) {
				minX = chunk.getX();
				minY = chunk.getY();
			}
			
			if (chunk.getX() < minX) minX = chunk.getX();
			if (chunk.getY() < minY) minY = chunk.getY();
		}
		
		float width = 0 - minX;
		float height = 0 - minY;
		
		for (Chunk chunk : workspace.getChunkGroup().getChunks()) {
			chunk.changePosition(chunk.getX() + width, chunk.getY() + height);
		}
		
		StringBuilder cmd = new StringBuilder();
		FileHandle file = Gdx.files.absolute(path + "/map.txt");
		
		cmd.append(workspace.getChunkGroup().getTileAmountX() + " ");
		cmd.append(workspace.getChunkGroup().getTileAmountY() + " ");
		cmd.append(workspace.getChunkGroup().getTileWidth() + " ");
		cmd.append(workspace.getChunkGroup().getTileHeight() + " ");
		cmd.append(workspace.getChunkGroup().getChunkAmountX() + " ");
		cmd.append(workspace.getChunkGroup().getChunkAmountY() + " ");
		cmd.append(workspace.getChunkGroup().getChunkWidth() + " ");
		cmd.append(workspace.getChunkGroup().getChunkHeight() + " ");
		
		cmd.append("mapTexturesBegin ");
		for (MapTexture mapTexture : workspace.getMapTextures()) {
			cmd.append(mapTexture.getId() + " ");
			String absolutePath = path + "/images/" + mapTexture.getId() + ".png";
			if (!mapTexture.getTexture().getTextureData().isPrepared()) mapTexture.getTexture().getTextureData().prepare();
			PixmapIO.writePNG(Gdx.files.absolute(absolutePath), mapTexture.getTexture().getTextureData().consumePixmap());
		}
		cmd.append("mapTexturesEnd ");
		
		cmd.append("wallTexturesBegin ");
		for (WallTexture wallTexture : workspace.getWallTextures()) {
			cmd.append(wallTexture.getId() + " ");
			String absolutePath = path + "/images/other/" + wallTexture.getId() + ".png";
			if (!wallTexture.getTexture().getTextureData().isPrepared()) wallTexture.getTexture().getTextureData().prepare();
			PixmapIO.writePNG(Gdx.files.absolute(absolutePath), wallTexture.getTexture().getTextureData().consumePixmap());
		}
		cmd.append("wallTexturesEnd ");
		
		cmd.append("chunksBegin ");
		for (Chunk chunk : workspace.getChunkGroup().getChunks()) {
			cmd.append("chunkBegin ");
			cmd.append(chunk.getX() + " ");
			cmd.append(chunk.getY() + " ");
			cmd.append("tileBegin ");
			for (Tile tile : chunk.getTiles()) {
				cmd.append(tile.getX() + " ");
				cmd.append(tile.getY() + " ");
				cmd.append(tile.getMapTexture().getId() + " ");
			}
			cmd.append("tileEnd ");
			
			cmd.append("wallBegin ");
			for (MapObjectBase base : chunk.getMapObjects()) {
				if (!base.isWrapper()) {
					MapObject object = (MapObject) base;
					cmd.append("mapObjectBegin ");
					cmd.append(object.getX() + " ");
					cmd.append(object.getY() + " ");
					cmd.append(object.getWidth() + " ");
					cmd.append(object.getHeight() + " ");
					cmd.append(object.getRotation() + " ");
					cmd.append(object.isWall() + " ");
					cmd.append(object.getWholeTexture().getId() + " ");
					cmd.append(object.isDestroyedWall() + " ");
					cmd.append(object.isDestroyedWall() ? (object.getDestroyedTexture().getId() + " ") : "null ");
					cmd.append("mapObjectEnd ");
				} else {
					cmd.append("mapObjectWrapperBegin ");
					cmd.append(((MapObjectWrapper) base).getX() + " ");
					cmd.append(((MapObjectWrapper) base).getY() + " ");
					cmd.append(((MapObjectWrapper) base).getWidth() + " ");
					cmd.append(((MapObjectWrapper) base).getHeight() + " ");
					for (MapObjectBase objectBase : ((MapObjectWrapper) base).getMapObjects()) {
						MapObject object = (MapObject) objectBase;
						cmd.append("mapObjectBegin ");
						cmd.append(object.getX() + " ");
						cmd.append(object.getY() + " ");
						cmd.append(object.getWidth() + " ");
						cmd.append(object.getHeight() + " ");
						cmd.append(object.getRotation() + " ");
						cmd.append(object.isWall() + " ");
						cmd.append(object.getWholeTexture().getId() + " ");
						cmd.append(object.isDestroyedWall() + " ");
						cmd.append(object.isDestroyedWall() ? (object.getDestroyedTexture().getId() + " ") : "null " );
						cmd.append("mapObjectEnd ");
					}
					cmd.append("mapObjectWrapperEnd ");
				}
			}
			cmd.append("wallEnd ");
			
			cmd.append("graphBegin ");
			for (Vertex vertex : chunk.getVertices()) {
				cmd.append(vertex.getX() + " " + vertex.getY() + " ");
				cmd.append(vertex.isSpawnOne() + " " + vertex.isSpawnTwo() + " ");
				cmd.append("neighborBegin ");
				for (Vertex neighbor : vertex.getNeighbors()) {
					cmd.append(neighbor.getX() + " " + neighbor.getY() + " ");
				}
				cmd.append("neighborEnd ");
			}
			cmd.append("graphEnd ");
			cmd.append("chunkEnd ");
		}
		cmd.append("chunksEnd ");
		
		cmd.append("baseOne " + workspace.getChunkGroup().getBaseOne().getBase().getX() + " " + workspace.getChunkGroup().getBaseOne().getBase().getY() + " ");
		cmd.append("baseTwo " + workspace.getChunkGroup().getBaseTwo().getBase().getX() + " " + workspace.getChunkGroup().getBaseTwo().getBase().getY() + " ");
		
		file.writeString(cmd.toString(), false);
	}
	
	public static void loadMap (String path, ChunkGroup group, Array <MapTexture> mapTextures, Array <WallTexture> wallTextures) {
		FileHandle file = Gdx.files.absolute(path + "/map.txt");
		if (file.exists()) {
			String cmd [] = file.readString().split(" ");
			group.setTileAmount(Integer.parseInt(cmd[0]), Integer.parseInt(cmd[1]));
			group.setTileSize(Float.parseFloat(cmd[2]), Float.parseFloat(cmd[3]));
			group.setChunkAmount(Integer.parseInt(cmd[4]), Integer.parseInt(cmd[5]));
			group.setChunkSize(Float.parseFloat(cmd[6]), Float.parseFloat(cmd[7]));
			
			int index;
			
			for (index = 8; index < cmd.length; index++) {
				if (cmd[index].equals("mapTexturesBegin")) {
					String next = cmd[++index];
					while (!next.equals("mapTexturesEnd")) {
						MapTexture mapTexture = new MapTexture(Integer.parseInt(next), new Texture(Gdx.files.absolute(path + "/images/" + Integer.parseInt(next) + ".png")));
						mapTextures.add(mapTexture);
						next = cmd[++index];
					}
				} else if (cmd[index].equals("wallTexturesBegin")) {
					String next = cmd[++index];
					while (!next.equals("wallTexturesEnd")) {
						String tempPath = path + "/images/other/" + Integer.parseInt(next) + ".png";
						WallTexture wallTexture = new WallTexture(Integer.parseInt(next), new Texture(Gdx.files.absolute(tempPath)), tempPath);
						wallTextures.add(wallTexture);
						next = cmd[++index];
					}
				} else if (cmd[index].equals("chunksBegin")) {
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
												for (WallTexture wallTexture : wallTextures) {
													if (id == wallTexture.getId()) {
														mapObject.setWholeTexture(wallTexture);
														break;
													}
												}
												mapObject.setDestroyedWall(Boolean.parseBoolean(cmd[++index]));
												if (mapObject.isDestroyedWall()) {
													id = Integer.parseInt(cmd[++index]);
													for (WallTexture wallTexture : wallTextures) {
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
														for (WallTexture wallTexture : wallTextures) {
															if (id == wallTexture.getId()) {
																mapObject.setWholeTexture(wallTexture);
																break;
															}
														}
														mapObject.setDestroyedWall(Boolean.parseBoolean(cmd[++index]));
														if (mapObject.isDestroyedWall()) {
															id = Integer.parseInt(cmd[++index]);
															for (WallTexture wallTexture : wallTextures) {
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
					group.getBaseOne().getBase().setPosition(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
				} else if (cmd[index].equals("baseTwo")) {
					group.getBaseTwo().getBase().setPosition(Float.parseFloat(cmd[++index]), Float.parseFloat(cmd[++index]));
				}
			}
		}
		group.lookingForNeighbors();
		
		for (int i = 0; i < group.getChunks().size; i++) {
			for (int j = 0; j < group.getChunks().get(i).getVertices().size; j++) {
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