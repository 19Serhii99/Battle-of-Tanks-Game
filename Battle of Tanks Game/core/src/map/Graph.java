package map;

import com.badlogic.gdx.utils.Array;

import util.Mathematics;

public class Graph {
	private Array <Vertex> vertices;
	
	public Graph () {
		vertices = new Array <Vertex>(1);
	}
	
	public void aStar (Vertex start, Vertex end) {
		Array <Vertex> closed = new Array <Vertex>();
		Array <Vertex> open = new Array <Vertex>();
		Array <Vertex> from = new Array <Vertex>();
		
		open.add(start);
		float g = 0;
		float f = Mathematics.getDistance(open.get(0).getX(), open.get(0).getY(), end.getX(), end.getY());
		
		while (open.size > 0) {
			Vertex curr = minF(open, end);
			if (curr.equals(end)) return;
			for (int i = 0; i < open.size; i++) {
				Vertex openV = open.get(i);
				if (curr.equals(openV)) {
					open.removeIndex(i);
					i--;
					closed.add(curr);
					for (int j = 0; j < openV.getNeighbors().size; j++) {
						Vertex neighbor = openV.getNeighbors().get(j);
						boolean hasInClosed = false;
						for (Vertex closedVertex : closed) {
							if (closedVertex.equals(neighbor)) {
								hasInClosed = true;
								break;
							}
						}
						if (!hasInClosed) {
							float tempG = g + Mathematics.getDistance(curr.getX(), curr.getY(), neighbor.getX(), neighbor.getY());
							boolean hasInOpen = false;
							for (int k = 0; k < open.size; k++) {
								if (open.get(k).equals(neighbor)) {
									hasInOpen = true;
									break;
								}
							}
							if (!hasInOpen) {
								from.add(neighbor);
								g = tempG;
								f = g + Mathematics.getDistance(neighbor.getX(), neighbor.getY(), end.getX(), end.getY());
							}
						}
					}
					break;
				}
			}
		}
	}
	
	private Vertex minF (Array <Vertex> open, Vertex end) {
		Vertex current = open.get(0);
		float f = Mathematics.getDistance(current.getX(), current.getY(), end.getX(), end.getY());
		for (int i = 0; i < open.size; i++) {
			Vertex vertex = open.get(i);
			float tempF = Mathematics.getDistance(open.get(i).getX(), open.get(i).getY(), end.getX(), end.getY());
			if (tempF < f) {
				f = tempF;
				current = vertex;
			}
		}
		return current;
	}
	
	public Array <Vertex> getVertices () {
		return vertices;
	}
}