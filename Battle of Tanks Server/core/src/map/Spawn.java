package map;

public class Spawn {
	private boolean busy;
	
	private Vertex vertex;
	
	public Spawn (Vertex vertex) {
		this.vertex = vertex;
	}
	
	public void setBusy (boolean busy) {
		this.busy = busy;
	}
	
	public boolean isBusy () {
		return busy;
	}
	
	public Vertex getVertex () {
		return vertex;
	}
}