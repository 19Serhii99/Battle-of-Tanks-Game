package map;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.editor.Events;

import forms.MapObjectPropertiesForm;
import util.CameraController;
import util.TextureCreator;

public class MapObjectManager implements Disposable {
	private Sprite object;
	private Sprite border;
	private Sprite selectedBorder;
	private MapObjectBase selectedObject;
	private Chunk selectedChunk;
	private MapObjectPropertiesForm mapObjectPropertiesForm;
	private Array <MapObjectBase> selectedObjects;
	
	private boolean isBorder = true;
	private boolean isMoving = true;
	
	public MapObjectManager () {
		object = new Sprite();
		
		border = new Sprite(TextureCreator.createTexture(Color.WHITE));
		selectedBorder = new Sprite(TextureCreator.createTexture(Color.GREEN));
		
		selectedObjects = new Array <MapObjectBase>();
	}
	
	public void show (SpriteBatch batch, Chunk chunk, Sprite workspace, Array <WallTexture> wallTextures, float chunkWidth, float chunkHeight, boolean isBorder) {
		for (int i = 0; i < chunk.getMapObjects().size; i++) {
			MapObjectBase mapObject = chunk.getMapObjects().get(i);
			if (!mapObject.isWrapper()) {
				object.setRegion(((MapObject) mapObject).getWholeTexture().getTexture());
				object.setSize(mapObject.getWidth(), mapObject.getHeight());
				object.setOriginCenter();
				object.setPosition(mapObject.getX(), mapObject.getY());
				object.setRotation(((MapObject) mapObject).getRotation());
				object.draw(batch);
			} else if (mapObject.isWrapper()) {
				for (MapObjectBase mapObjectBase : ((MapObjectWrapper)mapObject).getMapObjects()) {
					object.setRegion(((MapObject) mapObjectBase).getWholeTexture().getTexture());
					object.setSize(mapObjectBase.getWidth(), mapObjectBase.getHeight());
					object.setOriginCenter();
					object.setPosition(mapObjectBase.getX(), mapObjectBase.getY());
					object.setRotation(((MapObject) mapObjectBase).getRotation());
					object.draw(batch);
				}
			}
			
			Sprite border = null;
			
			if (selectedObject != null) {
				if (selectedObject.equals(mapObject)) {
					border = selectedBorder;
				} else {
					border = this.border;
				}
			} else {
				border = this.border;
			}
			
			if (Events.getInstance().isMouseLeftReleased()) {
				if (selectedObject == null) {
					Vector3 cursor = CameraController.getInstance().unproject();
					if (cursor.x >= mapObject.getX() && cursor.x <= mapObject.getX() + mapObject.getWidth() && cursor.y >= mapObject.getY() && cursor.y <= mapObject.getY() + mapObject.getHeight()) {
						if (cursor.x >= workspace.getX() && cursor.x <= workspace.getX() + workspace.getWidth() && cursor.y >= workspace.getY() && cursor.y <= workspace.getY() + workspace.getHeight()) {
							selectedObject = mapObject;
							selectedChunk = chunk;
						}
					}
				}
				if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT)) {
					Vector3 cursor = CameraController.getInstance().unproject();
					if (cursor.x >= mapObject.getX() && cursor.x <= mapObject.getX() + mapObject.getWidth() && cursor.y >= mapObject.getY() && cursor.y <= mapObject.getY() + mapObject.getHeight()) {
						if (cursor.x >= workspace.getX() && cursor.x <= workspace.getX() + workspace.getWidth() && cursor.y >= workspace.getY() && cursor.y <= workspace.getY() + workspace.getHeight()) {
							selectedObjects.add(mapObject);
						}
					}
				}
			}
			
