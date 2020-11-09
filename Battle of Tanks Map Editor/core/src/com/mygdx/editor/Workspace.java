package com.mygdx.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import forms.FileChooserForm;
import map.Chunk;
import map.ChunkGroup;
import map.ChunkNeighbor;
import map.ChunkSide;
import map.GraphManager;
import map.MapObject;
import map.MapObjectManager;
import map.MapTexture;
import map.Tile;
import map.WallTexture;
import util.CameraController;
import util.TextureCreator;

public class Workspace implements Disposable {
	private Sprite workspace;
	private Sprite border;
	private Sprite chunkBorder;
	private Sprite tileSprite;
	private ChunkGroup chunkGroup;
	private Array <MapTexture> mapTextures;
	private Array <WallTexture> wallTextures;
	private Rectangle scissors;
	private Properties properties;
	private LowerPane lowerPane;
	private GraphManager graphManager;
	private FileChooserForm fileChooser;
	private MapObjectManager mapObjectManager;
	
	private boolean isMouseRightPressed = false;
	private boolean isBorder = true;
	private boolean isGraph = false;
	
	private Vector3 cursor = new Vector3(0, 0, 0);
	
	private float moveX = 0;
	private float moveY = 0;
	private float xWorkspace;
	private float yWorkspace;
	private float xWidthWorkspace;
	private float yHeightWorkspace;
	
	public Workspace () {
		workspace = new Sprite(TextureCreator.createTexture(Color.GRAY));
		workspace.setSize(1000, 640);
		workspace.setPosition(10, 100);
		
		border = new Sprite(TextureCreator.createTexture(Color.BROWN));
		chunkBorder = new Sprite(TextureCreator.createTexture(Color.RED));
		tileSprite = new Sprite();
		mapTextures = new Array <MapTexture>(1);
		wallTextures = new Array <WallTexture>(1);
		scissors = new Rectangle();
		properties = new Properties(this);
		lowerPane = new LowerPane();
		graphManager = new GraphManager();
		mapObjectManager = new MapObjectManager();
	}
	
	public void createMap (float chunkWidth, float chunkHeight, float tileWidth, float tileHeight, int tileAmountX, int tileAmountY) {
		MapTexture mapTexture = new MapTexture(0, TextureCreator.createTexture(Color.GRAY));
		mapTextures.add(mapTexture);
		
		wallTextures.add(new WallTexture(0, TextureCreator.createTexture(Color.GRAY), ""));
		wallTextures.get(0).using++;
		
		Array <Tile> tiles = new Array <Tile>(tileAmountX * tileAmountY);
		
		float tempX = workspace.getX() - tileWidth;
		float tempY = workspace.getY() + workspace.getHeight();
		
		for (int i = 0; i < tileAmountX; i++) {
			tempX += i == 0 ? tileWidth : tileWidth + ChunkGroup.shift;
			for (int j = 0; j < tileAmountY; j++) {
				tempY -= j == 0 ? tileHeight : tileHeight + ChunkGroup.shift;
				Tile tile = new Tile(mapTexture);
				tile.setPosition(tempX, tempY);
				tiles.add(tile);
			}
			tempY = workspace.getY() + workspace.getHeight();
		}
		
		chunkGroup = new ChunkGroup();
		chunkGroup.createChunks(workspace.getX(), workspace.getY() + workspace.getHeight(), chunkWidth, chunkHeight, tileWidth,
				tileHeight, tileAmountX, tileAmountY, mapTexture, tiles, workspace.getWidth(), workspace.getHeight());
		tileSprite.setSize(tileWidth, tileHeight);
		
		xWorkspace = workspace.getX();
		yWorkspace = workspace.getY();
		xWidthWorkspace = workspace.getX() + workspace.getWidth();
		yHeightWorkspace = workspace.getY() + workspace.getHeight();
	}
	
	public void loadMap (String path) {
		chunkGroup = new ChunkGroup();
		MapIO.loadMap(path, chunkGroup, mapTextures, wallTextures);
		chunkGroup.lookingForActiveChunks(workspace.getX(), workspace.getY() + workspace.getHeight(), workspace.getWidth(), workspace.getHeight());
		
		tileSprite.setSize(chunkGroup.getTileWidth(), chunkGroup.getTileHeight());
		
		xWorkspace = workspace.getX();
		yWorkspace = workspace.getY();
		xWidthWorkspace = workspace.getX() + workspace.getWidth();
		yHeightWorkspace = workspace.getY() + workspace.getHeight();
		
		for (MapTexture mapTexture : mapTextures) {
			if (mapTexture.getId() != 0) properties.getTexturesTable().addMapTexture(mapTexture);
		}
		
		for (Chunk chunk : chunkGroup.getChunks()) {
			Sprite base = chunkGroup.getBaseOne().getBase();
			if (base.getX() >= chunk.getX() && base.getX() <= chunk.getX() + chunkGroup.getChunkWidth() && base.getY() >= chunk.getY() && base.getY() <= chunk.getY() + chunkGroup.getChunkHeight()) {
				chunk.setBaseOne(chunkGroup.getBaseOne());
			}
			base = chunkGroup.getBaseTwo().getBase();
			if (base.getX() >= chunk.getX() && base.getX() <= chunk.getX() + chunkGroup.getChunkWidth() && base.getY() >= chunk.getY() && base.getY() <= chunk.getY() + chunkGroup.getChunkHeight()) {
				chunk.setBaseTwo(chunkGroup.getBaseTwo());
			}
		}
	}
	
