package map;

public class ChunkNeighbor {
	private Chunk chunk;
	private ChunkSide side;
	
	public ChunkNeighbor (Chunk chunk, ChunkSide side) {
		this.chunk = chunk;
		this.side = side;
	}
	
	public Chunk getChunk () {
		return chunk;
	}
	
	public ChunkSide getChunkSide () {
		return side;
	}
}