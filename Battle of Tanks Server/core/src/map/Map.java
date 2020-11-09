package map;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.server.Player;

import technique.Shell;

public class Map {
	private ChunkGroup chunkGroup;
	private Array<MapTexture> mapTextures;
	private Array<MapTexture> wallTextures;
	private Array<Spawn> spawnCommandOne;
	private Array<Spawn> spawnCommandTwo;

	private String name;

	private boolean isLoaded = false;

	public Map(String path) {
		mapTextures = new Array<MapTexture>();
		wallTextures = new Array<MapTexture>();
		spawnCommandOne = new Array<Spawn>();
		spawnCommandTwo = new Array<Spawn>();

		chunkGroup = new ChunkGroup();
		chunkGroup.loadMap(path, mapTextures, wallTextures, spawnCommandOne, spawnCommandTwo);

		name = path;
	}

	public void act() {
		if (!isLoaded) {
			if (chunkGroup.isLoaded()) {
				isLoaded = true;
			}
		}
	}

	public void checkPlayer(Player player) {
		final float v[] = player.getTechnique().getCorpsBackground().getVertices();
		final Array<Integer> delete = new Array<Integer>(4);
		final int size = player.getTechnique().getCurrentChunks().size();
		for (int i = 0; i < size; i++) {
			final Chunk chunk = player.getTechnique().getCurrentChunks().get(i);

			float width = chunk.isRightLast() ? chunk.getVirtualWidth() : chunkGroup.getChunkWidth();
			float height = chunk.isBottomLast() ? chunk.getVirtualHeight() : chunkGroup.getChunkHeight();

			final float xWidth = chunk.getX() + width;
			final float yWidth = chunk.getY() + height;

			final boolean test11 = v[Batch.X1] >= chunk.getX();
			final boolean test12 = v[Batch.X1] <= xWidth;
			final boolean test13 = v[Batch.Y1] >= chunk.getY();
			final boolean test14 = v[Batch.Y1] <= yWidth;

			final boolean test21 = v[Batch.X2] >= chunk.getX();
			final boolean test22 = v[Batch.X2] <= xWidth;
			final boolean test23 = v[Batch.Y2] >= chunk.getY();
			final boolean test24 = v[Batch.Y2] <= yWidth;

			final boolean test31 = v[Batch.X3] >= chunk.getX();
			final boolean test32 = v[Batch.X3] <= xWidth;
			final boolean test33 = v[Batch.Y3] >= chunk.getY();
			final boolean test34 = v[Batch.Y3] <= yWidth;

			final boolean test41 = v[Batch.X4] >= chunk.getX();
			final boolean test42 = v[Batch.X4] <= xWidth;
			final boolean test43 = v[Batch.Y4] >= chunk.getY();
			final boolean test44 = v[Batch.Y4] <= yWidth;

			final boolean test1 = test11 && test12 && test13 && test14;
			final boolean test2 = test21 && test22 && test23 && test24;
			final boolean test3 = test31 && test32 && test33 && test34;
			final boolean test4 = test41 && test42 && test43 && test44;

			if (!test1 && !test2 && !test3 && !test4) {
				for (int j = 0; j < chunk.getNeighbors().size; j++) {
					final Chunk neighbor = chunk.getNeighbors().get(j).getChunk();

					width = neighbor.isRightLast() ? neighbor.getVirtualWidth() : chunkGroup.getChunkWidth();
					height = neighbor.isBottomLast() ? neighbor.getVirtualHeight() : chunkGroup.getChunkHeight();

					final float xWidthN = neighbor.getX() + width;
					final float yHeightN = neighbor.getY() + height;

					final boolean testN11 = v[Batch.X1] >= neighbor.getX();
					final boolean testN12 = v[Batch.X1] <= xWidthN;
					final boolean testN13 = v[Batch.Y1] >= neighbor.getY();
					final boolean testN14 = v[Batch.Y1] <= yHeightN;

					final boolean testN21 = v[Batch.X2] >= neighbor.getX();
					final boolean testN22 = v[Batch.X2] <= xWidthN;
					final boolean testN23 = v[Batch.Y2] >= neighbor.getY();
					final boolean testN24 = v[Batch.Y2] <= yHeightN;

					final boolean testN31 = v[Batch.X3] >= neighbor.getX();
					final boolean testN32 = v[Batch.X3] <= xWidthN;
					final boolean testN33 = v[Batch.Y3] >= neighbor.getY();
					final boolean testN34 = v[Batch.Y3] <= yHeightN;

					final boolean testN41 = v[Batch.X4] >= neighbor.getX();
					final boolean testN42 = v[Batch.X4] <= xWidthN;
					final boolean testN43 = v[Batch.Y4] >= neighbor.getY();
					final boolean testN44 = v[Batch.Y4] <= yHeightN;

					final boolean testN1 = testN11 && testN12 && testN13 && testN14;
					final boolean testN2 = testN21 && testN22 && testN23 && testN24;
					final boolean testN3 = testN31 && testN32 && testN33 && testN34;
					final boolean testN4 = testN41 && testN42 && testN43 && testN44;

					if (testN1 || testN2 || testN3 || testN4) {
						boolean has = false;
						for (Chunk chunkTemp : player.getTechnique().getCurrentChunks()) {
							if (chunkTemp.equals(neighbor)) {
								has = true;
								break;
							}
						}
						if (!has)
							player.getTechnique().getCurrentChunks().add(neighbor);
					}
				}
				delete.add(i);
			}
		}
		for (int i = delete.size - 1; i >= 0; i--) {
			player.getTechnique().getCurrentChunks().remove(i);
		}
	}

