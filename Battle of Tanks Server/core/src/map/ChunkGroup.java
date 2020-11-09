package map;

import com.badlogic.gdx.utils.Array;

public class ChunkGroup {
	private float chunkWidth;
	private float chunkHeight;
	private float tileWidth;
	private float tileHeight;
	private int chunkAmountX;
	private int chunkAmountY;
	private int tileAmountX;
	private int tileAmountY;
	
	private float mapWidth;
	private float mapHeight;
	
	private boolean isLoaded = false;
	
	private Array <Chunk> chunks;
	private Array <Chunk> activeChunks;
	
	public final static float shift = 0.0001f;
	
	public ChunkGroup () {
		chunks = new Array <Chunk>(1);
		activeChunks = new Array <Chunk>(1);
	}
	
	public void loadMap (final String path, final Array <MapTexture> mapTextures, final Array <MapTexture> wallTextures, final Array <Spawn> spawnCommandOne, final Array <Spawn> spawnCommandTwo) {
		final ChunkGroup group = this;
		new Thread (new Runnable () {
			@Override
			public void run () {
				MapInput.loadMap(path, group, mapTextures, wallTextures, spawnCommandOne, spawnCommandTwo);
				lookingForLastChunks();
				isLoaded = true;
			}
		}).start();
	}
	
	public void lookingForNeighbors () {
		for (int i = 0; i < chunks.size; i++) {
			for (int j = 0; j < chunks.size; j++) {
				if (i != j) {
					if ((Math.abs(chunks.get(i).getX() - chunks.get(j).getX()) <= 10 && Math.abs(chunks.get(i).getY() - chunks.get(j).getY()) <= chunkHeight + 10)
							|| (Math.abs(chunks.get(i).getX() - chunks.get(j).getX()) <= chunkWidth + 10 && Math.abs(chunks.get(i).getY() - chunks.get(j).getY()) <= 10)) {
						ChunkSide side;
						if (chunks.get(i).getX() < chunks.get(j).getX() && chunks.get(i).getY() == chunks.get(j).getY()) {
							side = ChunkSide.LEFT;
						} else if (chunks.get(i).getX() > chunks.get(j).getX() && chunks.get(i).getY() == chunks.get(j).getY()) {
							side = ChunkSide.RIGHT;
						} else if (chunks.get(i).getX() == chunks.get(j).getX() && chunks.get(i).getY() > chunks.get(j).getY()) {
							side = ChunkSide.UP;
						} else {
							side = ChunkSide.DOWN;
						}
						chunks.get(j).getNeighbors().add(new ChunkNeighbor(chunks.get(i), side));
					}
				}
			}
		}
	}
	
	public void lookingForActiveChunks (float startX, float startY, float workspaceWidth, float workspaceHeight) {
		for (Chunk chunk : chunks) {
			if ((chunk.getX() >= startX && chunk.getX() <= startX + workspaceWidth && chunk.getY() <= startY && chunk.getY() >= startY - workspaceHeight)
					|| (chunk.getX() >= startX && chunk.getX() <= startX + workspaceWidth && chunk.getY() + chunkHeight <= startY && chunk.getY() + chunkHeight >= startY - workspaceHeight)
					|| (chunk.getX() + chunkWidth >= startX && chunk.getX() + chunkWidth <= startX + workspaceWidth && chunk.getY() <= startY && chunk.getY() >= startY - workspaceHeight)
					|| (chunk.getX() + chunkWidth >= startX && chunk.getX() + chunkWidth <= startX + workspaceWidth && chunk.getY() + chunkHeight <= startY && chunk.getY() + chunkHeight >= startY - workspaceHeight)) {
				activeChunks.add(chunk);
				chunk.setActivated(true);
			}
		}
	}
	
	private void lookingForLastChunks () {
		for (Chunk chunk : chunks) {
			boolean wasRight = false;
			boolean wasBottom = false;
			for (ChunkNeighbor neighbor : chunk.getNeighbors()) {
				if (neighbor.getChunkSide() == ChunkSide.RIGHT) {
					wasRight = true;
				} else if (neighbor.getChunkSide() == ChunkSide.DOWN) {
					wasBottom = true;
				}
			}
			if (!wasRight) {
				chunk.setRightLast(true);
				float x = 0;
				for (Tile tile : chunk.getTiles()) {
					if (tile.getX() + tileWidth > x) {
						x = tile.getX() + tileWidth;
					}
				}
				mapWidth = x;
				chunk.setVirtualWidth(x - chunk.getX());
			}
			if (!wasBottom) {
				chunk.setBottomLast(true);
				float y = 0;
				for (Tile tile : chunk.getTiles()) {
					if (tile.getY() + tileHeight > y) {
						y = tile.getY() + tileHeight;
					}
				}
				chunk.setVirtualHeight(y - chunk.getY());
				mapHeight = chunk.getY() + chunk.getVirtualHeight() + chunkHeight * (chunkAmountY - 1);
			}
		}
	}
	
	public void setTileAmount (int x, int y) {
		tileAmountX = x;
		tileAmountY = y;
	}
	
	public void setChunkSize (float width, float height) {
		chunkWidth = width;
		chunkHeight = height;
	}
	
	public void setTileSize (float width, float height) {
		tileWidth = width;
		tileHeight = height;
	}
	
	public void setChunkAmount (int x, int y) {
		chunkAmountX = x;
		chunkAmountY = y;
	}
	
	public float getChunkWidth () {
		return chunkWidth;
	}
	
	public float getChunkHeight () {
		return chunkHeight;
	}
	
	public float getTileWidth () {
		return tileWidth;
	}
	
	public float getTileHeight () {
		return tileHeight;
	}
	
	public int getTileAmountX () {
		return tileAmountX;
	}
	
	public int getTileAmountY () {
		return tileAmountY;
	}
	
	public int getChunkAmountX () {
		return chunkAmountX;
	}
	
	public int getChunkAmountY () {
		return chunkAmountY;
	}
	
	public Array <Chunk> getChunks () {
		return chunks;
	}
	
	public boolean isLoaded () {
		return isLoaded;
	}
	
	public Array <Chunk> getActiveChunks () {
		return activeChunks;
	}
	
	public float getMapWidth () {
		return mapWidth;
	}
	
	public float getMapHeight () {
		return mapHeight;
	}
}