			if (mapObjectPropertiesForm != null) {
				if (mapObjectPropertiesForm.isApply()) {
					selectedObject.setPosition(mapObjectPropertiesForm.getX(), mapObjectPropertiesForm.getY());
					if (!(selectedObject.getX() >= selectedChunk.getX() && selectedObject.getX() <= selectedChunk.getX() + chunkWidth && selectedObject.getY() >= selectedChunk.getY() && selectedObject.getY() <= selectedChunk.getY() + chunkHeight)) {
						for (int j = 0; j < selectedChunk.getNeighbors().size; j++) {
							Chunk neighbor = selectedChunk.getNeighbors().get(j).getChunk();
							if (selectedObject.getX() >= neighbor.getX() && selectedObject.getX() <= neighbor.getX() + chunkWidth && selectedObject.getY() >= neighbor.getY() && selectedObject.getY() <= neighbor.getY() + chunkHeight) {
								neighbor.getMapObjects().add(selectedObject);
								for (int k = 0; k < selectedChunk.getMapObjects().size; k++) {
									if (selectedObject.equals(selectedChunk.getMapObjects().get(k))) {
										selectedChunk.getMapObjects().removeIndex(k);
										break;
									}
								}
								selectedChunk = neighbor;
								break;
							}
						}
					}
					selectedObject.setSize(mapObjectPropertiesForm.getWidth(), mapObjectPropertiesForm.getHeight());
					MapObject obj = (MapObject) selectedObject;
					obj.setWall(mapObjectPropertiesForm.isWall());
					obj.setRotation(mapObjectPropertiesForm.getRotation());
					obj.setDestroyedWall(mapObjectPropertiesForm.isDestroyed());
					if (!mapObjectPropertiesForm.getWholeTexturePath().equals(obj.getWholeTexture().getPath())) {
						obj.getWholeTexture().using--;
						if (obj.getWholeTexture().using == 0) {
							if (obj.getWholeTexture().getId() != 0) {
								obj.getWholeTexture().getTexture().dispose();
								Iterator <WallTexture> wallTexture = wallTextures.iterator();
								while (wallTexture.hasNext()) {
									if (wallTexture.next().equals(obj.getWholeTexture())) {
										wallTexture.remove();
									}
								}
							}
						}
						if (mapObjectPropertiesForm.getWholeTexturePath().length() > 3) {
							boolean isFound = false;
							for (WallTexture wallTexture : wallTextures) {
								if (wallTexture.getPath().equals(mapObjectPropertiesForm.getWholeTexturePath())) {
									isFound = true;
									obj.setWholeTexture(wallTexture);
									wallTexture.using++;
									break;
								}
							}
							if (!isFound) {
								try {
									Texture texture = new Texture(Gdx.files.absolute(mapObjectPropertiesForm.getWholeTexturePath()));
									texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
									WallTexture wt = new WallTexture(wallTextures.size, texture, mapObjectPropertiesForm.getWholeTexturePath());
									wallTextures.add(wt);
									wt.using++;
									obj.setWholeTexture(wt);
								} catch (GdxRuntimeException e) {
									System.out.println("Не удалось загрузить текстуру : " + mapObjectPropertiesForm.getWholeTexturePath());
								}
							}
						} else {
							obj.setWholeTexture(wallTextures.get(0));
							obj.getWholeTexture().using++;
						}
					}
					if (mapObjectPropertiesForm.isDestroyed()) {
						if (!mapObjectPropertiesForm.getDestroyedTexturePath().equals(obj.getDestroyedTexture().getPath())) {
							obj.getDestroyedTexture().using--;
							if (obj.getDestroyedTexture().using == 0) {
								if (obj.getDestroyedTexture().getId() != 0) {
									obj.getDestroyedTexture().getTexture().dispose();
									Iterator <WallTexture> wallTexture = wallTextures.iterator();
									while (wallTexture.hasNext()) {
										if (wallTexture.next().equals(obj.getDestroyedTexture())) {
											wallTexture.remove();
										}
									}
								}
							}
							if (mapObjectPropertiesForm.getDestroyedTexturePath().length() > 3) {
								boolean isFound = false;
								for (WallTexture wallTexture : wallTextures) {
									if (wallTexture.getPath().equals(mapObjectPropertiesForm.getDestroyedTexturePath())) {
										isFound = true;
										obj.setDestroyedTexture(wallTexture);
										wallTexture.using++;
										break;
									}
								}
								if (!isFound) {
									try {
										Texture texture = new Texture(Gdx.files.absolute(mapObjectPropertiesForm.getDestroyedTexturePath()));
										texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
										WallTexture wt = new WallTexture(wallTextures.size, texture, mapObjectPropertiesForm.getDestroyedTexturePath());
										wallTextures.add(wt);
										wt.using++;
										obj.setDestroyedTexture(wt);
									} catch (GdxRuntimeException e) {
										System.out.println("Не удалось загрузить текстуру : " + mapObjectPropertiesForm.getDestroyedTexturePath());
									}
								}
							} else {
								obj.setDestroyedTexture(wallTextures.get(0));
								obj.getDestroyedTexture().using++;
							}
						}
					}
					mapObjectPropertiesForm.dispose();
					mapObjectPropertiesForm = null;
				}
			} else {
				if (selectedObject != null) {
					if (Events.getInstance().isTextInput()) {
						if (Gdx.input.isKeyPressed(Keys.FORWARD_DEL)) {
							for (int j = 0; j < selectedChunk.getMapObjects().size; j++) {
								MapObjectBase obj = selectedChunk.getMapObjects().get(j);
								if (((MapObject)selectedObject).equals((MapObject)obj)) {
									selectedChunk.getMapObjects().removeIndex(j);
									break;
								}
							}
						} else if (Gdx.input.isKeyPressed(Keys.ENTER)) {
							if (selectedObject.getClass() == MapObject.class && mapObjectPropertiesForm == null) mapObjectPropertiesForm = new MapObjectPropertiesForm((MapObject)selectedObject);
						} else if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
							selectedObject = null;
							selectedChunk = null;
							selectedObjects.clear();
						} else if (Gdx.input.isKeyPressed(Keys.G)) {
							MapObjectWrapper wrapper = new MapObjectWrapper();
							for (MapObjectBase object : selectedObjects) {
								MapObject mapObjectTemp = (MapObject) object;
								wrapper.getMapObjects().add(mapObjectTemp);
								for (int j = 0; j < selectedChunk.getMapObjects().size; j++) {
									if (object.equals(selectedChunk.getMapObjects().get(j))) {
										selectedChunk.getMapObjects().removeIndex(j);
										break;
									}
								}
							}
							wrapper.resize();
							selectedChunk.getMapObjects().add(wrapper);
							selectedObjects.clear();
						} else if (Gdx.input.isKeyPressed(Keys.U)) {
							MapObjectWrapper wrapper = (MapObjectWrapper) selectedObject;
							for (MapObjectBase object : wrapper.getMapObjects()) {
								MapObject mapObjectTemp = (MapObject) object;
								selectedChunk.getMapObjects().add(mapObjectTemp);
							}
							for (int j = 0; j < selectedChunk.getMapObjects().size; j++) {
								if (wrapper.equals(selectedChunk.getMapObjects().get(j))) {
									selectedChunk.getMapObjects().removeIndex(j);
									break;
								}
							}
							wrapper.getMapObjects().clear();
						} else {
							if (isMoving) {
								float x = 0, y = 0;
								
								if (Gdx.input.isKeyPressed(Keys.W)) {
									if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
										y = selectedObject.getY() + 1;
									} else {
										y = selectedObject.getY() + 5;
									}
									x = selectedObject.getX();
								}
								
								if (Gdx.input.isKeyPressed(Keys.S)) {
									if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
										y = selectedObject.getY() - 1;
									} else {
										y = selectedObject.getY() - 5;
									}
									x = selectedObject.getX();
								}
								
								if (Gdx.input.isKeyPressed(Keys.D)) {
									if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
										x = selectedObject.getX() + 1;
									} else {
										x = selectedObject.getX() + 5;
									}
									y = selectedObject.getY();
								}
								
								if (Gdx.input.isKeyPressed(Keys.A)) {
									if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
										x = selectedObject.getX() - 1;
									} else {
										x = selectedObject.getX() - 5;
									}
									y = selectedObject.getY();
								}
								