	public void checkShells(Collision collision) {
		final LinkedList<Shell> shells = collision.getShells();
		final Iterator<Shell> shellIterator = shells.iterator();
		while (shellIterator.hasNext()) {
			final Shell shell = shellIterator.next();
			final float v[] = shell.getVertices();
			final Array<Integer> delete = new Array<Integer>(4);
			final int size = shell.getCurrentChunks().size();
			for (int i = 0; i < size; i++) {
				final Chunk chunk = shell.getCurrentChunks().get(i);

				final float xWidth = chunk.getX() + getChunkGroup().getChunkWidth();
				final float yHeight = chunk.getY() + getChunkGroup().getChunkHeight();

				final boolean test11 = v[Batch.X1] >= chunk.getX();
				final boolean test12 = v[Batch.X1] <= xWidth;
				final boolean test13 = v[Batch.Y1] >= chunk.getY();
				final boolean test14 = v[Batch.Y1] <= yHeight;

				final boolean test21 = v[Batch.X2] >= chunk.getX();
				final boolean test22 = v[Batch.X2] <= xWidth;
				final boolean test23 = v[Batch.Y2] >= chunk.getY();
				final boolean test24 = v[Batch.Y2] <= yHeight;

				final boolean test31 = v[Batch.X3] >= chunk.getX();
				final boolean test32 = v[Batch.X3] <= xWidth;
				final boolean test33 = v[Batch.Y3] >= chunk.getY();
				final boolean test34 = v[Batch.Y3] <= yHeight;

				final boolean test41 = v[Batch.X4] >= chunk.getX();
				final boolean test42 = v[Batch.X4] <= xWidth;
				final boolean test43 = v[Batch.Y4] >= chunk.getY();
				final boolean test44 = v[Batch.Y4] <= yHeight;

				final boolean test1 = test11 && test12 && test13 && test14;
				final boolean test2 = test21 && test22 && test23 && test24;
				final boolean test3 = test31 && test32 && test33 && test34;
				final boolean test4 = test41 && test42 && test43 && test44;

				if (!test1 && !test2 && !test3 && !test4) {
					for (int j = 0; j < chunk.getNeighbors().size; j++) {
						final Chunk neighbor = chunk.getNeighbors().get(j).getChunk();

						final float xWidthN = neighbor.getX() + getChunkGroup().getChunkWidth();
						final float yHeightN = neighbor.getY() + getChunkGroup().getChunkHeight();

						final boolean testN11 = v[Batch.X1] >= neighbor.getX();
						final boolean testN12 = v[Batch.X1] <= xWidthN;
						final boolean testN13 = v[Batch.Y1] >= neighbor.getY();
						final boolean testN14 = v[Batch.Y1] <= yHeightN;

						final boolean testN21 = v[Batch.X2] >= neighbor.getX();
						final boolean testN22 = v[Batch.X2] <= xWidthN;
						final boolean testN23 = v[Batch.Y2] >= neighbor.getY();
						final boolean testN24 = v[Batch.Y2] <= yHeightN;

						final boolean testN31 = v[Batch.X3] >= neighbor.getX();
						final boolean testN32 = v[Batch.X3] <= xWidthN;
						final boolean testN33 = v[Batch.Y3] >= neighbor.getY();
						final boolean testN34 = v[Batch.Y3] <= yHeightN;

						final boolean testN41 = v[Batch.X4] >= neighbor.getX();
						final boolean testN42 = v[Batch.X4] <= xWidthN;
						final boolean testN43 = v[Batch.Y4] >= neighbor.getY();
						final boolean testN44 = v[Batch.Y4] <= yHeightN;

						final boolean testN1 = testN11 && testN12 && testN13 && testN14;
						final boolean testN2 = testN21 && testN22 && testN23 && testN24;
						final boolean testN3 = testN31 && testN32 && testN33 && testN34;
						final boolean testN4 = testN41 && testN42 && testN43 && testN44;

						if (testN1 || testN2 || testN3 || testN4) {
							boolean has = false;
							for (Chunk chunkTemp : shell.getCurrentChunks()) {
								if (chunkTemp.equals(neighbor)) {
									has = true;
									break;
								}
							}
							if (!has)
								shell.getCurrentChunks().add(neighbor);
						} else {
							for (int k = 0; k < neighbor.getNeighbors().size; k++) {
								final Chunk other = neighbor.getNeighbors().get(k).getChunk();

								final float xWidthO = other.getX() + getChunkGroup().getChunkWidth();
								final float yHeightO = other.getY() + getChunkGroup().getChunkHeight();

								final boolean testO11 = v[Batch.X1] >= other.getX();
								final boolean testO12 = v[Batch.X1] <= xWidthO;
								final boolean testO13 = v[Batch.Y1] >= other.getY();
								final boolean testO14 = v[Batch.Y1] <= yHeightO;

								final boolean testO21 = v[Batch.X2] >= other.getX();
								final boolean testO22 = v[Batch.X2] <= xWidthO;
								final boolean testO23 = v[Batch.Y2] >= other.getY();
								final boolean testO24 = v[Batch.Y2] <= yHeightO;

								final boolean testO31 = v[Batch.X3] >= other.getX();
								final boolean testO32 = v[Batch.X3] <= xWidthO;
								final boolean testO33 = v[Batch.Y3] >= other.getY();
								final boolean testO34 = v[Batch.Y3] <= yHeightO;

								final boolean testO41 = v[Batch.X4] >= other.getX();
								final boolean testO42 = v[Batch.X4] <= xWidthO;
								final boolean testO43 = v[Batch.Y4] >= other.getY();
								final boolean testO44 = v[Batch.Y4] <= yHeightO;

								final boolean testO1 = testO11 && testO12 && testO13 && testO14;
								final boolean testO2 = testO21 && testO22 && testO23 && testO24;
								final boolean testO3 = testO31 && testO32 && testO33 && testO34;
								final boolean testO4 = testO41 && testO42 && testO43 && testO44;

								if (testO1 || testO2 || testO3 || testO4) {
									boolean has = false;
									for (Chunk chunkTemp : shell.getCurrentChunks()) {
										if (chunkTemp.equals(other)) {
											has = true;
											break;
										}
									}
									if (!has)
										shell.getCurrentChunks().add(other);
								}
							}
						}
					}
					delete.add(i);
				}
			}
			for (int i = delete.size - 1; i >= 0; i--) {
				shell.getCurrentChunks().remove(i);
			}
		}
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public Array<Spawn> getSpawnCommandOne() {
		return spawnCommandOne;
	}

	public Array<Spawn> getSpawnCommandTwo() {
		return spawnCommandTwo;
	}

	public ChunkGroup getChunkGroup() {
		return chunkGroup;
	}

	public String getName() {
		return name;
	}
}