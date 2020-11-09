package answers;

public class OtherPlayerLocalAnswer extends MyLocalAnswer {
	private static final long serialVersionUID = 1L;
	private int id;
	
	public OtherPlayerLocalAnswer (int id, float x, float y, float rotation, float rotationTower, float xTower, float yTower) {
		super(x, y, rotation, rotationTower, xTower, yTower);
		this.id = id;
	}
	
	public int getId () {
		return id;
	}
}