								if (x != 0.0f || y != 0.0f) {
									if (!selectedObject.isWrapper()) {
										selectedObject.setPosition(x, y);
										if (!(x >= selectedChunk.getX() && x <= selectedChunk.getX() + chunkWidth && y >= selectedChunk.getY() && y <= selectedChunk.getY() + chunkHeight)) {
											for (int j = 0; j < selectedChunk.getNeighbors().size; j++) {
												Chunk neighbor = selectedChunk.getNeighbors().get(j).getChunk();
												if (x >= neighbor.getX() && x <= neighbor.getX() + chunkWidth && y >= neighbor.getY() && y <= neighbor.getY() + chunkHeight) {
													neighbor.getMapObjects().add(selectedObject);
													for (int k = 0; k < selectedChunk.getMapObjects().size; k++) {
														if (selectedObject.equals(selectedChunk.getMapObjects().get(k))) {
															selectedChunk.getMapObjects().removeIndex(k);
															break;
														}
													}
													selectedChunk = neighbor;
													break;
												}
											}
										}
										isMoving = false;
									}
								}
							}
						}
					}
				}
			}
			
			if (isBorder) {
				border.setSize(mapObject.getWidth(), 1);
				border.setPosition(mapObject.getX(), mapObject.getY());
				border.draw(batch);
				
				border.setSize(mapObject.getWidth(), 1);
				border.setPosition(mapObject.getX(), mapObject.getY() + mapObject.getHeight() - border.getHeight());
				border.draw(batch);
				
				border.setSize(1, mapObject.getHeight());
				border.setPosition(mapObject.getX(), mapObject.getY());
				border.draw(batch);
				
				border.setSize(1, mapObject.getHeight());
				border.setPosition(mapObject.getX() + mapObject.getWidth() - border.getWidth(), mapObject.getY());
				border.draw(batch);
			}
		}
	}
	
	public void reset () {
		isBorder = true;
		isMoving = true;
	}
	
	public void setEnabledBorder (boolean enabled) {
		this.isBorder = enabled;
	}
	
	public boolean isBorder () {
		return isBorder;
	}
	
	public MapObjectPropertiesForm getMapObjectPropertiesForm () {
		return mapObjectPropertiesForm;
	}
	
	@Override
	public void dispose () {
		border.getTexture().dispose();
		selectedBorder.getTexture().dispose();
	}
}