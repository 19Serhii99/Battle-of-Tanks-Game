package map;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import net.Player;
import technique.Shell;
import util.CameraController;
import util.TextureCreator;

public class Map implements Disposable {
	private ChunkGroup chunkGroup;
	private Array <MapTexture> mapTextures;
	private Array <MapTexture> wallTextures;
	private Array <Spawn> spawnCommandOne;
	private Array <Spawn> spawnCommandTwo;
	private AssetManager assetManager;
	private Sprite sprite;
	private Graph graph;
	
	private boolean isLoaded = false;
	private boolean isAccessLoaded = false;	
	
	public Map (String path) {
		mapTextures = new Array <MapTexture>();
		wallTextures = new Array <MapTexture>();
		spawnCommandOne = new Array <Spawn>();
		spawnCommandTwo = new Array <Spawn>();
		
		graph = new Graph();
		
		chunkGroup = new ChunkGroup();
		chunkGroup.loadMap(path, mapTextures, wallTextures, spawnCommandOne, spawnCommandTwo, graph);
		
		assetManager = new AssetManager();
		sprite = new Sprite();
	}
	
	public void act () {
		if (!isLoaded) {
			if (chunkGroup.isLoaded()) {
				if (!isAccessLoaded) {
					for (MapTexture mapTexture : mapTextures) {
						assetManager.load("maps/" + mapTexture.getPath(), Texture.class);
					}
					for (MapTexture wallTexture : wallTextures) {
						assetManager.load("maps/" + wallTexture.getPath(), Texture.class);
					}
					isAccessLoaded = true;
				} else {
					assetManager.update();
					if (assetManager.getProgress() == 1.0f) {
						for (MapTexture mapTexture : mapTextures) {
							mapTexture.setTexture((Texture)assetManager.get("maps/" + mapTexture.getPath()));
						}
						for (MapTexture wallTexture : wallTextures) {
							wallTexture.setTexture((Texture)assetManager.get("maps/" + wallTexture.getPath()));
						}
						isLoaded = true;
					}
				}
			}
		}
	}
	
	public void checkPlayer (Player player) {
		float v[] = player.getTechnique().getCorpsBackground().getVertices();
		Array <Integer> delete = new Array <Integer>(4);
		int size = player.getTechnique().getCurrentChunks().size;
		for (int i = 0; i < size; i++) {
			Chunk chunk = player.getTechnique().getCurrentChunks().get(i);
			float width = chunk.isRightLast() ? chunk.getVirtualWidth() : chunkGroup.getChunkWidth();
			float height = chunk.isBottomLast() ? chunk.getVirtualHeight() : chunkGroup.getChunkHeight();
			if (!(v[Batch.X1] >= chunk.getX() && v[Batch.X1] <= chunk.getX() + width && v[Batch.Y1] >= chunk.getY() && v[Batch.Y1] <= chunk.getY() + height)
					&& !(v[Batch.X2] >= chunk.getX() && v[Batch.X2] <= chunk.getX() + width && v[Batch.Y2] >= chunk.getY() && v[Batch.Y2] <= chunk.getY() + height)
					&& !(v[Batch.X3] >= chunk.getX() && v[Batch.X3] <= chunk.getX() + width && v[Batch.Y3] >= chunk.getY() && v[Batch.Y3] <= chunk.getY() + height)
					&& !(v[Batch.X4] >= chunk.getX() && v[Batch.X4] <= chunk.getX() + width && v[Batch.Y4] >= chunk.getY() && v[Batch.Y4] <= chunk.getY() + height)) {
				for (int j = 0; j < chunk.getNeighbors().size; j++) {
					Chunk neighbor = chunk.getNeighbors().get(j).getChunk();
					width = neighbor.isRightLast() ? neighbor.getVirtualWidth() : chunkGroup.getChunkWidth();
					height = neighbor.isBottomLast() ? neighbor.getVirtualHeight() : chunkGroup.getChunkHeight();
					if ((v[Batch.X1] >= neighbor.getX() && v[Batch.X1] <= neighbor.getX() + width && v[Batch.Y1] >= neighbor.getY() && v[Batch.Y1] <= neighbor.getY() + height)
							|| (v[Batch.X2] >= neighbor.getX() && v[Batch.X2] <= neighbor.getX() + width && v[Batch.Y2] >= neighbor.getY() && v[Batch.Y2] <= neighbor.getY() + height)
							|| (v[Batch.X3] >= neighbor.getX() && v[Batch.X3] <= neighbor.getX() + width && v[Batch.Y3] >= neighbor.getY() && v[Batch.Y3] <= neighbor.getY() + height)
							|| (v[Batch.X4] >= neighbor.getX() && v[Batch.X4] <= neighbor.getX() + width && v[Batch.Y4] >= neighbor.getY() && v[Batch.Y4] <= neighbor.getY() + height)) {
						boolean has = false;
						for (Chunk chunkTemp : player.getTechnique().getCurrentChunks()) {
							if (chunkTemp.equals(neighbor)) {
								has = true;
								break;
							}
						}
						if (!has) player.getTechnique().getCurrentChunks().add(neighbor);
					}
				}
				delete.add(i);
			}
		}
		for (int i = delete.size - 1; i >= 0; i--) {
			player.getTechnique().getCurrentChunks().removeIndex(i);
		}
	}
	