	public void show (SpriteBatch batch) {
		graphManager.reset();
		mapObjectManager.reset();
		
		batch.begin();
		workspace.draw(batch);
		
		if (!properties.isBlockWorkspaceEvents() && fileChooser == null && mapObjectManager.getMapObjectPropertiesForm() == null) {
			Vector3 cur = CameraController.getInstance().unproject();
			if (cur.x >= workspace.getX() && cur.x <= workspace.getX() + workspace.getWidth() && cur.y >= workspace.getY() && cur.y <= workspace.getY() + workspace.getHeight()) {
				if (Events.getInstance().isMouseRightPressed()) {
					cursor = new Vector3(cur);
					isMouseRightPressed = true;
				}
			}
		}
		
		if (isMouseRightPressed) {
			moveX = 0;
			moveY = 0;
			Vector3 tempCur = CameraController.getInstance().unproject();
			moveX = tempCur.x - cursor.x;
			moveY = tempCur.y - cursor.y;
			cursor = new Vector3(tempCur);
			if (Events.getInstance().isMouseRightReleased()) {
				isMouseRightPressed = false;
				moveX = 0;
				moveY = 0;
			}
		}
		
		ScissorStack.calculateScissors(CameraController.getInstance().getCamera(), batch.getTransformMatrix(), workspace.getBoundingRectangle(), scissors);
		ScissorStack.pushScissors(scissors);
		
		if (chunkGroup.isLoaded()) {
			for (int i = 0; i < chunkGroup.getActiveChunks().size; i++) {
				Chunk chunk = chunkGroup.getActiveChunks().get(i);
				
				float chunkXWidth = chunk.getX() + chunkGroup.getChunkWidth();
				float chunkYHeight = chunk.getY() + chunkGroup.getChunkHeight();
				
				if (chunk.getBaseOne() != null) {
					Sprite base = chunkGroup.getBaseOne().getBase();
					if (!(base.getX() >= chunk.getX() && base.getX() <= chunk.getX() + chunkGroup.getChunkWidth() && base.getY() >= chunk.getY() && base.getY() <= chunk.getY() + chunkGroup.getChunkHeight())) {
						for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
							if (base.getX() >= neighbor.getChunk().getX() && base.getX() <= neighbor.getChunk().getX() + chunkGroup.getChunkWidth()
							&& base.getY() >= neighbor.getChunk().getY() && base.getY() <= neighbor.getChunk().getY() + chunkGroup.getChunkHeight()) {
								neighbor.getChunk().setBaseOne(chunk.getBaseOne());
								chunk.setBaseOne(null);
							}
						}
					}
				}
				
				if (chunk.getBaseTwo() != null) {
					Sprite base = chunkGroup.getBaseTwo().getBase();
					if (!(base.getX() >= chunk.getX() && base.getX() <= chunk.getX() + chunkGroup.getChunkWidth() && base.getY() >= chunk.getY() && base.getY() <= chunk.getY() + chunkGroup.getChunkHeight())) {
						for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
							if (base.getX() >= neighbor.getChunk().getX() && base.getX() <= neighbor.getChunk().getX() + chunkGroup.getChunkWidth()
							&& base.getY() >= neighbor.getChunk().getY() && base.getY() <= neighbor.getChunk().getY() + chunkGroup.getChunkHeight()) {
								neighbor.getChunk().setBaseTwo(chunk.getBaseTwo());
								chunk.setBaseTwo(null);
							}
						}
					}
				}
				
				for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
					if (!neighbor.getChunk().isActivated()) {
						if (neighbor.getChunkSide() == ChunkSide.LEFT) {
							if (chunk.getX() > xWorkspace && !((chunk.getY() > yHeightWorkspace && chunkYHeight > yHeightWorkspace) || (chunk.getY() < yWorkspace && chunkYHeight < yWorkspace))) {
								neighbor.getChunk().setActivated(true);
								neighbor.getChunk().changePosition(chunk.getX() - chunkGroup.getChunkWidth() - ChunkGroup.shift, chunk.getY());
								chunkGroup.getActiveChunks().add(neighbor.getChunk());
							}
						} else if (neighbor.getChunkSide() == ChunkSide.RIGHT) {
							if (chunkXWidth < xWidthWorkspace && !((chunk.getY() > yHeightWorkspace && chunkYHeight > yHeightWorkspace) || (chunk.getY() < yWorkspace && chunkYHeight < yWorkspace))) {
								neighbor.getChunk().setActivated(true);
								neighbor.getChunk().changePosition(chunk.getX() + chunkGroup.getChunkWidth() + ChunkGroup.shift, chunk.getY());
								chunkGroup.getActiveChunks().add(neighbor.getChunk());
							}
						} else if (neighbor.getChunkSide() == ChunkSide.UP) {
							if (chunkYHeight < yHeightWorkspace && !((chunk.getX() > xWidthWorkspace && chunkXWidth > xWidthWorkspace) || (chunk.getX() < xWorkspace && chunkXWidth < xWorkspace))) {
								neighbor.getChunk().setActivated(true);
								neighbor.getChunk().changePosition(chunk.getX(), chunk.getY() + chunkGroup.getChunkHeight() + ChunkGroup.shift);
								chunkGroup.getActiveChunks().add(neighbor.getChunk());
							}
						} else if (neighbor.getChunkSide() == ChunkSide.DOWN) {
							if (chunk.getY() > yWorkspace && !((chunk.getX() > xWidthWorkspace && chunkXWidth > xWidthWorkspace) || (chunk.getX() < xWorkspace && chunkXWidth < xWorkspace))) {
								neighbor.getChunk().setActivated(true);
								neighbor.getChunk().changePosition(chunk.getX(), chunk.getY() - chunkGroup.getChunkHeight() - ChunkGroup.shift);
								chunkGroup.getActiveChunks().add(neighbor.getChunk());
							}
						}
					}
				}
				
