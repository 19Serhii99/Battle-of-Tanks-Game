package map;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
	private boolean isLoaded = false;
	
	private Array <Chunk> chunks;
	private Array <Chunk> activeChunks;
	private BaseManager baseOne;
	private BaseManager baseTwo;
	
	public final static float shift = 0.0001f;
	
	public ChunkGroup () {
		chunks = new Array <Chunk>(1);
		activeChunks = new Array <Chunk>(1);
		baseOne = new BaseManager(100, 500);
		baseTwo = new BaseManager(100, 500);
	}
	
	public void createChunks (final float startX, final float startY, final float _chunkWidth, final float _chunkHeight, final float _tileWidth,
			final float _tileHeight, final int _tileAmountX, final int _tileAmountY, final MapTexture defaultMapTexture, final Array <Tile> tiles,
			final float workspaceWidth, final float workspaceHeight) {
		
		new Thread (new Runnable () {
			@Override
			public void run () {
				chunkWidth = _chunkWidth;
				chunkHeight = _chunkHeight;
				tileWidth = _tileWidth;
				tileHeight = _tileHeight;
				tileAmountX = _tileAmountX;
				tileAmountY = _tileAmountY;
				
				chunkAmountX = (int)((tileAmountX * tileWidth) / chunkWidth);
				chunkAmountY = (int)((tileAmountY * tileHeight) / chunkHeight);
				
				float tempX = startX - chunkWidth;
				float tempY = startY;
				
				for (int i = 0; i < chunkAmountX; i++) {
					tempX += i == 0 ? chunkWidth : chunkWidth + shift;
					for (int j = 0; j < chunkAmountY; j++) {
						tempY -= j == 0 ? chunkHeight : chunkHeight + shift;
						Chunk chunk = new Chunk(tempX, tempY);
						for (Tile tile : tiles) {
							if (tile.getX() >= chunk.getX() && tile.getX() <= chunk.getX() + chunkWidth && tile.getY() + tileHeight >= chunk.getY() && tile.getY() + tileHeight <= chunk.getY() + chunkHeight) {
								chunk.getTiles().add(tile);
							}
						}
						chunks.add(chunk);
					}
					tempY = startY;
				}
				
				lookingForNeighbors();
				lookingForActiveChunks(startX, startY, workspaceWidth, workspaceHeight);
				
				for (Chunk chunk : getChunks()) {
					Sprite base = getBaseOne().getBase();
					if (base.getX() >= chunk.getX() && base.getX() <= chunk.getX() + getChunkWidth() && base.getY() >= chunk.getY() && base.getY() <= chunk.getY() + getChunkHeight()) {
						chunk.setBaseOne(getBaseOne());
					}
					base = getBaseTwo().getBase();
					if (base.getX() >= chunk.getX() && base.getX() <= chunk.getX() + getChunkWidth() && base.getY() >= chunk.getY() && base.getY() <= chunk.getY() + getChunkHeight()) {
						chunk.setBaseTwo(getBaseTwo());
					}
				}
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
		isLoaded = true;
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
	
	public BaseManager getBaseOne () {
		return baseOne;
	}
	
	public BaseManager getBaseTwo () {
		return baseTwo;
	}
}