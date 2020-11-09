package map;

import com.badlogic.gdx.utils.Array;

public class Graph {
	private Array <Vertex> vertices;
	
	public Graph () {
		vertices = new Array <Vertex>(1);
	}
	
	public Array <Vertex> getVertices () {
		return vertices;
	}
}