				chunk.changePosition(chunk.getX() + moveX, chunk.getY() + moveY);
			}
			
			for (int i = 0; i < chunkGroup.getActiveChunks().size; i++) {
				Chunk chunk = chunkGroup.getActiveChunks().get(i);
				
				float chunkXWidth = chunk.getX() + chunkGroup.getChunkWidth();
				float chunkYHeight = chunk.getY() + chunkGroup.getChunkHeight();
				
				if (!((chunk.getX() >= xWorkspace && chunk.getX() <= xWidthWorkspace && chunk.getY() >= yWorkspace && chunk.getY() <= yHeightWorkspace)
						|| (chunkXWidth >= xWorkspace && chunkXWidth <= xWidthWorkspace && chunk.getY() >= yWorkspace && chunk.getY() <= yHeightWorkspace)
						|| (chunk.getX() >= xWorkspace && chunk.getX() <= xWidthWorkspace && chunkYHeight >= yWorkspace && chunkYHeight <= yHeightWorkspace)
						|| (chunkXWidth >= xWorkspace && chunkXWidth <= xWidthWorkspace && chunkYHeight >= yWorkspace && chunkYHeight <= yHeightWorkspace))) {
					if (chunkGroup.getActiveChunks().size > 1) {
						chunkGroup.getActiveChunks().removeIndex(i);
						i--;
						chunk.setActivated(false);
						continue;
					}
				}
				
				for (Tile tile : chunk.getTiles()) {
					tileSprite.setRegion(tile.getMapTexture().getTexture());
					tileSprite.setPosition(tile.getX(), tile.getY());
					tileSprite.draw(batch);
					if (isBorder) {
						border.setPosition(tile.getX(), tile.getY());
						border.setSize(chunkGroup.getTileWidth(), 1);
						border.draw(batch);
						
						border.setPosition(tile.getX(), tile.getY() + chunkGroup.getTileHeight());
						border.setSize(chunkGroup.getTileWidth(), 1);
						border.draw(batch);
						
						border.setPosition(tile.getX(), tile.getY());
						border.setSize(1, chunkGroup.getTileHeight());
						border.draw(batch);
						
						border.setPosition(tile.getX() + chunkGroup.getTileWidth(), tile.getY());
						border.setSize(1, chunkGroup.getTileHeight());
						border.draw(batch);
					}
					if (!properties.isBlockWorkspaceEvents() && fileChooser == null && mapObjectManager.getMapObjectPropertiesForm() == null) {
						if (Events.getInstance().isMouseLeftReleased()) {
							Vector3 cur = CameraController.getInstance().unproject();
							if (cur.x >= workspace.getX() && cur.x <= workspace.getX() + workspace.getWidth() && cur.y >= workspace.getY() && cur.y <= workspace.getY() + workspace.getHeight()) {
								if (cur.x >= tileSprite.getX() && cur.x <= tileSprite.getX() + tileSprite.getWidth() && cur.y >= tileSprite.getY() && cur.y <= tileSprite.getY() + tileSprite.getHeight()) {
									if (properties.getTexturesTable().isSelected()) {
										tile.setMapTexture(properties.getTexturesTable().getSelectedItem().getMapTexture());
									}
								}
							}
						}
					}
				}
			}
			
			for (Chunk chunk : chunkGroup.getActiveChunks()) {
				if (isGraph) {
					graphManager.show(batch, chunk, workspace, chunkGroup.getChunkWidth(), chunkGroup.getChunkHeight());
				} else {
					if (lowerPane.getAddMapObjectButton().isReleased()) {
						MapObject mapObject = new MapObject();
						mapObject.setPosition(chunk.getX() + chunkGroup.getChunkWidth() / 2, chunk.getY() + chunkGroup.getChunkHeight() / 2);
						mapObject.setSize(50, 50);
						mapObject.setWholeTexture(wallTextures.get(0));
						chunk.getMapObjects().add(mapObject);
						lowerPane.getAddMapObjectButton().reset();
					}
				}
				mapObjectManager.show(batch, chunk, workspace, wallTextures, chunkGroup.getChunkWidth(), chunkGroup.getChunkHeight(), isBorder);
				if (isBorder) {
					chunkBorder.setPosition(chunk.getX(), chunk.getY());
					chunkBorder.setSize(chunkGroup.getChunkWidth(), 1);
					chunkBorder.draw(batch);
					
					chunkBorder.setPosition(chunk.getX(), chunk.getY() + chunkGroup.getChunkHeight());
					chunkBorder.setSize(chunkGroup.getChunkWidth(), 1);
					chunkBorder.draw(batch);
					
					chunkBorder.setPosition(chunk.getX(), chunk.getY());
					chunkBorder.setSize(1, chunkGroup.getChunkHeight());
					chunkBorder.draw(batch);
					
					chunkBorder.setPosition(chunk.getX() + chunkGroup.getChunkWidth(), chunk.getY());
					chunkBorder.setSize(1, chunkGroup.getChunkHeight());
					chunkBorder.draw(batch);
				}
			}
			
			if (lowerPane.getPointsButton().isReleased()) {
				isGraph = isGraph ? false : true;
			}
			
			if (lowerPane.getAddBaseOneButton().isReleased()) {
				chunkGroup.getBaseOne().setDisabled(chunkGroup.getBaseOne().isDisabled() ? false : true);
			}
			if (lowerPane.getAddBaseTwoButton().isReleased()) {
				chunkGroup.getBaseTwo().setDisabled(chunkGroup.getBaseTwo().isDisabled() ? false : true);
			}
			
			if (isGraph) {
				if (lowerPane.getSpawnOneButton().isReleased()) {
					if (graphManager.isSpawnOne()) {
						graphManager.setSpawnOne(false);
					} else {
						graphManager.setSpawnOne(true);
						graphManager.setSpawnTwo(false);
					}
				}
				
				if (lowerPane.getSpawnTwoButton().isReleased()) {
					if (graphManager.isSpawnTwo()) {
						graphManager.setSpawnTwo(false);
					} else {
						graphManager.setSpawnTwo(true);
						graphManager.setSpawnOne(false);
					}
				}	
			} else {
				if (lowerPane.getSaveMapButton().isReleased()) {
					fileChooser = new FileChooserForm();
				}
			}
		}
		
		if (Events.getInstance().isTextInput()) {
			if (Gdx.input.isKeyPressed(Keys.B)) {
				isBorder = isBorder ? false : true;
			}
		}
		
		chunkGroup.getBaseOne().show(batch);
		chunkGroup.getBaseTwo().show(batch);
		
		batch.flush();
		ScissorStack.popScissors();
		
		lowerPane.show(batch);
		
		batch.end();
		
		properties.show(batch);
		
		if (fileChooser != null) {
			fileChooser.show(batch);
			if (fileChooser.isCancel()) {
				fileChooser.dispose();
				fileChooser = null;
			} else if (fileChooser.isOpen()) {
				MapIO.saveMap(this, fileChooser.getPath());
				fileChooser.dispose();
				fileChooser = null;
			}
		}
		
		if (mapObjectManager.getMapObjectPropertiesForm() != null) {
			mapObjectManager.getMapObjectPropertiesForm().show(batch);
		}
	}
	
	public Array <MapTexture> getMapTextures () {
		return mapTextures;
	}
	
	public Array <WallTexture> getWallTextures () {
		return wallTextures;
	}
	
	public ChunkGroup getChunkGroup () {
		return chunkGroup;
	}
	
	@Override
	public void dispose () {
		workspace.getTexture().dispose();
		border.getTexture().dispose();
		chunkBorder.getTexture().dispose();
		lowerPane.dispose();
		for (MapTexture mapTexture : mapTextures) mapTexture.dispose();
		mapTextures.clear();
		mapObjectManager.dispose();
		graphManager.dispose();
	}
}