	public void checkShells (LinkedList <Shell> shells) {
		Iterator <Shell> shellIterator = shells.iterator();
		while (shellIterator.hasNext()) {
			Shell shell = shellIterator.next();
			float v[] = shell.getVertices();
			Array <Integer> delete = new Array <Integer>(4);
			int size = shell.getCurrentChunks().size;
			for (int i = 0; i < size; i++) {
				Chunk chunk = shell.getCurrentChunks().get(i);
				if ((!(v[Batch.X1] >= chunk.getX() && v[Batch.X1] <= chunk.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y1] >= chunk.getY() && v[Batch.Y1] <= chunk.getY() + getChunkGroup().getChunkHeight())
						&& !(v[Batch.X2] >= chunk.getX() && v[Batch.X2] <= chunk.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y2] >= chunk.getY() && v[Batch.Y2] <= chunk.getY() + getChunkGroup().getChunkHeight())
						&& !(v[Batch.X3] >= chunk.getX() && v[Batch.X3] <= chunk.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y3] >= chunk.getY() && v[Batch.Y3] <= chunk.getY() + getChunkGroup().getChunkHeight())
						&& !(v[Batch.X4] >= chunk.getX() && v[Batch.X4] <= chunk.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y4] >= chunk.getY() && v[Batch.Y4] <= chunk.getY() + getChunkGroup().getChunkHeight()))) {
					for (int j = 0; j < chunk.getNeighbors().size; j++) {
						Chunk neighbor = chunk.getNeighbors().get(j).getChunk();
						if ((v[Batch.X1] >= neighbor.getX() && v[Batch.X1] <= neighbor.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y1] >= neighbor.getY() && v[Batch.Y1] <= neighbor.getY() + getChunkGroup().getChunkHeight())
								|| (v[Batch.X2] >= neighbor.getX() && v[Batch.X2] <= neighbor.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y2] >= neighbor.getY() && v[Batch.Y2] <= neighbor.getY() + getChunkGroup().getChunkHeight())
								|| (v[Batch.X3] >= neighbor.getX() && v[Batch.X3] <= neighbor.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y3] >= neighbor.getY() && v[Batch.Y3] <= neighbor.getY() + getChunkGroup().getChunkHeight())
								|| (v[Batch.X4] >= neighbor.getX() && v[Batch.X4] <= neighbor.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y4] >= neighbor.getY() && v[Batch.Y4] <= neighbor.getY() + getChunkGroup().getChunkHeight())) {
							boolean has = false;
							for (Chunk chunkTemp : shell.getCurrentChunks()) {
								if (chunkTemp.equals(neighbor)) {
									has = true;
									break;
								}
							}
							if (!has) shell.getCurrentChunks().add(neighbor);
						} else {
							for (int k = 0; k < neighbor.getNeighbors().size; k++) {
								Chunk other = neighbor.getNeighbors().get(k).getChunk();
								if ((v[Batch.X1] >= other.getX() && v[Batch.X1] <= other.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y1] >= other.getY() && v[Batch.Y1] <= other.getY() + getChunkGroup().getChunkHeight())
										|| (v[Batch.X2] >= other.getX() && v[Batch.X2] <= other.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y2] >= other.getY() && v[Batch.Y2] <= other.getY() + getChunkGroup().getChunkHeight())
										|| (v[Batch.X3] >= other.getX() && v[Batch.X3] <= other.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y3] >= other.getY() && v[Batch.Y3] <= other.getY() + getChunkGroup().getChunkHeight())
										|| (v[Batch.X4] >= other.getX() && v[Batch.X4] <= other.getX() + getChunkGroup().getChunkWidth() && v[Batch.Y4] >= other.getY() && v[Batch.Y4] <= other.getY() + getChunkGroup().getChunkHeight())) {
									boolean has = false;
									for (Chunk chunkTemp : shell.getCurrentChunks()) {
										if (chunkTemp.equals(other)) {
											has = true;
											break;
										}
									}
									if (!has) shell.getCurrentChunks().add(other);
								}
							}
						}
					}
					delete.add(i);
				}
			}
			for (int i = delete.size - 1; i >= 0; i--) {
				shell.getCurrentChunks().removeIndex(i);
			}
		}
	}
	
	public void show (SpriteBatch batch) {
		Texture tempTexture = TextureCreator.createTexture(Color.WHITE);
		Sprite tempSprite = new Sprite(tempTexture);
		
		CameraController camera = CameraController.getInstance();
		
		Iterator <Chunk> chunkIterator = chunkGroup.getActiveChunks().iterator();
		
		while (chunkIterator.hasNext()) {
			Chunk chunk = chunkIterator.next();
			if (chunk.getX() <= camera.getX() - camera.getWidth() / 2 - chunkGroup.getChunkWidth() * 2 || chunk.getX() >= camera.getX() + camera.getWidth() / 2 + chunkGroup.getChunkWidth()
					|| chunk.getY() <= camera.getY() - camera.getHeight() / 2 - chunkGroup.getChunkHeight() * 2 || chunk.getY() >= camera.getY() + camera.getHeight() / 2 + chunkGroup.getChunkHeight()) {
				chunk.setActivated(false);
				chunkIterator.remove();
			} else {
				for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
					if (!neighbor.getChunk().isActivated()) {
						if (neighbor.getChunkSide() == ChunkSide.LEFT) {
							neighbor.getChunk().setActivated(true);
							neighbor.getChunk().changePosition(chunk.getX() - chunkGroup.getChunkWidth() - ChunkGroup.shift, chunk.getY());
							chunkGroup.getActiveChunks().add(neighbor.getChunk());
						} else if (neighbor.getChunkSide() == ChunkSide.RIGHT) {
							neighbor.getChunk().setActivated(true);
							neighbor.getChunk().changePosition(chunk.getX() + chunkGroup.getChunkWidth() + ChunkGroup.shift, chunk.getY());
							chunkGroup.getActiveChunks().add(neighbor.getChunk());
						} else if (neighbor.getChunkSide() == ChunkSide.UP) {
							neighbor.getChunk().setActivated(true);
							neighbor.getChunk().changePosition(chunk.getX(), chunk.getY() + chunkGroup.getChunkHeight() + ChunkGroup.shift);
							chunkGroup.getActiveChunks().add(neighbor.getChunk());
						} else if (neighbor.getChunkSide() == ChunkSide.DOWN) {
							neighbor.getChunk().setActivated(true);
							neighbor.getChunk().changePosition(chunk.getX(), chunk.getY() - chunkGroup.getChunkHeight() - ChunkGroup.shift);
							chunkGroup.getActiveChunks().add(neighbor.getChunk());
						}
					}
				}
			}
		}
		
		sprite.setSize(chunkGroup.getTileWidth(), chunkGroup.getTileHeight());
		sprite.setRotation(0);
		for (Chunk chunk : chunkGroup.getActiveChunks()) {
			for (Tile tile : chunk.getTiles()) {
				sprite.setRegion(tile.getMapTexture().getTexture());
				sprite.setPosition(tile.getX(), tile.getY());
				sprite.draw(batch);
			}
		}
		
		for (Chunk chunk : chunkGroup.getActiveChunks()) {
			for (MapObjectBase objectBase : chunk.getMapObjects()) {
				if (!objectBase.isWrapper()) {
					MapObject mapObject = (MapObject) objectBase;
					sprite.setSize(mapObject.getWidth(), mapObject.getHeight());
					sprite.setOriginCenter();
					sprite.setPosition(mapObject.getX(), mapObject.getY());
					sprite.setRotation(mapObject.getRotation());
					sprite.setRegion(mapObject.getWholeTexture().getTexture());
					sprite.draw(batch);
					
//					tempSprite.setPosition(sprite.getX(), sprite.getY());
//					tempSprite.setSize(sprite.getWidth(), 2);
//					tempSprite.draw(batch);
//					
//					tempSprite.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight() - 2);
//					tempSprite.setSize(sprite.getWidth(), 2);
//					tempSprite.draw(batch);
//					
//					tempSprite.setPosition(sprite.getX(), sprite.getY());
//					tempSprite.setSize(2, sprite.getHeight());
//					tempSprite.draw(batch);
//					
//					tempSprite.setPosition(sprite.getX() + sprite.getWidth() - 2, sprite.getY());
//					tempSprite.setSize(2, sprite.getHeight());
//					tempSprite.draw(batch);
				}
			}
		}
	}
	
	public boolean isLoaded () {
		return isLoaded;
	}
	
	public Array <Spawn> getSpawnCommandOne () {
		return spawnCommandOne;
	}
	
	public Array <Spawn> getSpawnCommandTwo () {
		return spawnCommandTwo;
	}
	
	public ChunkGroup getChunkGroup () {
		return chunkGroup;
	}
	
	@Override
	public void dispose () {
		assetManager.dispose();
	}
}